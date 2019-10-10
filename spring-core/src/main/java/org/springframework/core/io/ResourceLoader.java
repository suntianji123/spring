package org.springframework.core.io;

import org.springframework.util.ResourceUtils;

/**
 * 资源加载器
 */
public interface ResourceLoader {

    /**
     *classpath资源前缀
     */
    String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;

    /**
     * 获取资源
     * @param location 资源位置
     * @return
     */
    Resource getResource(String location);

    /**
     * 获取类加载器
     * @return
     */
    ClassLoader getClassLoader();
}
