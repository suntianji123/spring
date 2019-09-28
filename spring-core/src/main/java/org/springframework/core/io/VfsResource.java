package org.springframework.core.io;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * 虚拟文件资源
 */
public class VfsResource extends AbstractResource {

    /**
     * VirtualFile实例
     */
    private Object resource;


    /**
     * 实例化VfsResource
     * @param resource VirtualFile实例
     */
    public VfsResource(@Nullable  Object resource){
        Assert.notNull(resource,"resource can not be null");
        this.resource = resource;
    }

    /**
     * 资源是否存在
     * @return
     */
    @Override
    public boolean exists() {
        return VfsUtils.exists(this.resource);
    }

    /**
     * 获取资源长度
     * @return
     * @throws IOException
     */
    @Override
    public long contentLength() throws IOException {
        return VfsUtils.getSize(this.resource);
    }

    /**
     * 获取资源最后一次修改时间
     * @return
     * @throws IOException
     */
    @Override
    public long lastModified() throws IOException {
        return VfsUtils.getLastModified(this.resource);
    }

    /**
     * 创建一个相对路径的资源
     * @param relativePath 相对路径
     * @return
     * @throws IOException
     */
    @Override
    public Resource createRelative(String relativePath) throws IOException {
        //如果路径为绝对路径
        if(!relativePath.startsWith(".")&&relativePath.contains("/")){
            try{
                return new VfsResource(VfsUtils.getChild(this.resource,relativePath));
            }catch (IOException ex){

            }
        }
        //如果路径为相对路径
        return new VfsResource(VfsUtils.getRelative(new URL(getURL(),relativePath)));
    }

    /**
     * 判断两个资源为同一资源
     * @param other 另一个资源对象
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof VfsResource && this.equals((VfsResource) other));
    }

    /**
     * 获取哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return this.resource.hashCode();
    }

    /**
     * 获取输入流
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return VfsUtils.getInputStream(this.resource);
    }

    /**
     * 资源是否可读
     * @return
     * @throws IOException
     */
    @Override
    public boolean isReadable() throws IOException {
        return VfsUtils.isReadable(this.resource);
    }

    /**
     * 获取资源定位符
     * @return
     * @throws IOException
     */
    @Override
    public URL getURL() throws IOException {
       return VfsUtils.getURL(this.resource);
    }

    /**
     * 获取资源描述符
     * @return
     * @throws IOException
     */
    @Override
    public URI getURI() throws IOException {
        return VfsUtils.getURI(this.resource);
    }

    /**
     * 获取文件
     * @return
     * @throws IOException
     */
    @Override
    public File getFile() throws IOException {
        return VfsUtils.getFile(this.resource);
    }

    /**
     * 获取文件名
     * @return
     */
    @Override
    public String getFilename() {
        return VfsUtils.getName(this.resource);
    }

    @Override
    public String getDescription() {
        return "VFS resource ["+this.resource+"]";
    }
}
