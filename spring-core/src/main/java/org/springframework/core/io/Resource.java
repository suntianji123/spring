package org.springframework.core.io;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * 资源公共接口
 */
public interface Resource extends InputStreamSouce {

    /**
     * 判断资源是否存在
     * @return
     */
    boolean exists() throws IOException;


    /**
     * 获取可读的Channel对象
     * @return
     */
    default ReadableByteChannel readableByteChannel() throws IOException{
        return Channels.newChannel(getInputStream());
    };

    /**
     * 资源的长度
     * @return
     */
    int contentLength() throws IOException;

    /**
     * 资源最后一次修改时间
     * @return
     * @throws IOException
     */
    long lastModified() throws IOException;


    /**
     * 根据相对路径创建一个相对路径资源
     * @return
     */
    Resource createRelative(String relativePath) throws IOException;

    /**
     * 资源是否可读
     * @return
     * @throws IOException
     */
    default boolean isReadable() throws IOException{
        return exists();
    }

    /**
     * 资源是否打开
     * @return
     */
    default boolean isOpen(){
        return false;
    }

    /**
     * 是否为文件资源
     * @return
     */
    default boolean isFile(){
        return false;
    }

    /**
     * 获取资源定位符
     * @return
     */
    URL getURL() throws IOException;

    /**
     * 获取资源描述符
     * @return
     */
    URI getURI() throws IOException;

    /**
     * 获取文件
     * @return
     * @throws IOException
     */
    File getFile() throws IOException;

    /**
     * 获取文件名
     */
    String getFilename();

    /**
     * 获取资源描述
     * @return
     */
    String getDescription();
}
