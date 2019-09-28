package org.springframework.util;

import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 反射工具类
 */
public abstract  class ReflectionUtils {

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /**
     * 处理方法内部异常,抛出为不可检查异常（RuntimeException)和错误(Error)
     * @param ex 异常
     */
    public static void handleInvocationTargetException(InvocationTargetException ex){
        reThrowRuntimeException(ex.getTargetException());
    }


    /**
     * 抛出运行时异常
     * @param ex 异常
     */
    private static void reThrowRuntimeException(Throwable ex){
        if(ex instanceof RuntimeException){
            //如果是运行时异常，直接抛出
            throw (RuntimeException)ex;
        }

        //如果是错误，直接抛出
        if(ex instanceof Error){
            throw (Error)ex;
        }

        //抛出出没有声明的异常
        throw new UndeclaredThrowableException(ex);
    }

    /**
     * 处理反射异常
     * @param ex 异常
     */
    public static void handleReflectionException(Exception ex){
        if(ex instanceof NoSuchMethodException){
            //用反射执行方法时，目标对象中没有这个方法，抛出异常
            throw new IllegalStateException("No such method :"+ex.getMessage());
        }

        if(ex instanceof IllegalAccessException){
            //用反射执行方法时，目标对象的方法不允许访问，抛出异常
            throw new IllegalStateException("Can not access method:"+ex.getMessage());
        }

        if(ex instanceof InvocationTargetException){
            //方法内部异常
            handleInvocationTargetException((InvocationTargetException)ex);
        }

        if(ex instanceof RuntimeException){
            //如果为运行时异常，直接抛出
            throw (RuntimeException)ex;
        }

        throw new UndeclaredThrowableException(ex);
    }

    /**
     * 获取对象某个字段值
     * @param field 字段
     * @param target 目标对象
     * @return
     */
    public static Object getField(Field field,@Nullable Object target){
        try{
            return field.get(target);
        }catch (IllegalAccessException ex){
            handleReflectionException(ex);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    /**
     * 反射执行方法
     * @param method 方法
     * @param target 目标
     * @return
     */
    public static Object invokeMethod(Method method,@Nullable Object target){
        return invokeMethod(method,target,EMPTY_OBJECT_ARRAY);
    }

    /**
     * 反射执行方法
     * @param method 方法
     * @param target 目标实例
     * @param args 参数
     * @return
     */
    public static Object invokeMethod(Method method,@Nullable  Object target,Object... args){
        try {
            return method.invoke(target,args);
        }catch (Exception ex){
            handleReflectionException(ex);
        }
        throw new IllegalStateException("Should new get here");
    }
}
