package org.springframework.core;

import org.springframework.lang.Nullable;

/**
 * 对象元数据访问接口
 */
public interface AttributeAccessor {

    /**
     * 设置属性值
     * @param name 属性
     * @param value 值
     */
    void setAttribute(String name,@Nullable Object value);

    /**
     * 获取属性值
     * @param name 属性
     * @return
     */
    @Nullable
    Object getAttribute(String name);

    /**
     * 删除属性
     * @param name 属性
     * @return
     */
    Object removeAttribute(String name);

    /**
     * 是否包含某个属性
     * @param name 属性
     * @return
     */
    boolean hasAttribute(String name);

    /**|
     * 获取所有属性
     * @return
     */
    String[] attributeNames();
}
