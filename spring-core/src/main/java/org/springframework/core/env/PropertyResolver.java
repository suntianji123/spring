package org.springframework.core.env;

/**
 * 属性解析器
 */
public interface PropertyResolver {

    /**
     * 是否包含某个属性
     * @param key 属性名字
     * @return
     */
    boolean containsProperty(String key);

    /**
     * 获取某个属性的值
     * @param key 属性名字
     * @return
     */
    String getProperty(String key);

    /**
     * 获取某个属性值，如果没有这个属性，返回默认值
     * @param key 属性名
     * @param defaultValue 默认值
     * @return
     */
    String getProperty(String key,String defaultValue);

    /**
     * 获取某个属性值，指定返回值类型
     * @param key 属性名
     * @param targetType 返回值类型
     * @param <T> 返回值类型
     * @return
     */
    <T> T getProperty(String key,Class<T> targetType);

    /**
     * 返回某个属性值，指定返回值类型，没有属性，返回默认值
     * @param key 属性名
     * @param targetType 返回值类型
     * @param defaultValue 默认值
     * @param <T> 返回值类型
     * @return
     */
    <T> T getProperty(String key,Class<T> targetType,T defaultValue);

    /**
     * 获取某个一定存在的属性值
     * @param key 属性名
     * @return
     */
    String getRequiredProperty(String key);

    /**
     * 获取摸个一定存在的属性值，指定返回值类型
     * @param key  属性名
     * @param targetType 返回值类型
     * @param <T> 返回值类型
     * @return
     */
    <T> T getRequiredProperty(String key,Class<T> targetType);

    /**
     * 解析包含指定占位符的文本
     * @param text 文本
     * @return
     */
    String resolvePlaceholder(String text);

    /**
     * 解析一定包含指定占位符的文本
     * @param text 文本
     * @return
     */
    String resolveRequiredPlaceholders(String text);
}
