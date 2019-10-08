package org.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

/**
 * 可写的资源接口
 */
public interface WritableResource extends Resource {

    /**
     * 判断是否可写
     * @return
     */
    default  boolean isWritable(){
        return true;
    }

    /**
     * 获取输出流
     * @return
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * 获取可写的字节管道
     * @return
     * @throws IOException
     */
    default WritableByteChannel getWritableByteChannel() throws IOException{
        return Channels.newChannel(getOutputStream());
    }
}
