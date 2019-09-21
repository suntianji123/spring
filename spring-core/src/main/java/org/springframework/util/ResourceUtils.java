package org.springframework.util;

import com.sun.istack.internal.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public abstract class ResourceUtils {

    /**
     * classpath资源定位符前缀
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * file资源定位符前缀
     */
    public static final String FILE_URL_PREFIX = "file:";

    /**
     * jar资源定位符前缀
     */
    public static final String JAR_URL_PREFIX = "jar:";

    /**
     * war资源定位符前缀
     */
    public static final String WAR_URL_PREFIX = "war:";

    /**
     * file资源定位符协议名
     */
    public static final String URL_PROTOCOL_FILE = "file";

    /**
     * jar资源定位符协议名
     */
    public static final String URL_PROTOCOL_JAR = "jar";

    /**
     * war资源定位符协议名
     */
    public static final String URL_PROTOCOL_WAR = "war";

    /**
     * zip资源定位符协议名
     */
    public static final String URL_PROTOCOL_ZIP = "zip";

    /**
     * wsjar资源定位符协议名
     */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";

    /**
     *  vfszip资源定位符协议名
     */
    public  static final String URL_PROTOCOL_VFSJZIP = "vfszip";

    /**
     * vfsfile资源定位符协议名
     */
    public static final String URL_PROTOCOL_VFSFILE = "vfsfile";

    /**
     * vfs资源定位符协议名
     */
    public static final String URL_PROTOCOL_VFS = "vfs";

    /**
     * jar文件名后缀
     */
    public static final String JAR_FILE_EXTENSION = ".jar";

    /**
     * jar资源定位符分隔符
     */
    public static final String JAR_URL_SEPARATOR = "!/";

    /**
     * war资源定位符分隔符
     */
    public static final String WAR_URL_SEPARATOR = "*/";





    /**
     * 判断资源路径是否为有效的Url路径
     * @param resourceLocation 资源路径地址
     * @return
     */
    public static boolean isURL(@Nullable String resourceLocation){
        if(resourceLocation == null){
            return false;
        }

        if(resourceLocation.startsWith(CLASSPATH_URL_PREFIX)){
            return true;
        }

        try {
            new URL(resourceLocation);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * 根据给定的资源路径获取Url资源定位符对象
     * @param resourceLocation 资源路径地址
     * @return
     */
    public static URL getURL(String resourceLocation){


        return null;
    }

    /**
     * 根据给定的Url获取文件
     * @param url Url资源定位符对象
     * @return
     */
    public static File getFile(URL url){
        return null;
    }

    /**
     * 根据给定Url和描述获取文件
     * @param url Url资源定位符对象
     * @param description 描述
     * @return
     */
    public static File getFile(URL url,String description){
        return null;
    }

    /**
     * 格局给定的资源标识符和描述获取文件
     * @param uri 资源标识符
     * @param desciption 描述
     * @return
     */
    public static File getFile(URI uri,String desciption){
        return null;
    }

    /**
     * 根据资源描述符获取文件
     * @param uri 资源描述符
     * @return
     */
    public static File getFile(URI uri){
        return null;
    }

    /**
     * 判断给定的资源定位符是否为文件资源定位符
     * @param url 资源定位符
     * @return
     */
    public static boolean isFileUrl(URL url){
        return false;
    }

    /**
     * 判断给定的资源定位符是否为jar资源定位符
     * @param url 资源定位符
     * @return
     */
    public static boolean isJarURL(URL url){
        return false;
    }

    /**
     * 判断给定的资源定位符是否为jar文件资源定位符
     * @param url 资源定位符
     * @return
     */
    public static boolean jarFileURL(URL url){
        return false;
    }

    /**
     * 根据给定资源定位符获取jar文件资源定位符
     * @param url 资源定位符
     * @return
     */
    public static URL extractJarFileURL(URL url){
        return null;
    }

    /**
     * 根据给定的资源定位符获取war或者jar文件资源定位符
     * @param url 资源定位符
     * @return
     */
    public static URL extractArchiveURL(URL url){
        return null;
    }

    /**
     * 根据资源定位符获取资源标识符
     * @param url 资源定位符
     * @return
     */
    public static URI toURI(URL url){
        return  null;
    }


    /**
     * 根据资源路径获取资源标识符
     * @param resourceLocation 资源路径
     * @return
     */
    public static URI toURI(String resourceLocation){
        return null;
    }

    /**
     * 对特定的URLConnection设置使用缓存
     * @param con
     */
    public static void useCacheIfNecessary(URLConnection con){
        con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
    }
}
