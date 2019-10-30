package org.springframework.core;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AttributeAccessorSupport implements AttributeAccessor, Serializable {

    private final Map<String,Object> attributes = new LinkedHashMap<>();

    /**
     * 设置属性值
     * @param name 属性
     * @param value 值
     */
    @Override
    public void setAttribute(String name, @Nullable Object value){
        Assert.notNull(name,"Name must not be null");
        if(value != null){
            this.attributes.put(name,value);
        }else{
            this.attributes.remove(name);
        }
    }


    /**
     * 获取属性值
     * @param name 属性
     * @return
     */
    @Override
    @Nullable
    public Object getAttribute(String name){
        Assert.notNull(name,"Name must not be null");
        return this.attributes.get(name);
    }

    /**
     * 删除属性
     * @param name 属性
     */
    @Override
    public Object removeAttribute(String name){
        Assert.notNull(name,"Name must not be null");
        return this.attributes.remove(name);
    }

    /**
     * 是否包含属性
     * @param name 属性
     * @return
     */
    @Override
    public boolean hasAttribute(String name) {
        Assert.notNull(name,"Name must not be null");
        return this.attributes.containsKey(name);
    }

    /**
     * 获取所有的属性
     * @return
     */
    @Override
    public String[] attributeNames() {
        return StringUtils.toStringArray(this.attributes.keySet());
    }

    /**
     * 从某个attributeAccessor中复制属性
     * @param source
     */
    protected void copyAttributesFrom(AttributeAccessor source){
        Assert.notNull(source,"Source attribute Accessor must not be null");
        String[] attributeNames = source.attributeNames();
        for(String attribute : attributeNames){
            setAttribute(attribute,source.getAttribute(attribute));
        }
    }

    /**
     * 判断对象相等
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof AttributeAccessorSupport && this.attributes.equals(((AttributeAccessorSupport) other).attributes));
    }

    /**
     * 获取哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return this.attributes.hashCode();
    }
}
