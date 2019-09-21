package org.springframework.util;


import org.springframework.lang.Nullable;

import java.io.File;
import java.net.*;

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
        Assert.notNull(resourceLocation,"resourceLocation不允许为空");

        //如果是classpath开始的路径，转为file类型资源
        if(resourceLocation.startsWith(CLASSPATH_URL_PREFIX)){
            //加载class文件资源
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            //获取类加载器，获取资源
            ClassLoader cl = ClassUtils.getDefaultClassLoader();
            URL url =  cl != null?cl.getResource(path):ClassUtils.class.getClassLoader().getResource(path);
            if(url == null){
                throw new IllegalArgumentException(resourceLocation+"资源路径不能解析为有效的资源");
            }
            return url;
        }

        try{
            return new URL(resourceLocation);
        }catch(MalformedURLException e) {
            //转为普通的文件资源
            try{
                return new File(resourceLocation).toURI().toURL();
            }catch (MalformedURLException ex){
                throw new IllegalArgumentException(resourceLocation+"资源路径不能解析为有效的资源");
            }

        }
    }

    /**
     * 根据给定的Url获取文件
     * @param url Url资源定位符对象
     * @return
     */
    public static File getFile(URL url){
        return getFile(url,"URL");
    }

    /**
     * 根据给定Url和描述获取文件 file://ftp.linkwan.com/pub/files/foobar.txt
     * @param url Url资源定位符对象
     * @param description 描述
     * @return
     */
    public static File getFile(@Nullable URL url,  String description){
        Assert.notNull(url,"URL["+description+"]参数不能为空");

        //如果不是文件类型的资源,抛出非法参数异常
        if(!URL_PROTOCOL_FILE.equals(url.getProtocol())){
            throw new IllegalArgumentException("URL["+description+"]必须为文件类型的资源");
        }
        //获取资源描述符中的文件路径
        //[scheme:]scheme-specific-part[#fragment] 返回scheme-specific-part部分
        // ftp.linkwan.com/pub/files/foobar.txt
        try{
            return new File(toURI(url).getSchemeSpecificPart());
        }catch (URISyntaxException e){
            return new File(url.getFile());
        }

    }

    /**
     * 格局给定的资源标识符和描述获取文件
     * @param uri 资源标识符
     * @param desciption 描述
     * @return
     */
    public static File getFile(URI uri,String desciption){
        Assert.notNull(uri,"URI["+desciption+"]参数不能为空");
        //判断资源描述符是否为 文件
        if(!URL_PROTOCOL_FILE.equals(uri.getScheme())){
            throw new IllegalArgumentException("URI["+desciption+"]资源不是文件资源");
        }
        return new File(uri.getSchemeSpecificPart());
    }

    /**
     * 根据资源描述符获取文件
     * @param uri 资源描述符
     * @return
     */
    public static File getFile(URI uri){
        return getFile(uri,"URI");
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
    public static URI toURI(URL url) throws URISyntaxException{
       //获取url的全路径
        return toURI(url.toString());
    }


    /**
     * 根据资源路径获取资源标识符
     * @param resourceLocation 资源路径
     * @return
     */
    public static URI toURI(String resourceLocation) throws URISyntaxException{
        return new URI(resourceLocation.replaceAll(" ","%20"));
    }

    /**
     * 对特定的URLConnection设置使用缓存
     * @param con
     */
    public static void useCacheIfNecessary(URLConnection con){
        con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
    }
}
