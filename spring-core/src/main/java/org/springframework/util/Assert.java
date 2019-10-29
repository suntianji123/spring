package org.springframework.util;

import org.springframework.lang.Nullable;

/**
 * 断言类
 */
public abstract class Assert {

    /**
     * 断言非法参数,必须为空
     * @param object 参数对象
     * @param message 错误信息
     */
    public static void isNull(@Nullable Object object,String message){
        if(object != null){
            //抛出非法参数异常
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言非法参数，参数不能为空
     * @param object 参数对象
     * @param message 错误信息
     */
    public static void notNull(@Nullable Object object,String message){
        if(object == null){
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言表达式为True
     * @param expression 表达式
     * @param message 错误信息
     */
    public static void isTrue(boolean expression,String message){
        if(!expression){
            throw new IllegalArgumentException(message);
        }
    }
}
