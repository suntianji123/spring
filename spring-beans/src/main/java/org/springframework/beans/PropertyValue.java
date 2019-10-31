package org.springframework.beans;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * 保存bean单个属性信息
 */
public class PropertyValue extends BeanMetadataAttributeAccessor implements Serializable {

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
     * 是否可选
     */
    private boolean optional = false;

    /**
     * 是否已经转换
     */
    private boolean converted = false;

    /**
     * 转换过后的值
     */
    @Nullable
    private Object convertedValue;

    /**
     * 是否有必要转换
     */
    @Nullable
    volatile Boolean conversionNecessary;

    /**
     * 解析令牌
     */
    @Nullable
    transient volatile Object resolvedTokens;


    /**
     * 通过属性名、值构造属性值
     * @param name 属性名
     * @param value 属性值
     */
    public PropertyValue(String name,@Nullable Object value){
        Assert.notNull(name,"Name must not be null");
        this.name = name;
        this.value = value;
    }

    /**
     * 通过指定一个PropertyValue实例对象构造
     * @param original propertyValue实例
     */
    public PropertyValue(PropertyValue original){
        this.name = original.name;
        this.value = original.value;
        this.optional = original.optional;
        this.conversionNecessary = original.conversionNecessary;
        this.converted = original.converted;
        this.convertedValue = original.convertedValue;
        this.resolvedTokens = original.resolvedTokens;
        setSource(original.getSource());
        copyAttributesFrom(original);
    }

    /**
     * 通过指定一个ProperyValue和一个值构造实例
     * @param original PropertyValue实例
     * @param newValue 新值
     */
    public PropertyValue(PropertyValue original,@Nullable Object newValue){
        this.name = original.name;
        this.value = newValue;
        this.optional = original.optional;
        this.conversionNecessary = original.conversionNecessary;
        this.resolvedTokens = original.resolvedTokens;
        setSource(original.getSource());
        copyAttributesFrom(original);
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

        if(!(other instanceof PropertyValue)){
            return false;
        }

        PropertyValue otherPro = (PropertyValue) other;
        return this.name.equals(otherPro.name) &&
                ObjectUtils.nullSafeEquals(this.value,otherPro.value) && ObjectUtils.nullSafeEquals(this.getSource(),otherPro.getSource());
    }

    /**
     * 哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return this.name.hashCode()*29 + ObjectUtils.nullSafeHashCode(this.value);
    }

    @Override
    public String toString() {
        return "bean property '"+this.name+"'";
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
     * 设置可选
     * @param optional 可选
     */
    public void setOptional(boolean optional){
        this.optional = optional;
    }

    /**
     * 是否可选
     * @return
     */
    public boolean isOptional(){
        return this.optional;
    }

    /**
     * 设置是否转换
     * @param converted 是否转换
     */
    public void setConverted(boolean converted){
        this.converted = converted;
    }

    /**
     * 是否已经转换
     * @return
     */
    public boolean isConverted(){
        return this.converted;
    }

    /**
     * 设置转换后的值
     * @param convertedValue 转换后的值
     */
    public void setConvertedValue(@Nullable Object convertedValue){
        this.convertedValue = convertedValue;
    }

    /**
     * 获取转换后的值
     * @return
     */
    @Nullable
    public Object getConvertedValue(){
        return this.convertedValue;
    }

    /**
     * 获取原始的propertyValue
     * @return
     */
    public PropertyValue getOriginalPropertyValue(){
        PropertyValue original = this;
        Object source = original.getSource();
        while (source instanceof PropertyValue && source != original){
            original = (PropertyValue)source;
            source = original.getSource();
        }
        return original;
    }
}
