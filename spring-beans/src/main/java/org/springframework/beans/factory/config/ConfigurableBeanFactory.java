package org.springframework.beans.factory.config;

public interface ConfigurableBeanFactory {

    /**
     * 单例
     */
    String SCOPE_SINGLETON = "singleton";

    /**
     * 原型
     */
    String SCOPE_PROTOTYPE = "prototype";
}
