package org.springframework.core.io;

import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class UrlResource extends AbstractFileResolvingResource {

    /**
     * 资源定位符
     */
    private final URL url;

    /**
     * 资源描述符
     */
    private final URI uri;

    /**
     * 简化的资源定位符
     */
    private final URL cleanedUrl;

    /**
     * 通过资源描述符构造资源对象
     * @param uri 资源描述符
     * @throws MalformedURLException
     */
    public UrlResource(URI uri) throws MalformedURLException {
        Assert.notNull(uri,"URI must not be null");
        this.uri = uri;
        this.url = uri.toURL();
        this.cleanedUrl = getCleanedUrl(this.url,this.url.toString());
    }

    /**
     * 通过资源定位符构造资源对象
     * @param url 资源定位符
     */
    public UrlResource(URL url){
        Assert.notNull(url,"URL must not be null");
        this.url = url;
        this.cleanedUrl = getCleanedUrl(this.url,this.url.toString());
        this.uri = null;
    }

    /**
     * 通过资源定位符路径构造资源对象
     * @param path 资源路径
     * @throws MalformedURLException
     */
    public UrlResource(String path) throws MalformedURLException {
        this.uri = null;
        this.url = new URL(path);
        this.cleanedUrl = getCleanedUrl(this.url,path);
    }

    /**
     * 通过协议名，位置构造资源
     * @param protocol 协议名
     * @param location 位置
     * @throws Exception
     */
    public UrlResource(String protocol,String location) throws MalformedURLException{
        this(protocol,location,null);
    }


    /**
     * 通过协议名，位置，附加信息构造资源
     * @param protocol 协议名
     * @param location 位置
     * @param fragment 附加信息
     */
    public UrlResource(String protocol,String location,String fragment) throws MalformedURLException {
        try{
            this.uri = new URI(protocol,location,fragment);
            this.url = this.uri.toURL();
            this.cleanedUrl = getCleanedUrl(this.url,this.url.toString());
        }catch (URISyntaxException ex){
            MalformedURLException malformedURLException = new MalformedURLException(ex.getMessage());
            malformedURLException.initCause(ex.getCause());
            throw malformedURLException;
        }
    }

    /**
     * 获取简化后的资源
     * @param originalUrl 原始资源定位符
     * @param originalPath 原始资源路径
     * @return
     */
    private URL getCleanedUrl(URL originalUrl,String originalPath){
        String path = StringUtils.cleanPath(originalPath);
        if(!path.equals(originalPath)){
            try{
                return new URL(path);
            }catch (MalformedURLException e){

            }
        }
        return originalUrl;
    }

    /**
     * 创建一个相对路径的资源
     * @param relativePath 相对路径
     * @return
     * @throws IOException
     */
    @Override
    public Resource createRelative(String relativePath) throws IOException {
        if(relativePath.startsWith("/")){
            relativePath = relativePath.substring(1);
        }
        return new UrlResource(new URL(this.url,relativePath));
    }

    /**
     * 判断两个资源是否为同一资源
     * @param other 另一个资源对象
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof UrlResource && this.cleanedUrl .equals(((UrlResource)other).cleanedUrl));
    }

    /**
     * 获取哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return this.cleanedUrl.hashCode();
    }

    /**
     * 获取输入流
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        ResourceUtils.useCacheIfNecessary(con);
        try{
            return con.getInputStream();
        }catch (IOException ex){
            if(con instanceof  HttpURLConnection){
                ((HttpURLConnection) con).disconnect();
            }
            throw  ex;
        }
    }

    /**
     * 获取资源定位符
     * @return
     */
    @Override
    public URL getURL() {
        return this.url;
    }

    /**
     * 获取资源描述符
     * @return
     */
    @Override
    public URI getURI() {
        return this.uri;
    }

    /**
     * 判断是否为文件
     * @return
     */
    @Override
    public boolean isFile() {
        if(this.uri != null){
            return super.isFile(this.uri);
        }
        return super.isFile();
    }

    /**
     * 获取文件
     * @return
     * @throws IOException
     */
    @Override
    public File getFile() throws IOException {
        if(this.uri != null){
            return super.getFile(uri);
        }
        return super.getFile();
    }

    /**
     * 获取文件名
     * @return
     */
    @Override
    public String getFilename() {
        return StringUtils.getFilename(this.cleanedUrl.getPath());
    }

    /**
     * 获取描述
     * @return
     */
    @Override
    public String getDescription() {
        return "URL [" +this.url+"]";
    }
}
