package org.springframework.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 文件复制工具类
 */
public abstract class FileCopyUtils {

    /**
     * 写入字节数组缓存区大小
     */
    public static final int BUFFER_SIZE = StreamUtils.BUFFER_SIZE;

    /**
     * 读取输入，写入输出
     * @param in 输入
     * @param out 输出
     * @return
     * @throws IOException
     */
    public static int copy(Reader in, Writer out) throws IOException{
        Assert.notNull(in,"Reader must not be null");
        Assert.notNull(out,"Writer must not be null");

        int byteCount = 0;
        char[] bytes = new char[BUFFER_SIZE];
        int readBytes = -1;
        try {
            while ((readBytes = in.read(bytes))!=-1){
                out.write(bytes,0,readBytes);
                byteCount += readBytes;
            }
            out.flush();
            return byteCount;
        }finally {
            try{
                in.close();
            }catch (IOException ex){

            }

            try {
                out.close();
            }catch (IOException ex){

            }
        }

    }

    /**
     * 将阅读器转成字符串
     * @param in 阅读器
     * @return
     */
    public static String copyToString(Reader in) throws IOException{
        if(in == null){
            return "";
        }

        StringWriter out = new StringWriter();
        copy(in,out);
        return out.toString();
    }
}