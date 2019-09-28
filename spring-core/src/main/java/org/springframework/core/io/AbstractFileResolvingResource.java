package org.springframework.core.io;

import org.springframework.util.ResourceUtils;
import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;

/**
 * 文件解析资源
 */
public abstract class AbstractFileResolvingResource extends AbstractResource {
    @Override
    public boolean exists() {

        try{
            URL url = getURL();
            if(ResourceUtils.isFileUrl(url)){
                //如果为文件资源定位符
                File file = getFile();
                //文件存在并且文件不是文件夹
                return file.canRead() && !file.isDirectory();
            }else{
                //如果为http网络资源，发送head请求，返回码为ok，则http资源存在
                //打开连接
                URLConnection con = url.openConnection();

                //自定义连接,发送header请求
                customizeConnection(con);
                HttpsURLConnection httpCon = con instanceof HttpsURLConnection?(HttpsURLConnection)con:null;
                if(httpCon != null){
                   int httpCode = httpCon.getResponseCode();
                   if(httpCode == HttpURLConnection.HTTP_OK){
                       return true;
                   }else if(httpCode == HttpURLConnection.HTTP_NOT_FOUND){
                       return false;
                   }
                }

                //获取header内容
                long contentLength = con.getContentLengthLong();
                if(contentLength > 0){
                    return true;
                }

                if(httpCon != null){
                    httpCon.disconnect();
                    return false;
                }else{
                    //再次判断是否为输入流资源
                    getInputStream().close();
                    return true;
                }
            }
        }catch (IOException e){
            return false;
        }
    }

    /**
     * 自定义连接
     * @param con
     */
    private void customizeConnection(URLConnection con) throws IOException {
        //设置缓存
        ResourceUtils.useCacheIfNecessary(con);
        if(con instanceof HttpURLConnection){
            //如果为http连接
            customizeConnection((HttpsURLConnection)con);
        }
    }

    /**
     * 自定义连接，发送header请求
     * @param con http连接
     */
    private  void customizeConnection(HttpURLConnection con) throws  IOException{
        con.setRequestMethod("HEAD");
    }

    /**
     * 判断给定uri是否问文件
     * @param uri 资源描述符
     * @return
     */
    protected boolean isFile(URI uri){
        //如果为虚拟文件描述符
        try{
            if(ResourceUtils.URL_PROTOCOL_VFS.equals(uri.getScheme())){
                //委托到VfsResource判断资源是否存在
                return VfsResourceDelegate.getResource(uri).isFile();
            }

            //普通文件描述符
            return ResourceUtils.URL_PROTOCOL_FILE.equals(uri.getScheme());
        }catch (IOException e){
            return false;
        }

    }


    /**
     * 获取文件
     * @param uri 资源描述符
     * @return
     */
    protected File getFile(URI uri) throws IOException{
        if(ResourceUtils.URL_PROTOCOL_VFS.equals(uri.getScheme())){
            //如果是虚拟文件资源，委托到VfsResource获取文件
            return VfsResourceDelegate.getResource(uri).getFile();
        }

        //普通文件资源
        return ResourceUtils.getFile(uri);
    }

    /**
     * 获取可读的字节管道
     * @return
     * @throws IOException
     */
    @Override
    public ReadableByteChannel readableByteChannel() throws IOException{
        try{
            //文件资源：打开文件资源
            return FileChannel.open(getFile().toPath(), StandardOpenOption.READ);
        }catch (IOException ex){
            return super.readableByteChannel();
        }
    }

    /**
     * 获取文件资源长度
     * @return
     * @throws IOException
     */
    @Override
    public long contentLength() throws IOException {
        URL url = getURL();
        if(ResourceUtils.isFileUrl(url)){
            //如果为文件资源
            File file = getFile();
            long length = file.length();
            if(length == 0 || !file.exists()){
                throw new FileNotFoundException("can not be resolved in file system for its content length");
            }
            return length;
        }else{
            //网络资源
            URLConnection conn = url.openConnection();
            //发送head请求
            customizeConnection(conn);
            //获取响应长度
            return conn.getContentLengthLong();
        }
    }

