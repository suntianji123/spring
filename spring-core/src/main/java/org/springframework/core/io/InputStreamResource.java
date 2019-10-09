package org.springframework.core.io;


import org.springframework.util.Assert;

import java.io.InputStream;

/**
 * 输入流资源
 */
public class InputStreamResource extends AbstractResource {

    /**
     * 输入流
     */
    private final InputStream inputStream;

    /**
     * 描述
     */
    private final String description;

    /**
     * 可读
     */
    private boolean read = false;

    /**
     *通过输入流构造资源
     * @param inputStream 输入流
     */
    public InputStreamResource(InputStream inputStream){
        this(inputStream,"resource loaded through inputStream ");
    }

    /**
     * 通过输入流和描述构造资源
     * @param inputStream 输入流
     * @param description 描述
     */
    public InputStreamResource(InputStream inputStream,String description){
        Assert.notNull(inputStream,"InputStream must not be null");
        this.inputStream = inputStream;
        this.description = description != null?description:"";
    }

    /**
     * 资源是否存在
     * @return
     */
    @Override
    public boolean exists() {
        return true;
    }

    /**
     * 资源是否相等
     * @param other 另一个资源对象
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return this == other || ((other instanceof InputStreamResource) && this.inputStream.equals(((InputStreamResource)other).inputStream));
    }

    @Override
    public int hashCode() {
        return this.inputStream.hashCode();
    }

    /**
     * 获取输入流资源
     * @return
     */
    @Override
    public InputStream getInputStream() {
        if(read){
            throw  new IllegalStateException("InputStream has already been read - " +
                    "do not use InputStreamResource if a stream needs to be read multiple times");
        }

        this.read = true;
        return this.inputStream;
    }

    @Override
    public String getDescription() {
        return "InputStream Resource ["+this.description+"]";
    }

    /**
     * 资源是否打开
     * @return
     */
    @Override
    public boolean isOpen() {
        return true;
    }
}
