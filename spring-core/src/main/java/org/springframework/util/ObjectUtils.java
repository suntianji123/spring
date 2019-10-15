package org.springframework.util;

import org.springframework.lang.Nullable;

import java.util.Arrays;

/**
 * 对象工具类
 */
public abstract class ObjectUtils {

    private static final int INITAL_HASH = 7;
    private static final int MULTIPLIER = 31;


    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param element 元素
     * @return
     */
    public static boolean containsElement(@Nullable Object[] array,Object element){
        if(array == null){
            return false;
        }

        for(Object obj : array){
            if(nullSafeEquals(obj,element)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取可为空的对象的哈希值
     * @param obj 对象
     * @return
     */
    public static int nullSafeHashCode(Object obj){
        if(obj == null){
            return 0;
        }

        if(obj instanceof Object[]){
            return nullSafeHashCode((Object[])obj);
        }
        if(obj instanceof boolean[]){
            return nullSafeHashCode((boolean[])obj);
        }
        if(obj instanceof byte[]){
            return nullSafeHashCode((byte[])obj);
        }
        if(obj instanceof char[]){
            return nullSafeHashCode((char[])obj);
        }
        if(obj instanceof double[]){
            return nullSafeHashCode((double[])obj);
        }
        if(obj instanceof float[]){
            return nullSafeHashCode((float[])obj);
        }
        if(obj instanceof int[]){
            return nullSafeHashCode((int[])obj);
        }
        if(obj instanceof long[]){
            return nullSafeHashCode((long[])obj);
        }
        if(obj instanceof short[]){
            return nullSafeHashCode((short[])obj);
        }
        return obj.hashCode();
    }

    /**
     * 获取可为空对象数组对象的哈希值
     * @param array 对象数组
     * @return
     */
    public static int nullSafeHashCode(@Nullable  Object[] array){
        if(array == null){
            return 0;
        }

        int hash = INITAL_HASH;
        for(Object element : array){
            hash = MULTIPLIER * hash + nullSafeHashCode(element);
        }
        return hash;
    }


    /**
     * 获取可为空布尔数组对象的哈希值
     * @param array 布尔数组
     * @return
     */
    public static int nullSafeHashCode(@Nullable  boolean[] array){
        if(array == null){
            return 0;
        }

        int hash = INITAL_HASH;
        for(boolean element : array){
            hash = MULTIPLIER * hash + Boolean.hashCode(element);
        }
        return hash;
    }


    /**
     * 获取可为空字节数组对象的哈希值
     * @param array 字节数组
     * @return
     */
    public static int nullSafeHashCode(@Nullable  byte[] array){
        if(array == null){
            return 0;
        }

        int hash = INITAL_HASH;
        for(byte element : array){
            hash = MULTIPLIER * hash + Byte.hashCode(element);
        }
        return hash;
    }

    /**
     * 获取可为空字符数组对象的哈希值
     * @param array 字符数组
     * @return
     */
    public static int nullSafeHashCode(@Nullable  char[] array){
        if(array == null){
            return 0;
        }

        int hash = INITAL_HASH;
        for(char element : array){
            hash = MULTIPLIER * hash + Character.hashCode(element);
        }
        return hash;
    }

    /**
     * 获取可为空双精度小数数组对象的哈希值
     * @param array 双精度小数数组
     * @return
     */
    public static int nullSafeHashCode(@Nullable  double[] array){
        if(array == null){
            return 0;
        }

        int hash = INITAL_HASH;
        for(double element : array){
            hash = MULTIPLIER * hash + Double.hashCode(element);
        }
        return hash;
    }

    /**
     * 获取可为空浮点数数组对象的哈希值
     * @param array 浮点数组
     * @return
     */
    public static int nullSafeHashCode(@Nullable  float[] array){
        if(array == null){
            return 0;
        }

        int hash = INITAL_HASH;
        for(float element : array){
            hash = MULTIPLIER * hash + Float.hashCode(element);
        }
        return hash;
    }

    /**
     * 获取可为空整数数数组对象的哈希值
     * @param array 整数数组
     * @return
     */
    public static int nullSafeHashCode(@Nullable  int[] array){
        if(array == null){
            return 0;
        }

        int hash = INITAL_HASH;
        for(int element : array){
            hash = MULTIPLIER * hash + Integer.hashCode(element);
        }
        return hash;
    }

    /**
     * 获取可为空长整数数组对象的哈希值
     * @param array 长整数数组
     * @return
     */
    public static int nullSafeHashCode(@Nullable  long[] array){
        if(array == null){
            return 0;
        }

        int hash = INITAL_HASH;
        for(long element : array){
            hash = MULTIPLIER * hash + Float.hashCode(element);
        }
        return hash;
    }

    /**
     * 获取可为空短整数数组对象的哈希值
     * @param array 短整数数组
     * @return
     */
    public static int nullSafeHashCode(@Nullable  short[] array){
        if(array == null){
            return 0;
        }

        int hash = INITAL_HASH;
        for(short element : array){
            hash = MULTIPLIER * hash + Short.hashCode(element);
        }
        return hash;
    }

    /**
     * 判断两个对象是否为同一对象
     * @param o1
     * @param o2
     * @return
     */
    public static boolean nullSafeEquals(@Nullable Object o1,@Nullable Object o2){
        if(o1 == o2){
            return true;
        }

        if(o1 == null || o2 == null){
            return false;
        }

        if(o1.equals(o2)){
            return true;
        }

        if(o1.getClass().isArray() && o2.getClass().isArray()){
            return arrayEquals(o1,o2);
        }
        return false;
    }

    /**
     * 判断数组相等
     * @param o1 数组1
     * @param o2 数组2
     * @return
     */
    public static boolean arrayEquals(Object o1,Object o2){
        if(o1 instanceof Object[] && o2 instanceof Object[]){
            return Arrays.equals((Object[])o1,(Object[])o2);
        }
        if(o1 instanceof boolean[] && o2 instanceof boolean[]){
            return Arrays.equals((boolean[])o1,(boolean[])o2);
        }
        if(o1 instanceof byte[] && o2 instanceof byte[]){
            return Arrays.equals((byte[])o1,(byte[])o2);
        }
        if(o1 instanceof char[] && o2 instanceof char[]){
            return Arrays.equals((char[])o1,(char[])o2);
        }
        if(o1 instanceof double[] && o2 instanceof double[]){
            return Arrays.equals((double[])o1,(double[])o2);
        }
        if(o1 instanceof float[] && o2 instanceof float[]){
            return Arrays.equals((float[])o1,(float[])o2);
        }
        if(o1 instanceof int[] && o2 instanceof int[]){
            return Arrays.equals((int[])o1,(int[])o2);
        }
        if(o1 instanceof long[] && o2 instanceof long[]){
            return Arrays.equals((long[])o1,(long[])o2);
        }
        if(o1 instanceof short[] && o2 instanceof short[]){
            return Arrays.equals((short[])o1,(short[])o2);
        }

        return false;
    }
}
