package org.springframework.util;

import org.springframework.lang.Nullable;

/**
 * 类操作工具
 */
public abstract class ClassUtils {

    private static final char PACKAGE_SEPARATOR = '.';

    private static final char PATH_SEPARATOR = '/';

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

    public static String classPackageAsResourcePath(@Nullable Class<?> clazz) {
        if (clazz == null) {
            return "";
        }
        String className = clazz.getName();
        int packageEndIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        if (packageEndIndex == -1) {
            return "";
        }
        String packageName = className.substring(0, packageEndIndex);
        return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }
}
