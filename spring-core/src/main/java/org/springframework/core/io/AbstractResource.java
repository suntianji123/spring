package org.springframework.core.io;


import org.springframework.core.NestedIOException;
import org.springframework.core.log.LogAccessor;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public abstract class AbstractResource implements Resource{

    private static final LogAccessor logAccessor = new LogAccessor(AbstractResource.class.getName());

    @Override
    public boolean exists() {
       try{
           //如果为文件资源，判断文件是否存在
           return getFile().exists();
       }catch (IOException e){
            //如果为流资源，判断流是否可以关闭
           try{
               getInputStream().close();
               return true;
           }catch (Throwable ex){
               logAccessor.debug(ex,()->"Can not close inputStream for resource："+getDescription());
               return false;
           }
       }
    }

    /**
     * 可取可读的字节管道对象
     * @return
     * @throws IOException
     */
    @Override
    public ReadableByteChannel readableByteChannel() throws IOException {
        return Channels.newChannel(getInputStream());
    }

    /**
     * 获取资源长度
     * @return
     * @throws IOException
     */
    @Override
    public long contentLength() throws IOException {
        InputStream in = getInputStream();

        try{
            int size = 0;
            byte[] bytes = new byte[256];
            int read;
            //每次最大读取256个字节
            while((read = in.read(bytes))!=-1){
                size += read;
            }
            return size;
        }finally {
            try{
                in.close();
            }catch (Throwable ex){
                logAccessor.debug(ex,()->"can not close inputStream for resource:"+getDescription());
            }
        }
    }

    /**
     * 获取资源最后一次修改时间
     * @return
     * @throws IOException
     */
    @Override
    public long lastModified() throws IOException {
        File lastModifiedFile = getFileForLastModified();
        long lastModified = lastModifiedFile.lastModified();
        if(lastModified == 0L || !lastModifiedFile.exists()){
            throw new FileNotFoundException("can not found file in file system for resource :"+getDescription());
        }
        return lastModified;
    }

    /**
     * 创建一个相对路径的资源
     * @param relativePath 相对路径
     * @return
     * @throws IOException
     */
    @Override
    public Resource createRelative(String relativePath) throws IOException {
        throw new FileNotFoundException("can not create a relativePath resource for :"+getDescription());
    }

    /**
     * 判断两个Resource对象是否为同一资源对象
     * @param other 另一个资源对象
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof Resource && this.getDescription().equals(((Resource) other).getDescription()));
    }

    /**
     * 获取哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return getDescription().hashCode();
    }

    /**
     * 覆盖toString方法
     * @return
     */
    @Override
    public String toString() {
        return getDescription();
    }

    /**
     * 获取最后一次修改的文件
     * @return
     * @throws IOException
     */
    protected File getFileForLastModified() throws IOException{
        return getFile();
    }

    /**
     * 判断资源是否可读
     * @return
     * @throws IOException
     */
    @Override
    public boolean isReadable() throws IOException {
        return exists();
    }

    /**
     * 资源是否打开
     * @return
     */
    @Override
    public boolean isOpen() {
        return false;
    }

    /**
     * 判断是否为文件
     * @return
     */
    @Override
    public boolean isFile() {
        return false;
    }

    /**
     * 获取资源定位符
     * @return
     */
    @Override
    public URL getURL() throws IOException {
        throw new FileNotFoundException("can not get URL for Resource :"+getDescription());
    }

    /**
     * 获取资源描述符
     * @return
     * @throws IOException
     */
    @Override
    public URI getURI() throws IOException {
        URL url = getURL();
        try{
            return ResourceUtils.toURI(url);
        }catch (URISyntaxException e){
            throw new NestedIOException("Invalid URI ["+url+"]",e);
        }
    }

    /**
     * 获取文件
     * @return
     * @throws IOException
     */
    @Override
    public File getFile() throws IOException {
        throw new FileNotFoundException("can get file for resource :"+getDescription());
    }

    /**
     * 获取文件名
     * @return
     */
    @Override
    public String getFilename() {
        return null;
    }
}
