package org.springframework.core.io;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.*;

public class FileSystemResource extends AbstractResource implements WritableResource {

    /**
     * 文件路径
     */
    private final String path;

    /**
     * 文件
     */
    private final File file;

    /**
     * 文件路径
     */
    private final Path filePath;

    /**
     * 通过路径构造文件资源
     * @param path 路径
     */
    public FileSystemResource(String path){
        Assert.notNull(path,"Path must not be null");
        this.path = StringUtils.cleanPath(path);
        this.file = new File(path);
        this.filePath = this.file.toPath();
    }

    /**
     * 通过文件构造文件资源
     * @param file 文件
     */
    public FileSystemResource(File file){
        Assert.notNull(file,"File must not be null");
        this.path = StringUtils.cleanPath(file.getPath());
        this.file = file;
        this.filePath = file.toPath();
    }

    /**
     * 通过文件路径对象构造资源
     * @param filePath 文件路径对象
     */
    public FileSystemResource(Path filePath){
        Assert.notNull(filePath,"File path must not be null");
        this.path = StringUtils.cleanPath(filePath.toString());
        this.file = null;
        this.filePath = filePath;
    }

    /**
     * 根据文件系统和路径构造资源
     * @param fileSystem 文件系统
     * @param path 路径
     */
    public FileSystemResource(FileSystem fileSystem,String path){
        Assert.notNull(fileSystem,"FileSystem must not be null");
        Assert.notNull(path,"Path must not be null");
        this.path = StringUtils.cleanPath(path);
        this.file = null;
        this.filePath = fileSystem.getPath(this.path).normalize();
    }

    /**
     * 判断资源是否存在
     * @return
     */
    @Override
    public boolean exists() {
        return this.file != null?this.file.exists(): Files.exists(this.filePath);
    }

    /**
     * 可读的字节管道
     * @return
     * @throws IOException
     */
    @Override
    public ReadableByteChannel readableByteChannel() throws IOException {
        try{
            return FileChannel.open(this.filePath, StandardOpenOption.READ);
        }catch (NoSuchFileException ex){
            throw  new FileNotFoundException(ex.getMessage());
        }

    }

    /**
     * 获取可写的字节管道
     * @return
     * @throws IOException
     */
    @Override
    public WritableByteChannel getWritableByteChannel() throws IOException {
        return FileChannel.open(this.filePath,StandardOpenOption.WRITE);
    }

    /**
     * 获取资源长度
     * @return
     * @throws IOException
     */
    @Override
    public long contentLength() throws IOException {
        if(this.file != null){
            long length = this.file.length();
            if(length == 0L && !file.exists()){
                throw new FileNotFoundException(getDescription() +"can not be resolving in file system check its length");
            }
            return length;
        }else{
            try{
                return Files.size(this.filePath);
            }catch (NoSuchFileException ex){
                throw  new FileNotFoundException(ex.getMessage());
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
        if(this.file != null){
            return super.lastModified();
        }else{
            try{
                return Files.getLastModifiedTime(this.filePath).toMillis();
            }catch (NoSuchFileException ex){
                throw new FileNotFoundException(ex.getMessage());
            }
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
        return this.file != null?new FileSystemResource(pathToUse):new FileSystemResource(this.filePath.getFileSystem(),pathToUse);
    }

    /**
     * 判断是否为同一资源
     * @param other 另一个资源对象
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof FileSystemResource && this.path.equals(((FileSystemResource)other).path));
    }

    @Override
    public int hashCode() {
        return this.path.hashCode();
    }

    /**
     * 获取路径
     * @return
     */
    public String getPath(){
        return this.path;
    }

    /**
     * 获取文件
     * @return
     */
    @Override
    public File getFile() {
        return this.file != null?this.file:this.filePath.toFile();
    }

    /**
     * 是否可读
     * @return
     */
    @Override
    public boolean isReadable() {
       return this.file != null?this.file.canRead() && !this.file.isDirectory() : Files.isReadable(this.filePath) && !Files.isDirectory(this.filePath);
    }

    /**
     * 获取输入流
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        try{
            return Files.newInputStream(this.filePath);
        }catch (NoSuchFileException ex){
            throw new FileNotFoundException(ex.getMessage());
        }

    }

    /**
     * 是否可写
     * @return
     */
    @Override
    public boolean isWritable() {
        return this.file != null ? this.file.canWrite() && !this.file.isDirectory() : Files.isWritable(this.filePath) && !Files.isDirectory(this.filePath);
    }

    /**
     * 获取输出流
     * @return
     * @throws IOException
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        return Files.newOutputStream(this.filePath);
    }

    /**
     * 获取资源定位符
     * @return
     * @throws IOException
     */
    @Override
    public URL getURL() throws IOException {
        return this.file != null?this.file.toURI().toURL():this.filePath.toUri().toURL();
    }

    /**
     * 获取资源描述符
     * @return
     * @throws IOException
     */
    @Override
    public URI getURI() throws IOException {
        return this.file != null?this.file.toURI():this.file.toURI();
    }

    /**
     * 是文件
     * @return
     */
    @Override
    public boolean isFile() {
        return true;
    }

    /**
     * 获取文件名
     * @return
     */
    @Override
    public String getFilename() {
        return this.file != null?this.file.getName():this.filePath.getFileName().toString();
    }

    /**
     * 获取描述
     * @return
     */
    @Override
    public String getDescription() {
        return "file [" +(this.file != null?this.file.getAbsolutePath():this.filePath.toAbsolutePath())+"]";
    }
}
