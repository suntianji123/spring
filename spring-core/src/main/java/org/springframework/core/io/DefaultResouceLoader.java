package org.springframework.core.io;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultResouceLoader implements ResourceLoader {


    /**
     * 类加载器
     */
    @Nullable
    private ClassLoader classLoader;

    /**
     * 协议解析器
     */
    private final Set<ProtocolResolver> protocolResolvers = new LinkedHashSet<>(4);

    /**
     * 类对象和资源之间的关系缓存
     */
    private final Map<Class<?>,Map<Resource,?>> resourceCaches = new ConcurrentHashMap<>();


    /**
     * 构造资源加载器
     */
    public DefaultResouceLoader(){
        this. classLoader = ClassUtils.getDefaultClassLoader();
    }

    /**
     * 通过类加载器构造资源加载器
     * @param classLoader 类加载器
     */
    public DefaultResouceLoader(@Nullable ClassLoader classLoader){
        this.classLoader = classLoader;
    }

    /**
     * 添加协议解析器
     * @param protocolResolver 协议解析器
     */
    public void addProtocolResolver(ProtocolResolver protocolResolver){
        Assert.notNull(protocolResolver,"Protocol Resolver must not be null");
        this.protocolResolvers.add(protocolResolver);
    }

    /**
     * 获取给定类型的资源缓存
     * @param valueType 类型
     * @param <T>
     * @return
     */
    public <T> Map<Resource,T> getResourceCache(Class<T> valueType){
        return (Map<Resource,T>)this.resourceCaches.computeIfAbsent(valueType,key ->new ConcurrentHashMap<>());
    }

    /**
     * 清楚资源缓存
     */
    public void clearResourceCaches(){
        this.resourceCaches.clear();
    }


    @Override
    public Resource getResource(String location) {
        Assert.notNull(location,"location must not be null");

        //通过协议解析器去获取资源
        for(ProtocolResolver protocolResolver : this.protocolResolvers){
            Resource resource = protocolResolver.resolve(location,this);
            if(resource != null){
                return resource;
            }
        }


        if(location.startsWith("/")){
            //如果路径为绝对路径，返回上下文资源
            return getResourceByPath(location);
        }else if(location.startsWith(CLASSPATH_URL_PREFIX)){
            //如果为classpath路径
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()),getClassLoader());
        }else{

            try{
                URL url = new URL(location);
                //文件资源获取其他资源
                return ResourceUtils.isFileUrl(url)?new FileUrlResource(location):new UrlResource(location);
            }catch (MalformedURLException ex){
                return getResourceByPath(location);
            }

        }
    }

    /**
     * 根据路径获取资源，路径必须为绝对路径
     * @param path 路径
     * @return
     */
    protected Resource getResourceByPath(String path){
        Assert.notNull(path,"Path must not be null");
        return new ClassPathContextResource(path,getClassLoader());
    }


    public void setClassLoader(@Nullable ClassLoader classLoader){
        this.classLoader = classLoader;
    }

    /**
     * 获取类加载器
     * @return
     */
    @Override
    @Nullable
    public ClassLoader getClassLoader() {
        return this.classLoader != null?this.classLoader:ClassUtils.getDefaultClassLoader();
    }

    /**
     * 获取协议解析器
     * @return
     */
    public Collection<ProtocolResolver> getProtocolResolvers(){
        return this.protocolResolvers;
    }

    /**
     * classpath上下文资源
     */
    protected static class ClassPathContextResource extends ClassPathResource implements ContextResource{

        /**
         * 通过路径和加载器构造资源
         * @param path 路径
         * @param classLoader 加载器
         */
        public ClassPathContextResource(String path, @Nullable ClassLoader classLoader){
            super(path,classLoader);
        }

        /**
         * 获取上下文路径
         * @return
         */
        @Override
        public String getPathWithinContext() {
            return getPath();
        }

        /**
         * 创建一个相对路径的资源
         * @param relativePath 相对路径
         * @return
         * @throws IOException
         */
        @Override
        public Resource createRelative(String relativePath) throws IOException {
            String pathToUse = StringUtils.applyRelativePath(getPath(),relativePath);
            return new ClassPathContextResource(pathToUse,getClassLoader());
        }
    }
}
