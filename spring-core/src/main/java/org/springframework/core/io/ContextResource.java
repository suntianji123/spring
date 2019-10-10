package org.springframework.core.io;

/**
 * 上下文资源接口
 */
public interface ContextResource extends Resource {

    /**
     * 获取上下文路径
     * @return
     */
    String getPathWithinContext();
}
