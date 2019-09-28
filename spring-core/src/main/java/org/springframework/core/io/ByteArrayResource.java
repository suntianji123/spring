package org.springframework.core.io;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 字节数组资源
 */
public class ByteArrayResource extends AbstractResource {
    /**
     * 描述
     */
    private final String descption;

    /**
     * 字节数组
     */
    private final byte[] byteArray;

    public ByteArrayResource(byte[] byteArray){
        this("resource load from byte array",byteArray);
    }

    public ByteArrayResource(@Nullable  String descption, byte[] byteArray){
        Assert.notNull(byteArray,"byteArray can not be null");
        this.descption = descption == null?"":descption;
        this.byteArray = byteArray;
    }

    /**
     * 获取描述
     * @return
     */
    @Override
    public String getDescription() {
        return "Byte Array Resource ["+this.descption+"]";
    }

    /**
     * 获取输入流
     * @return
     */
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.byteArray);
    }

    /**
     * 获取字节数组
     * @return
     */
    protected byte[] getByteArray(){
        return this.byteArray;
    }

    /**
     * 获取资源长度
     * @return
     * @throws IOException
     */
    @Override
    public long contentLength() throws IOException {
        return this.byteArray.length;
    }

    /**
     * 判断资源是否存在
     * @return
     */
    @Override
    public boolean exists() {
        return true;
    }

    /**
     * 判断两个资源是否为同一资源
     * @param other 另一个资源对象
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof ByteArrayResource && Arrays.equals(this.byteArray,((ByteArrayResource) other).getByteArray()));
    }

    /**
     * 哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return (byte[].class.hashCode() * 29 * this.byteArray.length);
    }
}
