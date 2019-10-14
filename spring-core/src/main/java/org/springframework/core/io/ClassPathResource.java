package org.springframework.core.io;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ClassPathResource extends AbstractFileResolvingResource {

    /**
     * 路径
     */
    private final String path;

    /**
     * 类加载器
     */
    @Nullable
    private ClassLoader classLoader;

    /**
     * 类对象
     */
    @Nullable
    private Class<?> clazz;

    /**
     * 通过路径构造资源
     * @param path 路径
     */
    public ClassPathResource(String path){
        this(path,(ClassLoader) null);
    }

    /**
     * 通过路径和类加载器构造资源
     * @param path 路径
     * @param classLoader 类加载器
     */
    public ClassPathResource(String path,@Nullable  ClassLoader classLoader){
        Assert.notNull(path,"Path must not be null");
        String pathToUse = StringUtils.cleanPath(path);
        if(pathToUse.startsWith("/")){
            pathToUse = pathToUse.substring(1);
        }
        this.path = pathToUse;
        this.classLoader = classLoader != null?classLoader: ClassUtils.getDefaultClassLoader();
    }

    /**
     * 通过路径和类对象加载资源
     * @param path 路径
     * @param clazz 类对象
     */
    public ClassPathResource(String path,@Nullable  Class<?> clazz){
        Assert.notNull(path,"Path must not be null");
        this.path = StringUtils.cleanPath(path);
        this.clazz = clazz;
    }

    /**
     * 通过路径、类加载器、类对象构造资源
     * @param path 路径
     * @param classLoader 类加载器
     * @param clazz 类对象
     */
    public ClassPathResource(String path,@Nullable ClassLoader classLoader,@Nullable Class<?> clazz){
        Assert.notNull(path,"Path must not be null");
        this.path = StringUtils.cleanPath(path);
        this.classLoader = classLoader;
        this.clazz = clazz;
    }

    /**
     * 判断是否存在
     * @return
     */
    @Override
    public boolean exists() {
        return resolveURL() != null;
    }

    /**
     * 获取URL
     * @return
     */
    protected URL resolveURL(){
        if(this.clazz != null){
            return this.clazz.getResource(this.path);
        }else if(this.classLoader != null){
            return this.classLoader.getResource(this.path);
        }else{
            return ClassLoader.getSystemResource(this.path);
        }
    }

    /**
     * 创建一个相对路径资源
     * @param relativePath 相对路径
     * @return
     * @throws IOException
     */
    @Override
    public Resource createRelative(String relativePath) throws IOException {
        String pathToUse = StringUtils.applyRelativePath(this.path,relativePath);
        return this.clazz != null?new ClassPathResource(pathToUse,this.clazz):new ClassPathResource(pathToUse,this.classLoader);
    }

    /**
     * 判断是否相等
     * @param other 另一个资源对象
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }

        if(!(other instanceof ClassPathResource)){
            return false;
        }

        ClassPathResource otherRes = (ClassPathResource) other;
        return this.path.equals(otherRes.path) && ObjectUtils.nullSafeEquals(this.clazz,otherRes.clazz)
                && ObjectUtils.nullSafeEquals(this.classLoader,otherRes.classLoader);
    }

    @Override
    public int hashCode() {
        return this.path.hashCode();
    }

    /**
     * 获取路径
     * @return
     */
    public final String getPath(){
        return this.path;
    }

    /**
     * 获取类加载器
     * @return
     */
    @Nullable
    public ClassLoader getClassLoader() {
        return this.clazz != null?this.clazz.getClassLoader():this.classLoader;
    }

    /**
     * 获取输入流
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = null;
        if(this.clazz != null){
            is = this.clazz.getResourceAsStream(this.path);
        }else if(this.classLoader != null){
            is = this.classLoader.getResourceAsStream(this.path);
        }else{
            is = ClassLoader.getSystemResourceAsStream(this.path);
        }

        if(is == null){
            throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
        }

        return is;
    }

    /**
     * 获取资源定位符
     * @return
     * @throws IOException
     */
    @Override
    public URL getURL() throws IOException {
        URL url = resolveURL();
        if(url == null){
            throw new FileNotFoundException(getDescription() + " cannot be resolved to URL because it does not exist");
        }
        return url;
    }

    /**
     * 获取文件名
     * @return
     */
    @Override
    public String getFilename() {
        return StringUtils.getFilename(this.path);
    }

    /**
     * 获取描述
     * @return
     */
    @Override
    public String getDescription() {
        StringBuilder builder = new StringBuilder("class path resource [");
        String pathToUse = this.path;
        if (this.clazz != null && !pathToUse.startsWith("/")) {
            builder.append(ClassUtils.classPackageAsResourcePath(this.clazz));
            builder.append('/');
        }
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        builder.append(pathToUse);
        builder.append(']');
        return builder.toString();
    }
}
