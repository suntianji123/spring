package org.springframework.beans;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * 元数据属性
 */
public class BeanMetadataAttribute implements BeanMetadataElement {

    /**
     * 属性名
     */
    private final String name;

    /**
     * 属性值
     */
    @Nullable
    private final Object value;

    /**
     * 属性所属对象
     */
    @Nullable
    private Object source;

    /**
     * 通过属性名、属性值构造元数据属性
     * @param name 属性名
     * @param value 属性值
     */
    public BeanMetadataAttribute(String name,@Nullable Object value){
        Assert.notNull(name,"Name must not be null");
        this.name = name;
        this.value = value;
    }

    /**
     * 判断对象相等
     * @param other 另一个对象
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }

        if(!(other instanceof BeanMetadataAttribute)){
            return false;
        }

        BeanMetadataAttribute otherMe = (BeanMetadataAttribute) other;
        return this.name.equals(otherMe.name) &&
                ObjectUtils.nullSafeEquals(this.value,otherMe.value) && ObjectUtils.nullSafeEquals(this.source,otherMe.source);
    }

    /**
     * 哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
    }

    @Override
    public String toString() {
        return "metadata attribute '" + this.name+"'";
    }

    /**
     * 获取属性名
     * @return
     */
    public String getName(){
        return this.name;
    }

    /**
     * 获取属性值
     * @return
     */
    @Nullable
    public Object getValue(){
        return this.value;
    }

    /**
     * 获取属性所属对象
     * @return
     */
    @Override
    @Nullable
    public Object getSource(){
        return this.source;
    }

    /**
     * 设置属性所属对象
     * @param source 所属对象
     */
    public void setSource(@Nullable Object source){
        this.source = source;
    }
}
