package org.springframework.core;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法参数
 */
public class MethodParameter {

    /**
     * 参数无注解列表
     */
    private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

    /**
     * 执行对象 Method  or Constructor
     */
    private final Executable executable;

    /**
     * 参数索引
     */
    private final int parameterIndex;

    /**
     * 参数
     */
    @Nullable
    private volatile Parameter parameter;

    /**
     * 嵌套层级别
     */
    private int nestingLevel;

    /**
     * 嵌套层参数级别对应的类型下标集合
     */
    @Nullable
    Map<Integer,Integer> typeIndexesPerLevel;

    /**
     * 方法所属的Class对象
     */
    @Nullable
    private Class<?> containingClass;

    /**
     * 参数class类型
     */
    @Nullable
    private Class<?> parameterType;

    /**
     * Type类型
     */
    @Nullable
    private Type genericParameterType;

    /**
     * 参数注解
     */
    @Nullable
    private volatile  Annotation[] parameterAnnotations;

    /**
     * 参数名
     */
    @Nullable
    private String parameterName;

    /**
     * 参数名发现者
     */
    @Nullable
    private ParameterNameDiscoverer parameterNameDiscoverer;

    /**
     * 嵌套层参数
     */
    @Nullable
    private MethodParameter nestedMethodParameter;

    /**
     * 通过方法、参数索引构造实例
     * @param method 方法
     * @param parameterIndex 参数索引
     */
    public MethodParameter(Method method,int parameterIndex){
        this(method,parameterIndex,1);
    }

    /**
     * 通过方法、参数索引、嵌套层级别构造实例
     * @param method 方法实例
     * @param parameterIndex 参数索引
     * @param nestingLevel 嵌套层级别
     */
    public MethodParameter(Method method,int parameterIndex,int nestingLevel){
        Assert.notNull(method,"Method must not be null");
        this.executable = method;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
    }

    /**
     * 通过构造函数、参数索引构造实例
     * @param constructor 构造函数
     * @param parameterIndex 参数索引
     */
    public MethodParameter(Constructor<?> constructor,int parameterIndex){
        this(constructor,parameterIndex,1);
    }

    /**
     * 通过构造函数、参数索引、嵌套层级别构造实例
     * @param constructor 构造函数
     * @param parameterIndex 参数索引
     * @param nestedLevel 嵌套层级别
     */
    public MethodParameter(Constructor<?> constructor,int parameterIndex,int nestedLevel){
        Assert.notNull(constructor,"Constructor must not be null");
        this.executable = constructor;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestedLevel;
    }

    /**
     * 通过执行对象、参数索引、参数所属对象构造实例
     * @param executable 可执行对象
     * @param parameterIndex 参数索引
     * @param containingClass 参数所属对象
     */
    public MethodParameter(Executable executable,int parameterIndex,Class<?> containingClass){
        Assert.notNull(executable,"Executable must not be null");
        this.executable = executable;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = 1;
        this.containingClass = containingClass;
    }

    /**
     * 基于原始的对象构造实例
     * @param original 原始对象
     */
    public MethodParameter(MethodParameter original){
        Assert.notNull(original,"Original MethodParameter must not be null");
        this.executable = original.executable;
        this.parameterIndex = original.parameterIndex;
        this.parameter = original.parameter;
        this.nestingLevel = original.nestingLevel;
        this.typeIndexesPerLevel = original.typeIndexesPerLevel;
        this.containingClass = original.containingClass;
        this.parameterType = original.parameterType;
        this.genericParameterType = original.genericParameterType;
        this.parameterAnnotations = original.parameterAnnotations;
        this.parameterNameDiscoverer = original.parameterNameDiscoverer;
        this.parameterName = original.parameterName;
    }

    /**
     * 获取方法
     * @return
     */
    @Nullable
    public Method getMethod(){
       return this.executable instanceof Method?(Method)this.executable:null;
    }

