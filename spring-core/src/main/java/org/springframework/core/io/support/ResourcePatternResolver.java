package org.springframework.core.io.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * 资源加载器
 */
public interface ResourcePatternResolver extends ResourceLoader {

    String CLASS_ALL_URL_PREFIX = "classpath*:";

    /**
     * 加载多个资源
     * @param location 路径
     * @return
     * @throws IOException
     */
    Resource[] getResources(String location) throws IOException;
}
