package org.springframework.core;

import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

final class SerializableTypeWrapper {

    /**
     * 类型提供者
     */
    interface TypeProvider extends Serializable {

        /**
         * 获取Type对象
         * @return
         */
        @Nullable
        Type getType();

        /**
         * 获取类型所属对象 ：Field Method Class
         * @return
         */
        @Nullable
        default Object getSource(){
            return null;
        }


    }

    /**
     * 字段类型提供者
     */
    static class FieldTypeProvider implements TypeProvider{
        /**
         * 字段名
         */
        private final String fieldName;

        /**
         *  字段所属Class对象
         */
        private final Class<?> declaringClass;

        /**
         * 字段对象
         */
        private transient Field field;

        /**
         * 通过指定字段对象构造字段类型提供者
         * @param field
         */
        public FieldTypeProvider(Field field){
            this.fieldName = field.getName();
            this.declaringClass = field.getDeclaringClass();
            this.field = field;
        }

        /**
         * 获取类型
         * @return
         */
        @Override
        public Type getType() {
            return this.field.getGenericType();
        }

        /**
         * 获取类型所属对象
         * @return
         */
        @Override
        public Object getSource() {
            return this.field;
        }

        /**
         * 读取输入流对象
         * @param objectInputStream 输入流对象
         * @throws IOException
         * @throws ClassNotFoundException
         */
        private void readObject(ObjectInputStream objectInputStream) throws IOException,ClassNotFoundException {
            //读取对象
            objectInputStream.defaultReadObject();
            try{
                this.field = this.declaringClass.getField(this.fieldName);
            }catch (Throwable ex){
                throw new IllegalStateException("Can not find original class structure ",ex);
            }
        }
    }


}


