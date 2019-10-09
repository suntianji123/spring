package org.springframework.core.io;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * 文件Url资源
 */
public class FileUrlResource extends UrlResource implements WritableResource {

    /**
     * 文件
     */
    private volatile File file;

    /**
     * 通过资源定位符构造资源
     * @param url 资源定位符
     */
    public FileUrlResource(URL url){
        super(url);
    }

    /**
     * 通过位置构造资源
     * @param location
     */
    public FileUrlResource(String location) throws MalformedURLException {
        super(ResourceUtils.URL_PROTOCOL_FILE,location);
    }

    /**
     * 获取文件
     * @return
     * @throws IOException
     */
    @Override
    public File getFile() throws IOException {
        if(this.file != null){
            return this.file;
        }

        File file = super.getFile();
        this.file = file;
        return file;
    }

    /**}
     * 获取可写的字节管道
     * @return
     * @throws IOException
     */
    @Override
    public WritableByteChannel getWritableByteChannel() throws IOException {
        return FileChannel.open(getFile().toPath(), StandardOpenOption.WRITE);
    }

    /**
     * 创建相对路径
     * @param relativePath 相对路径
     * @return
     * @throws IOException
     */
    @Override
    public Resource createRelative(String relativePath) throws IOException {
        if(relativePath.startsWith("/")){
            relativePath = relativePath.substring(1);
        }
        return new FileUrlResource(new URL(getURL(),relativePath));
    }

    /**
     * 是否可写
     * @return
     */
    @Override
    public boolean isWritable() {
        try{
            URL url = getURL();
            if(ResourceUtils.isFileUrl(url)){
                File file = getFile();
                return file.canWrite() && !file.isDirectory();
            }else{
                return true;
            }
        }catch (IOException ex){
            return false;
        }
    }

    /**
     * 获取输出流
     * @return
     * @throws IOException
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        return Files.newOutputStream(getFile().toPath());
    }
}
