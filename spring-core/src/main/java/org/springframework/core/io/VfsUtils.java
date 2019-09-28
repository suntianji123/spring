package org.springframework.core.io;

import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

/**
 * Vfs
 */
public abstract class VfsUtils {
    /**
     * vfs包名
     */
    private static final String VFS3_PKG = "org.jboss.vfs.";

    /**
     * VFS名称
     */
    private static final String VFS_NAME = "VFS";

    /**
     * 通过URL获取VirtualFile实例的方法
     */
    private static final Method VFS_METHOD_GET_ROOT_URL;

    /**
     * 通过URI获取VirtualFile实例的方法
     */
    private static final Method VFS_METOD_GET_ROOT_URI;

    /**
     * 判断虚拟文件是否存在
     */
    private static final Method VIRTUAL_FILE_METHOD_EXISTS;

    /**
     * 获取虚拟文件输入流资源
     */
    private static final Method VIRTUAL_FILE_METHOD_GET_INPUT_STREAM;

    /**
     * 获取虚拟文件大小
     */
    private static final Method VIRTUAL_FILE_METHOD_GET_SIZE;

    /**
     * 获取虚拟文件最后一次修改日期
     */
    private static final Method VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED;

    /**
     * 获取URL
     */
    private static final Method VIRTUAL_FILE_METHOD_TO_URL;

    /**
     * 获取URI
     */
    private static final Method VIRTUAL_FILE_METHOD_TO_URI;

    /**
     * 获取虚拟文件名
     */
    private static final Method VIRTUAL_FILE_METHOD_GET_NAME;

    /**
     * 获取虚拟文件路径
     */
    private static final Method VIRTUAL_FILE_METHOD_GET_PATH_NAME;

    /**
     * 获取虚拟文件的实际文件
     */
    private static final Method VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE;

    /**
     * 获取虚拟文件的子虚拟文件
     */
    private static final Method VIRTUAL_FILE_METHOD_GET_CHILD;

    /**
     * 访问者接口
     */
    private static final Class<?> VIRTUAL_FILE_VISITOR_INTERFACE;

    /**
     * 访问虚拟文件
     */
    private static final Method VIRTUAL_FILE_METHOD_VISIT;

    /**
     * 访问者字段
     */
    private static final Field VISITOR_ATTRIBUTES_FIELD_RECURSE;

    static {
        ClassLoader loader = VfsUtils.class.getClassLoader();
        try{
            Class<?> vfsClass = loader.loadClass(VFS3_PKG + VFS_NAME);
            VFS_METOD_GET_ROOT_URI = vfsClass.getMethod("getRoot", URI.class);
            VFS_METHOD_GET_ROOT_URL = vfsClass.getMethod("getRoot",URL.class);

            Class<?> virtualFileClass = loader.loadClass(VFS3_PKG + "VirtualFile");
            VIRTUAL_FILE_METHOD_EXISTS = virtualFileClass.getMethod("exists");
            VIRTUAL_FILE_METHOD_GET_INPUT_STREAM = virtualFileClass.getMethod("getInputStream");
            VIRTUAL_FILE_METHOD_GET_SIZE = virtualFileClass.getMethod("getSize");
            VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = virtualFileClass.getMethod("getLastModified");
            VIRTUAL_FILE_METHOD_TO_URL = virtualFileClass.getMethod("toURL");


            VIRTUAL_FILE_METHOD_TO_URI = virtualFileClass.getMethod("toURI");
            VIRTUAL_FILE_METHOD_GET_NAME = virtualFileClass.getMethod("getName");
            VIRTUAL_FILE_METHOD_GET_PATH_NAME = virtualFileClass.getMethod("getPathName");
            VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE = virtualFileClass.getMethod("getPhysicalFile");
            VIRTUAL_FILE_METHOD_GET_CHILD = virtualFileClass.getMethod("getChild");

            VIRTUAL_FILE_VISITOR_INTERFACE = loader.loadClass(VFS3_PKG + "VirtualFileVisitor ");
            VIRTUAL_FILE_METHOD_VISIT = virtualFileClass.getMethod("visit",VIRTUAL_FILE_VISITOR_INTERFACE);

            Class<?> visitorAttributesClass = loader.loadClass(VFS3_PKG + "VisitorAttributes");
            VISITOR_ATTRIBUTES_FIELD_RECURSE = visitorAttributesClass.getField("RECURSE");

        }catch (Throwable ex){
            throw new IllegalStateException("Can not detect JBoss VFS infrastructure",ex);
        }

    }