    /**
     * 增加嵌套层级别
     */
    @Deprecated
    public void increaseNestingLevel(){
        this.nestingLevel ++;
    }

    /**
     * 减少嵌套层级别
     */
    public void decreaseNestingLevel(){
        this.nestingLevel --;
    }

    /**
     * 根据类型索引创建一个实例
     * @param typeIndex 类型索引
     * @return
     */
    public MethodParameter withTypeIndex(int typeIndex){
        return nested(this.nestingLevel,typeIndex);
    }


    /**
     * 获取指定嵌套层级别的类型索引
     * @param nestingLevel 嵌套层级别
     * @return
     */
    @Nullable
    public Integer getTypeIndexForLevel(int nestingLevel){
        return getTypeIndexesPerLevel().get(nestingLevel);
    }

    /**
     * 获取某个嵌套层的实例
     * @return
     */
    public MethodParameter nested(){
        return nested(null);
    }

    /**
     * 指定类型索引，获取实例
     * @param typeIndex 类型索引
     * @return
     */
    public MethodParameter nested(@Nullable Integer typeIndex){
        MethodParameter nestedParam = this.nestedMethodParameter;
        if(nestedParam != null && typeIndex == null){
            return nestedParam;
        }

        nestedParam = nested(this.nestingLevel + 1,typeIndex);
        if(typeIndex == null){
            this.nestedMethodParameter = nestedParam;
        }
        return nestedParam;
    }

    /**
     * 设置当前当前的嵌套层级别和类型索引
     * @param typeIndex
     */
    public void setTypeIndexForCurrentLevel(int typeIndex){
        getTypeIndexesPerLevel().put(this.nestingLevel,typeIndex);
    }

    /**
     * 键入当前对象的嵌套层和类型的对应关系
     * @param nestingLevel 嵌套层级别
     * @param typeIndex 类型索引
     * @return
     */
    private MethodParameter nested(int nestingLevel,@Nullable Integer typeIndex){
        MethodParameter copy = clone();
        copy.nestingLevel = nestingLevel;
        if(this.typeIndexesPerLevel != null){
            copy.typeIndexesPerLevel = new HashMap<>(this.typeIndexesPerLevel);
        }

        if(typeIndex != null){
            copy.getTypeIndexesPerLevel().put(nestingLevel,typeIndex);
        }
        copy.parameterType = null;
        copy.genericParameterType = null;
        return copy;
    }

    /**
     * 获取级别和类型对应关系
     * @return
     */
    public Map<Integer,Integer> getTypeIndexesPerLevel(){
        if(this.typeIndexesPerLevel == null){
            this.typeIndexesPerLevel = new HashMap<>(4);
        }
        return this.typeIndexesPerLevel;
    }

    /**
     * 复制一个对象
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public MethodParameter clone() {
        return new MethodParameter(this);
    }

    /**
     * 是否有Nullable注解
     * @return
     */
    private boolean hasNullableAnnotation(){
        for(Annotation anno : getParameterAnnotations()){
            if("Nullable".equals(anno.annotationType().getSimpleName()){
                return true;
            }
        }
        return false;
    }

    public MethodParameter nestedIfOptional(){
        return
    }

    /**
     * 获取参数注解
     * @return
     */
    public Annotation[] getParameterAnnotations(){
        Annotation[] paramAnns = this.parameterAnnotations;
        if(paramAnns == null){
            //获取executable所有的注解
            Annotation[][] annotationArray = this.executable.getParameterAnnotations();
            int index = this.parameterIndex;
            if(this.executable instanceof Constructor &&
                    ClassUtils.isInnerClass(this.executable.getDeclaringClass()) && annotationArray.length == this.executable.getParameterCount() - 1){
                index = this.parameterIndex - 1;
            }

            paramAnns = (index >=0 && index < annotationArray.length ? annotationArray[index]:EMPTY_ANNOTATION_ARRAY);
            this.parameterAnnotations = paramAnns;
        }
        return paramAnns;
    }
}
