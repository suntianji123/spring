package org.springframework.core.env;

import org.springframework.util.ObjectUtils;

/**
 * 可枚举的属性源
 */
public abstract class EnumerablePropertySource<T> extends PropertySource<T> {

    /**
     * 通过名称和属性源源对象构造实例
     * @param name 名称
     * @param source 源对象
     */
    public EnumerablePropertySource(String name,T source){
        super(name,source);
    }

    /**
     * 通过名称构造属性源实例
     * @param name 名称
     */
    protected EnumerablePropertySource(String name){
        super(name);
    }

    /**
     * 是否包含某个属性
     * @param name 属性名称
     * @return
     */
    @Override
    public boolean containsProperty(String name) {
        return ObjectUtils.containsElement(getPropertyNames(),name);
    }

    /**
     * 获取所有的属性名
     * @return
     */
    public abstract String[] getPropertyNames();

}