    /**
     * 通过反射执行vfs资源对象的反射方法
     * @param method 方法
     * @param vfsResource vfsResource对象
     * @param args 参数
     * @return
     */
    protected static Object invokeVfsMethod(Method method,Object vfsResource,Object...args) throws IOException {
        try{
            return method.invoke(vfsResource,args);
        }catch (InvocationTargetException ex){
            Throwable throwable = ex.getTargetException();
            //如果不为RuntimeExcetion 并且为IOException直接抛出
            if(throwable instanceof IOException){
                throw (IOException)throwable;
            }
            ReflectionUtils.handleInvocationTargetException(ex);
        }catch (Exception ex){
            ReflectionUtils.handleReflectionException(ex);
        }
        throw new IllegalStateException("Invalid code path reached");
    }

    /**
     * 判断虚拟文件系统是否存在
     * @param vfsResource 虚拟文件
     * @return
     */
    static boolean exists(Object vfsResource){
        try{
            return (Boolean) invokeVfsMethod(VIRTUAL_FILE_METHOD_EXISTS,vfsResource);
        }catch (IOException ex){
            return false;
        }
    }

    /**
     * 判断虚拟文件是否可读
     * @param vfsResource 虚拟文件
     * @return
     */
    static boolean isReadable(Object vfsResource){
        try{
            return (Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE,vfsResource) > 0;
        }catch (IOException ex){
            return false;
        }
    }

    /**
     * 获取虚拟文件大小
     * @param vfsResource 虚拟文件
     * @return
     * @throws IOException
     */
    static long getSize(Object vfsResource) throws IOException{
        return (Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE,vfsResource);
    }

    /**
     * 获取虚拟文件最后一次修改时间
     * @param vfsResource 虚拟文件
     * @return
     * @throws IOException
     */
    static long getLastModified(Object vfsResource) throws IOException{
        return (Long) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED,vfsResource);
    }

    /**
     * 获取输入流资源
     * @param vfsResource 虚拟文件
     * @return
     * @throws IOException
     */
    static InputStream getInputStream(Object vfsResource) throws IOException{
        return (InputStream) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_INPUT_STREAM,vfsResource);
    }

    /**
     * 获取URL
     * @param vfsResource 虚拟文件
     * @return
     * @throws IOException
     */
    static URL getURL(Object vfsResource) throws IOException{
        return (URL)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URL,vfsResource);
    }

    /**
     * 获取URI
     * @param vfsResource
     * @return
     * @throws IOException
     */
    static URI getURI(Object vfsResource) throws IOException{
        return (URI)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URI,vfsResource);
    }

    /**
     * 获取虚拟文件名
     * @param vfsResource 虚拟文件
     * @return
     */
    static String getName(Object vfsResource){
        try{
            return (String) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_NAME,vfsResource);
        }catch (IOException e){
            throw new IllegalStateException("Can not get resource name",e);
        }
    }

    /**
     * 根据url获取虚拟文件
     * @param url 资源定位符
     * @return
     * @throws IOException
     */
    static Object getRelative(URL url) throws IOException{
        return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL,null,url);
    }

    /**
     * 获取虚拟文件的子虚拟文件
     * @param vfsResource 虚拟文件
     * @param path 路径
     * @return
     * @throws IOException
     */
    static Object getChild(Object vfsResource,String path) throws IOException{
        return invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_CHILD,vfsResource,path);
    }

    /**
     * 获取文件
     * @param vfsResource 虚拟文件
     * @return
     * @throws IOException
     */
    static File getFile(Object vfsResource) throws IOException {
        return (File)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE,vfsResource);
    }

    /**
     * 根据uri获取虚拟文件
     * @param uri 资源描述符
     * @return
     * @throws IOException
     */
    static Object getRoot(URI uri) throws IOException{
        return invokeVfsMethod(VFS_METOD_GET_ROOT_URI,null,uri);
    }

    /**
     * 根据url获取虚拟文件
     * @param url 资源定位符
     * @return
     * @throws IOException
     */
    static Object getRoot(URL url) throws IOException{
        return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL,null,url);
    }


    /**
     * 获取访问对象的属性值
     * @return
     */
    @Nullable
    protected static Object doGetAttributes(){
        return ReflectionUtils.getField(VISITOR_ATTRIBUTES_FIELD_RECURSE,null);
    }

    /**
     * 获取虚拟文件路径
     * @return
     */
    @Nullable
    protected  static Object doGetPath(Object resource){
        return ReflectionUtils.invokeMethod(VIRTUAL_FILE_METHOD_GET_PATH_NAME,resource);
    }
}