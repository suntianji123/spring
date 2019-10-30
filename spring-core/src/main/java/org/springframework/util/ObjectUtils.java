package org.springframework.util;

import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * 对象工具类
 */
public abstract class ObjectUtils {

    private static final int INITAL_HASH = 7;
    private static final int MULTIPLIER = 31;

    /**
     * null字符串
     */
    private static final String NULL_STRING = "null";

    /**
     * 数组字符串起始字符
     */
    private static final String ARRAY_START = "{";

    /**
     * 数组字符串结束字符
     */
    private static final String ARRAY_END = "}";

    /**
     * 数组字符串分隔符
     */
    private static final String ARRAY_SEPERATOR = ",";

    /**
     * 空数组字符串
     */
    private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;

    /**
     * 空字符串
     */
    private static final String EMPTY_STRING = "";

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

    /**
     * 判断数组为空
     * @param arr 数组
     * @return
     */
    public static boolean isEmpty(Object[] arr){
        return arr == null || arr.length == 0;
    }

    /**
     * 将对象转为字符串
     * @param obj
     */
    public static String nullSafeToString(@Nullable Object obj){
        if(obj == null){
            return NULL_STRING;
        }

        if(obj instanceof Object){
            return (String)obj;
        }

        if(obj instanceof Object[]){
            return nullSafeToString((Object[]) obj);
        }

        if(obj instanceof boolean[]){
            return nullSafeToString((boolean[]) obj);
        }

        if(obj instanceof byte[]){
            return nullSafeToString((byte[]) obj);
        }

        if(obj instanceof char[]){
            return nullSafeToString((char[]) obj);
        }

        if(obj instanceof int[]){
            return nullSafeToString((int[]) obj);
        }

        if(obj instanceof double[]){
            return nullSafeToString((double[]) obj);
        }


        if(obj instanceof float[]){
            return nullSafeToString((float[]) obj);
        }


        if(obj instanceof long[]){
            return nullSafeToString((long[]) obj);
        }

        if(obj instanceof short[]){
            return nullSafeToString((short[]) obj);
        }

        String str = obj.toString();
        return str != null?str:EMPTY_STRING;
    }

    /**
     * 将object数组转为指定格式的数组
     * @param arr 数组对象
     * @return
     */
    public static String nullSafeToString(@Nullable Object[] arr){
        if(arr == null){
            return NULL_STRING;
        }


        if(arr.length == 0){
            return EMPTY_ARRAY;
        }

        StringJoiner sj = new StringJoiner(ARRAY_SEPERATOR,ARRAY_START,ARRAY_END);
        for(Object obj : arr){
            sj.add(String.valueOf(obj));
        }
        return sj.toString();
    }

    /**
     * 将布尔数组转为指定格式的数组
     * @param arr 布尔数组对象
     * @return
     */
    public static String nullSafeToString(@Nullable boolean[] arr){
        if(arr == null){
            return NULL_STRING;
        }

        if(arr.length == 0){
            return EMPTY_ARRAY;
        }

        StringJoiner sj = new StringJoiner(ARRAY_SEPERATOR,ARRAY_START,ARRAY_END);
        for(boolean obj : arr){
            sj.add(String.valueOf(obj));
        }

        return sj.toString();
    }

    /**
     * 将字节数组转为指定格式的数组
     * @param arr 字节数组对象
     * @return
     */
    public static String nullSafeToString(@Nullable byte[] arr){
        if(arr == null){
            return NULL_STRING;
        }

        if(arr.length == 0){
            return EMPTY_ARRAY;
        }

        StringJoiner sj = new StringJoiner(ARRAY_SEPERATOR,ARRAY_START,ARRAY_END);
        for(byte obj : arr){
            sj.add(String.valueOf(obj));
        }

        return sj.toString();
    }

    /**
     * 将整数数组转为指定格式的数组
     * @param arr 整数数组对象
     * @return
     */
    public static String nullSafeToString(@Nullable int[] arr){
        if(arr == null){
            return NULL_STRING;
        }

        if(arr.length == 0){
            return EMPTY_ARRAY;
        }

        StringJoiner sj = new StringJoiner(ARRAY_SEPERATOR,ARRAY_START,ARRAY_END);
        for(int obj : arr){
            sj.add(String.valueOf(obj));
        }

        return sj.toString();
    }


    /**
     * 将短整数数组转为指定格式的数组
     * @param arr 短整数数组对象
     * @return
     */
    public static String nullSafeToString(@Nullable short[] arr){
        if(arr == null){
            return NULL_STRING;
        }

        if(arr.length == 0){
            return EMPTY_ARRAY;
        }

        StringJoiner sj = new StringJoiner(ARRAY_SEPERATOR,ARRAY_START,ARRAY_END);
        for(short obj : arr){
            sj.add(String.valueOf(obj));
        }

        return sj.toString();
    }

    /**
     * 将长整数数组转为指定格式的数组
     * @param arr 长整数数组对象
     * @return
     */
    public static String nullSafeToString(@Nullable long[] arr){
        if(arr == null){
            return NULL_STRING;
        }

        if(arr.length == 0){
            return EMPTY_ARRAY;
        }

        StringJoiner sj = new StringJoiner(ARRAY_SEPERATOR,ARRAY_START,ARRAY_END);
        for(long obj : arr){
            sj.add(String.valueOf(obj));
        }

        return sj.toString();
    }

    /**
     * 将双精度小数组转为指定格式的数组
     * @param arr 双精度小数数组对象
     * @return
     */
    public static String nullSafeToString(@Nullable double[] arr){
        if(arr == null){
            return NULL_STRING;
        }

        if(arr.length == 0){
            return EMPTY_ARRAY;
        }

        StringJoiner sj = new StringJoiner(ARRAY_SEPERATOR,ARRAY_START,ARRAY_END);
        for(double obj : arr){
            sj.add(String.valueOf(obj));
        }

        return sj.toString();
    }

    /**
     * 将小数组转为指定格式的数组
     * @param arr 小数数组对象
     * @return
     */
    public static String nullSafeToString(@Nullable float[] arr){
        if(arr == null){
            return NULL_STRING;
        }

        if(arr.length == 0){
            return EMPTY_ARRAY;
        }

        StringJoiner sj = new StringJoiner(ARRAY_SEPERATOR,ARRAY_START,ARRAY_END);
        for(float obj : arr){
            sj.add(String.valueOf(obj));
        }

        return sj.toString();
    }

    /**
     * 将字符数组转为指定格式的数组
     * @param arr 字符数组对象
     * @return
     */
    public static String nullSafeToString(@Nullable char[] arr){
        if(arr == null){
            return NULL_STRING;
        }

        if(arr.length == 0){
            return EMPTY_ARRAY;
        }

        StringJoiner sj = new StringJoiner(ARRAY_SEPERATOR,ARRAY_START,ARRAY_END);
        for(char obj : arr){
            sj.add(String.valueOf(obj));
        }

        return sj.toString();
    }


}
