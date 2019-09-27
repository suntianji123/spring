package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 输入流资源接口
 */
public interface InputStreamSouce {

    /**
     * 获取输入流
     * @return
     */
    InputStream getInputStream() throws IOException;
}
