package org.springframework.core.env;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * 属性源
 * @param <T> 源类型
 */
public abstract class PropertySource<T> {

    /**
     * 日志对象
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 属性源名称
     */
    protected String name;

    /**
     * 属性源源对象
     */
    protected T source;


    /**
     * 通过名称和源对象构建属性源
     * @param name 名称
     * @param source 源对象
     */
    public PropertySource(String name,T source) {
        Assert.notNull(name, "Property source name must not be null");
        Assert.notNull(source, "Propery source source must not be null");
        this.name = name;
        this.source = source;
    }

    /**
     * 通过名称构建属性源对象
     * @param name 名称
     */
    public PropertySource(String name){
        this(name,(T)new Object());
    }

    /**
     * 获取属性源名称
     * @return
     */
    public String getName(){
        return this.name;
    }

    /**
     * 获取属性源对象
     * @return
     */
    public T getSource(){
        return this.source;
    }

    /**
     * 是否包含某个属性
     * @param name 属性名称
     * @return
     */
    public boolean containsProperty(String name){
        return getProperty(name) != null;
    }

    /**
     * 获取某个属性的值（由子类来实现）
     * @param name 属性名
     * @return
     */
    @Nullable
    public abstract Object getProperty(String name);


    /**
     * 判断两个属性源是否为同一个属性源
     * @param other 另一个属性源
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return this == other ||(other instanceof PropertySource<?>
                && ObjectUtils.nullSafeEquals(this.name,((PropertySource<?>)other).name));
    }

    /**
     * 获取属性源的哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.name);
    }

    /**
     * 转为字符串
     * @return
     */
    @Override
    public String toString() {
        if (logger.isDebugEnabled()) {
            return getClass().getSimpleName() + "@" + System.identityHashCode(this) +
                    " {name='" + this.name + "', properties=" + this.source + "}";
        }
        else {
            return getClass().getSimpleName() + " {name='" + this.name + "'}";
        }
    }


    /**
     * 用于存根的属性源
     */
    public static class StubPropertySource extends PropertySource<Object>{

        /**
         * 根据名称构造属性源
         * @param name 名称
         */
        public StubPropertySource(String name){
            super(name,new Object());
        }

        /**
         * 获取属性值
         * @param name 属性名
         * @return
         */
        @Override
        @Nullable
        public Object getProperty(String name) {
            return null;
        }
    }

    /**
     * 用于集合比较的属性源
     */
    static class ComparisonPropertySource extends StubPropertySource{

        private static final String USAGE_ERROR =
                "ComparisonPropertySource instance are for use with collection comparison only";

        /**
         * 通过名称构造属性源
         * @param name 名称
         */
        public ComparisonPropertySource(String name){
            super(name);
        }


        /**
         * 获取资源源对象
         * @return
         */
        @Override
        public Object getSource() {
            throw new UnsupportedOperationException(USAGE_ERROR);
        }

        /**
         * 是否包含属性
         * @param name 属性名称
         * @return
         */
        @Override
        public boolean containsProperty(String name) {
            throw new UnsupportedOperationException(USAGE_ERROR);
        }

        /**
         * 获取属性值
         * @param name 属性名
         * @return
         */
        @Override
        public Object getProperty(String name) {
            throw new UnsupportedOperationException(USAGE_ERROR);
        }

        /**
         * 获取一个用于集合比较的属性源
         * @param name 名称
         * @return
         */
        public static PropertySource<?> named(String name){
            return new ComparisonPropertySource(name);
        }
    }
}
