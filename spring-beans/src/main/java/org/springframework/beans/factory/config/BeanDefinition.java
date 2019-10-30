package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
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

    /**
     * 获取构造函数参数值
     * @return
     */
    ConstructorArgumentValues getConstructorArgumentValues();

    /**
     * 判断是否有构造函数参数值
     * @return
     */
    default boolean hasConstructorArgumentValues(){
        return !getConstructorArgumentValues().isEmpty();
    };

    /**
     * 获取可变的属性值列表
     * @return
     */
    MutablePropertyValues getPropertyValues();

    /**
     * 判断是否有属性值
     * @return
     */
    default boolean hasPropertyValues(){
        return !getPropertyValues().isEmpty();
    }

    /**
     * 设置初始化方法名字
     * @param initMethodName
     */
    void setInitMethodName(@Nullable String initMethodName);

    /**
     * 获取初始化方法名字
     * @return
     */
    @Nullable
    String getInitMethodName();

    /**
     * 设置销毁方法名称
     * @param destoryMethodName 销毁方法名称
     */
    void setDestoryMethodName(@Nullable String destoryMethodName);

    /**
     * 获取销毁方法名称
     * @return
     */
    @Nullable
    String getDestoryMethodName();

    /**
     * 设置角色
     * @param role
     */
    void setRole(int role);

    /**
     * 获取角色
     * @return
     */
    int getRole();

    /**
     * 设置描述
     * @param description 描述
     */
    void setDescription(@Nullable  String description);

    /**
     * 获取描述
     * @return
     */
    String getDescription();

    /**
     * 是否是单例
     * @return
     */
    boolean isSingleTon();

    /**
     * 是否为原型
     * @return
     */
    boolean isProtoType();

    /**
     * 是否为抽象
     * @return
     */
    boolean isAbstract();

    /**
     * 获取资源描述
     * @return
     */
    String getResourceDescription();

    /**
     * 获取原始的BeanDefinition对象
     * @return
     */
    BeanDefinition getOriginalBeanDefinition();
}
