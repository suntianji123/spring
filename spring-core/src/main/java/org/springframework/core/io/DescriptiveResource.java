package org.springframework.core.io;

import org.springframework.lang.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 描述的资源
 */
public class DescriptiveResource extends AbstractResource {

    /**
     * 描述
     */
    private final String description;

    public DescriptiveResource(@Nullable String description){
        this.description = description != null?description:"";
    }

    /**
     * 资源是否存在
     * @return
     */
    @Override
    public boolean exists() {
        return false;
    }

    /**
     * 判断两个资源是否相同
     * @param other 另一个资源对象
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return this == other ||(other instanceof DescriptiveResource &&
                ((DescriptiveResource)other).description.equals(this.description));
    }

    /**
     * 哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return this.description.hashCode();
    }

    /**
     * 获取描述
     * @return
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * 是否可读
     * @return
     * @throws IOException
     */
    @Override
    public boolean isReadable() throws IOException {
        return false;
    }

    /**
     * 获取输入流
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        throw new FileNotFoundException("can not be open because it do not point a readable resource");
    }
}
