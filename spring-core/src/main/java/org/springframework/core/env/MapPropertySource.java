package org.springframework.core.env;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 集合属性源
 */
public class MapPropertySource extends EnumerablePropertySource<Map<String,Object>> {

    /**
     * 通过名称、集合对象构造属性源
     * @param name 名称
     * @param source 集合对象
     */
    public MapPropertySource(String name,Map<String,Object> source){
        super(name,source);
    }

    /**
     * 获取属性值
     * @param name 属性名
     * @return
     */
    @Override
    @Nullable
    public Object getProperty(String name) {
        return this.source.get(name);
    }

    /**
     * 是否包含属性
     * @param name 属性名称
     * @return
     */
    @Override
    public boolean containsProperty(String name) {
        return this.source.containsKey(name);
    }

    /**
     * 获取所有属性
     * @return
     */
    @Override
    public String[] getPropertyNames() {
        return StringUtils.toStringArray(this.source.keySet());
    }
}
