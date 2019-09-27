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
    private final String descption;
    private final byte[] byteArray;

    public ByteArrayResource(byte[] byteArray){
        this("resource load from byte array",byteArray);
    }

    public ByteArrayResource(@Nullable  String descption, byte[] byteArray){
        Assert.notNull(byteArray,"byteArray can not be null");
        this.descption = descption == null?"":descption;
        this.byteArray = byteArray;
    }

    @Override
    public String getDescription() {
        return "Byte Array Resource ["+this.descption+"]";
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.byteArray);
    }

    protected byte[] getByteArray(){
        return this.byteArray;
    }

    @Override
    public int contentLength() throws IOException {
        return this.byteArray.length;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof ByteArrayResource && Arrays.equals(this.byteArray,((ByteArrayResource) other).getByteArray()));
    }

    @Override
    public int hashCode() {
        return (byte[].class.hashCode() * 29 * this.byteArray.length);
    }
}