    /**
     * 获取文件 ： JarUrl VfsUrl FileUrl
     * @return
     * @throws IOException
     */
    @Override
    protected File getFileForLastModified() throws IOException {
        URL url = getURL();
        if(ResourceUtils.isJarURL(url)){
            URL acturalUrl = ResourceUtils.extractArchiveURL(url);

            if(ResourceUtils.URL_PROTOCOL_VFS.equals(acturalUrl.getProtocol())){
                //如果是虚拟文件资源定位符，委托到VfsResource处理
                return VfsResourceDelegate.getResource(url).getFile();
            }

            //普通文件资源
            return ResourceUtils.getFile(url,"Jar Url");
        }

        return getFile();
    }

    @Override
    public File getFile() throws IOException {
        URL url = getURL();
        if(ResourceUtils.URL_PROTOCOL_VFS.equals(url.getProtocol())){
            //虚拟文件资源，委托到VfsResource处理
           return VfsResourceDelegate.getResource(url).getFile();
        }
        //普通文件资源
        return ResourceUtils.getFile(url,getDescription());
    }

    /**
     * 资源最后一次修改时间
     * @return
     * @throws IOException
     */
    @Override
    public long lastModified() throws IOException {
        URL url = getURL();

        //文件资源
        boolean fileCheck = false;
        if(ResourceUtils.isFileUrl(url) || ResourceUtils.isJarURL(url)){
            //如果为FileUrl、JarUrl
            fileCheck = true;
            try {
                File file = getFileForLastModified();
                long lastModified = file.lastModified();
                if(lastModified > 0L || file.exists()){
                    return lastModified;
                }
            }catch (FileNotFoundException ex){

            }
        }

        //网络资源
        //打开连接
        URLConnection con = url.openConnection();
        //发送head请求
        customizeConnection(con);
        long lastModified = con.getLastModified();
        if(fileCheck && lastModified == 0L){
            throw new FileNotFoundException("can not be resolved in file system for its timestamp");
        }
        return lastModified;
    }

    /**
     * VfsResource委托类
     */
    private static final class VfsResourceDelegate{
        public static Resource getResource(URI uri) throws IOException{
            return new VfsResource(VfsUtils.getRoot(uri));
        }

        public static Resource getResource(URL url) throws IOException{
            return new VfsResource(VfsUtils.getRoot(url));
        }
    }

    /**
     * 资源是否可读
     */
    @Override
    public boolean isReadable(){
        try{
            URL url = getURL();
            if(ResourceUtils.isFileUrl(url)){
                //如果是文件资源，判断文件是否可读，并且文件存在
                File file = getFile();
                return file.canRead() && !file.isDirectory();
            }else{
                //网络连接资源
                URLConnection con = url.openConnection();
                customizeConnection(con);
                if(con instanceof HttpsURLConnection){
                    HttpURLConnection httpCon = (HttpsURLConnection)con;
                    if(httpCon.getResponseCode() != HttpURLConnection.HTTP_OK){
                        httpCon.disconnect();
                        return false;
                    }
                }

                long contentLength = con.getContentLengthLong();
                if(contentLength >0){
                    return true;
                }else if(contentLength == 0){
                    return false;
                }else{
                    getInputStream().close();
                    return true;
                }
            }
        }catch (IOException ex){
            return false;
        }
    }

    /**
     * 判断是否为文件资源
     * @return
     */
    @Override
    public boolean isFile() {
        try {
            URL url = getURL();
            if(ResourceUtils.URL_PROTOCOL_VFS.equals(url.getProtocol())){
                return VfsResourceDelegate.getResource(url).isFile();
            }
            return ResourceUtils.URL_PROTOCOL_FILE.equals(url.getProtocol());
        }catch (IOException ex){
            return false;
        }

    }
}
