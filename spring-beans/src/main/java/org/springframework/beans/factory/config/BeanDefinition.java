package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.core.AttributeAccessor;
import org.springframework.lang.Nullable;

/**
 * Bean定义接口
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

    /**
     * 单例对象标识符
     */
    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    /**
     * 原型实例标识符
     */
    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


    /**
     * 这个Bean是功能性应用程序的
     */
    int ROLE_APPLICATION = 0;

    int ROLE_SUPPORT = 1;

    int ROLE_INFRASTRUCTURE = 2;



    /**
     * 设置父BeanDefinition名称
     * @param name 名称
     */
    void setParentName(String name);

    /**
     * 获取父BeanDefinition名称
     * @return
     */
    String getParentName();

    /**
     * 获取Bean的类名
     */
    void setBeanClassName();

    /**
     * 设置Bean的类名
     * @return
     */
    String getBeanClassName();

    /**
     * 设置标识符
     * @param scope 标识符
     */
    void setScope(@Nullable  String scope);

    /**
     * 获取标识符
     * @return
     */
    String getScope();

    /**
     * 设置是否懒惰初始化
     * @param lazyInit 是否懒惰
     */
    void setLazyInit(boolean lazyInit);

    /**
     * 是否懒惰初始化
     * @return
     */
    boolean isLazyInit();

    /**
     * 设置依赖的BeanDefinition
     * @param dependsOn 依赖
     */
    void setDependsOn(String... dependsOn);

    /**
     * 获取依赖的BeanDefinition
     * @return
     */
    String[] getDependsOn();

    /**
     * 设置Bean是否可以自动注入
     */
    void autowireCandidate();

    /**
     * 是否可以自动注入
     * @return
     */
    boolean isAutowireCandidate();

    /**
     * 设置是否优先加载这个Bean
     * @param primary
     */
    void setPrimary(boolean primary);

    /**
     * 是否优先加载这个Bean
     * @return
     */
    boolean isPrimary();

    /**
     * 设置工厂Bean名称
     * @param factoryBeanName 工厂Bean名称
     */
    void setFactoryBeanName(String factoryBeanName);

    /**
     * 获取工厂Bean名称
     * @return
     */
    String getFactoryBeanName();

    /**
     * 设置工厂方法名称
     * @param factoryMethodName 工厂方法名称
     */
    void setFactoryMethodName(String factoryMethodName);

    /**
     * 获取工厂方法名称
     * @return
     */
    String getFactoryMethodName();
}
