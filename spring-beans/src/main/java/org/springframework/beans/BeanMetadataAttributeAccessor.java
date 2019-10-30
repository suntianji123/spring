package org.springframework.beans;

import org.springframework.core.AttributeAccessorSupport;
import org.springframework.lang.Nullable;

/**
 * 元数据属性访问器
 */
public class BeanMetadataAttributeAccessor extends AttributeAccessorSupport implements BeanMetadataElement {

    /**
     * 属性所属对象
     */
    @Nullable
    private Object source;

    /**
     * 设置属性所属对象
     * @param source
     */
    public void setSource(@Nullable Object source){
        this.source = source;
    }

    /**
     * 获取属性所属对象
     * @return
     */
    public Object getSource(){
        return this.source;
    }

    /**
     * 添加属性
     * @param attribute 属性
     */
    public void addBeanMetadataAttribute(BeanMetadataAttribute attribute){
        super.setAttribute(attribute.getName(),attribute);
    }

    /**
     * 获取属性BeanMetadataAttribute对象
     * @param name 属性值
     * @return
     */
    @Nullable
    public BeanMetadataAttribute getBeanMetadataAttribute(String name){
        return (BeanMetadataAttribute) super.getAttribute(name);
    }

    /**
     * 设置属性值（BeanMetadataAttribute对象）
     * @param name 属性
     * @param value 值
     */
    @Override
    public void setAttribute(String name, Object value) {
        super.setAttribute(name, new BeanMetadataAttribute(name, value));
    }

    /**
     * 获取属性BeanMetadataAttribute的值
     * @param name 属性
     * @return
     */
    @Override
    @Nullable
    public Object getAttribute(String name) {
        BeanMetadataAttribute attribute = (BeanMetadataAttribute) super.getAttribute(name);
        return (attribute != null?attribute.getValue():null);
    }

    /**
     * 删除属性值
     * @param name 属性
     * @return
     */
    @Override
    @Nullable
    public Object removeAttribute(String name) {
        BeanMetadataAttribute attribute = (BeanMetadataAttribute) super.removeAttribute(name);
        return attribute != null ? attribute.getValue() : null;
    }
}
