package org.springframework.core;


import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * 获取类型方法
 */
public class ResolvableType implements Serializable {


    /**
     * 变量解析器
     */
    interface VariableResolver extends Serializable{

        /**
         * 获取变量所属对象
         * @return
         */
        Object getSource();

        /**
         * 根据变量获取ResolvableType对象
         * @param variable 变量
         * @return
         */
        @Nullable
        ResolvableType resolveVariable(TypeVariable<?> variable);
    }


    /**
     * 空类型
     */
    static class EmptyType implements Type,Serializable{
        static final EmptyType INSTANCE = new EmptyType();

        Object readResolve(){
            return INSTANCE;
        }
    }

}



