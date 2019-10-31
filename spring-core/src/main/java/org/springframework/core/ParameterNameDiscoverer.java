package org.springframework.core;

import org.springframework.lang.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 参数名发现接口
 */
public interface ParameterNameDiscoverer {

    /**
     * 获取方法的参数名数组
     * @param method 方法实例
     * @return
     */
    @Nullable
    String[] getParameterNames(Method method);

    /**
     *  获取构造函数的参数名数组
     * @param ctor 构造函数实例
     * @return
     */
    String[] getParameterNames(Constructor<?> ctor);
}
