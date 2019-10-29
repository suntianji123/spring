package org.springframework.util;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * 类操作工具
 */
public abstract class ClassUtils {

    private static final char PACKAGE_SEPARATOR = '.';

    private static final char PATH_SEPARATOR = '/';

    /**
     * 包装类型和基础类型对应的集合
     */
    private static final Map<Class<?>,Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>();

    static{
        primitiveTypeToWrapperMap.put(Boolean.class,boolean.class);
        primitiveTypeToWrapperMap.put(Integer.class,int.class);
        primitiveTypeToWrapperMap.put(Double.class,double.class);
        primitiveTypeToWrapperMap.put(Short.class,short.class);
        primitiveTypeToWrapperMap.put(Long.class,long.class);
        primitiveTypeToWrapperMap.put(Byte.class,byte.class);
        primitiveTypeToWrapperMap.put(Void.class,void.class);
        primitiveTypeToWrapperMap.put(Float.class,float.class);
    }

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

    /**
     * 判断某个某个class对象的类名是否为给定的类名
     * @param clzz class对象
     * @param typeName 类名
     * @return
     */
    public static boolean matchesTypeName(Class<?> clzz,String typeName){
        return typeName != null && (typeName.equals(clzz.getTypeName()) || typeName.equals(clzz.getSimpleName()));
    }

    /**
     * 判断指定的值的类型是否为某一类型
     * @param type 类型
     * @param value 值
     * @return
     */
    public static boolean isAssignableValue(Class<?> type,@Nullable Object value){
        Assert.notNull(type,"Type must not be null");
        return type != null?isAssignableValue(type,value.getClass()):!type.isPrimitive();
    }

    /**
     * 判断右边的类型是否为左边的类型
     * @param lhsType 左边类型
     * @param rhsType 右边类型
     * @return
     */
    public static boolean isAssignableValue(Class<?> lhsType,Class<?> rhsType){
        Assert.notNull(lhsType,"Left hand side type must not be null");
        Assert.notNull(rhsType,"Right side type must not be null");

        if(lhsType.isAssignableFrom(rhsType)){
            //如果左边类型是右边类型的父类
            return true;
        }

        if(lhsType.isPrimitive()){
            //如果左边为基础类型
            Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);
            if(lhsType == resolvedWrapper){
                return true;
            }
        }else{

            //如果左边不是基本类型
            Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(lhsType);
            if(resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper)){
                return true;
            }
        }

        return false;

    }
}
