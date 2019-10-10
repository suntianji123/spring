package org.springframework.core.io;

/**
 * 协议解析器
 */
public interface ProtocolResolver {

    /**
     * 通过路径和资源加载器获取资源
     * @param location 路径
     * @param resourceLoader 资源加载器
     * @return
     */
    Resource resolve(String location,ResourceLoader resourceLoader);
}
