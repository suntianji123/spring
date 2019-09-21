package org.springframework.util;

/**
 * 类操作工具
 */
public abstract class ClassUtils {

    /**
     * 获取类加载器
     * @return
     */
    public static ClassLoader getDefaultClassLoader(){

        //优先获取上下文类加载器
        ClassLoader cl = null;
        try{
            Thread.currentThread().getContextClassLoader();
        }catch (Throwable ex){

        }
        if(cl == null){
            //获取工具类类加载器
            cl = ClassUtils.class.getClassLoader();
            if(cl == null){
                try{
                    //再次获取系统加载器
                    cl = ClassLoader.getSystemClassLoader();
                }catch (Throwable ex){

                }
            }
        }

        return cl;
    }
}
