package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.Mergeable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 构造方法参数值
 */
public class ConstructorArgumentValues {

    /**
     * 指定构造函数参数值索引的集合
     */
    private final Map<Integer,ValueHolder> indexedArgumentValues = new LinkedHashMap<>();

    /**
     * 常规的构造函数参数值列表
     */
    private final List<ValueHolder> genericArgumentValues = new ArrayList<>();

    /**
     * 无参的构造函数
     */
    public ConstructorArgumentValues(){

    }

    /**
     * 通过指定构造参数值对象构造实例
     * @param original 构造参数值对象
     */
    public ConstructorArgumentValues(ConstructorArgumentValues original){
        addArgumentValues(original);
    }

    /**
     * 添加构造函数参数值
     * @param other
     */
    public void addArgumentValues(@Nullable  ConstructorArgumentValues other){
        if(other != null){
            other.indexedArgumentValues.forEach(
                    (index,argValue) -> addOrMergeIndexedArgumentValue(index,argValue.copy())
            );

            other.genericArgumentValues.stream().filter(valueHolder -> !this.genericArgumentValues.contains(valueHolder)).forEach(
                    argValue -> addOrMergeGenericArgumentValue(argValue.copy())
            );
        }
    }



    /**
     *  添加或合并带索引的构造参数值
     * @param index 索引
     * @param newValue 新的值
     */
    public void addOrMergeIndexedArgumentValue(int index,ValueHolder newValue){
        ValueHolder currValue = this.indexedArgumentValues.get(index);
        if(currValue != null && currValue.getValue() instanceof Mergeable){
            Mergeable merge = (Mergeable) (currValue.getValue());
            if(merge.isMergeableEnabled()){
                newValue.setValue(merge.merge(currValue.getValue()));
            }
        }
        this.indexedArgumentValues.put(index,newValue);
    }

    /**
     * 添加常规的构造参数值
     * @param newValue
     */
    public void addOrMergeGenericArgumentValue(ValueHolder newValue){
       if(newValue.getValue() != null){
           for(Iterator<ValueHolder> it = this.genericArgumentValues.iterator();it.hasNext();){
               ValueHolder currValue = it.next();
               if(currValue.getName().equals(newValue.getName())){
                   if(newValue.getValue() instanceof Mergeable){
                       Mergeable mergeable = (Mergeable) newValue.getValue();
                       if(mergeable.isMergeableEnabled()){
                           newValue.setValue(mergeable.merge(currValue.getValue()));
                       }
                   }
                   it.remove();
               }
           }
       }

       this.genericArgumentValues.add(newValue);

    }

    /**
     * 添加可索引的参数值
     * @param index 索引
     * @param value 值
     */
    public void addIndexedArgumentValue(int index,@Nullable Object value){
        addIndexedArgumentValue(index,new ValueHolder(value));
    }

    /**
     * 添加可索引的参数值
     * @param index 索引
     * @param newValue 值
     */
    public void addIndexedArgumentValue(int index,ValueHolder newValue){
        Assert.isTrue(index >= 0 ,"Index must be negative");
        Assert.notNull(newValue,"Value must not be null");
        addOrMergeIndexedArgumentValue(index,newValue);
    }

    /**
     * 添加可索引的参数值
     * @param index 索引位置
     * @param value 值
     * @param type 值类型
     */
    public void addIndexedArgumentValue(int index,@Nullable Object value,String type){
        addIndexedArgumentValue(index,new ValueHolder(value,type));
    }

    /**
     * 添加可索引的参数值
     * @param index 索引位置
     * @param value 值
     * @param type 值类型
     * @param name 名称
     */
    public void addIndexedArgumentValue(int index,@Nullable Object value,String type,String name){
        addIndexedArgumentValue(index,new ValueHolder(value,type,name));
    }

    /**
     * 判断是否包含某个索引参数值
     * @param index 索引位置
     * @return
     */
    public boolean hasIndexedArgumentValue(int index){
        return this.indexedArgumentValues.containsKey(index);
    }


    /**
     * 获取指定下标，参数值类型的参数值持有者
     * @param index 下标
     * @param requiredType 参数值类型
     * @return
     */
    public ValueHolder getIndexedArgumentValue(int index,Class<?> requiredType){
        return getIndexedArgumentValue(index,requiredType,null);
    }

    /**
     * 获取某个索引的参数值持有者
     * @param index 索引
     * @param requiredType 需要的类型
     * @param requiredName  参数名】
     * @return
     */
    @Nullable
    public ValueHolder getIndexedArgumentValue(int index,@Nullable  Class<?> requiredType,@Nullable  String requiredName){
        Assert.isTrue(index >=0,"Index must be negative");
        if(this.indexedArgumentValues.containsKey(index)){
            ValueHolder valueHolder = this.indexedArgumentValues.get(index);
            if(valueHolder != null && (valueHolder.getType() == null ||
                    (valueHolder.getType() != null && ClassUtils.matchesTypeName(requiredType,valueHolder.getType())))&&(
                    (valueHolder.getName() == null || "".equals(valueHolder.getName())) || (requiredName.equals(valueHolder.getName()))
                    )){
                return valueHolder;
            }
        }

        return null;
    }


    /**
     * 添加常规的参数持有者
     * @param value 值
     */
    public void addGenericArgumentValue(Object value){
        addGenericArgumentValue(new ValueHolder(value));
    }

    /**
     * 通过值和参数添加参数值
     * @param value 参数值
     * @param type 参数类型
     */
    public void addGenericArgumentValue(Object value ,String type){
        addGenericArgumentValue(new ValueHolder(value,type));
    }



    /**
     * 添加常规参数值
     * @param newValue 参数持有者
     */
    public void addGenericArgumentValue(ValueHolder newValue){
        Assert.notNull(newValue,"Value must not be null");
        if(!genericArgumentValues.contains(newValue)){
            addOrMergeGenericArgumentValue(newValue);
        }
    }

    /**
     * 获取常规参数值持有者
     * @param requiredType 参数值类型
     * @param requiredName 参数名
     * @param usedValueHolders 正在使用的参数值持有者
     * @return
     */
    @Nullable
    public ValueHolder getGenericArgumentValue(@Nullable  Class<?> requiredType,@Nullable  String requiredName,@Nullable Set<ValueHolder> usedValueHolders){
        for(ValueHolder valueHolder : this.genericArgumentValues){
            //如果正在使用
            if(usedValueHolders != null && usedValueHolders.contains(valueHolder)){
                continue;
            }

            //判断名字是否一样
            if(valueHolder.getName() != null && !"".equals(valueHolder.getName()) && (
                    requiredName == null || !valueHolder.getName().equals(requiredName))){
                continue;
            }

            if(valueHolder.getType() != null&&(
                    requiredType == null || !ClassUtils.matchesTypeName(requiredType,valueHolder.getType()))){
                continue;
            }

            if (requiredType != null && valueHolder.getType() == null && valueHolder.getName() == null &&
                    !ClassUtils.isAssignableValue(requiredType, valueHolder.getValue())) {
                continue;
            }

            return valueHolder;
        }

        return null;
    }

    /**
     * 指定参数值类型，获取参数值持有者
     * @param requiredType 参数值类型
     * @return
     */
    public ValueHolder getGenericArgumentValue(Class<?> requiredType){
        return getGenericArgumentValue(requiredType,null,null);
    }

    /**
     * 指定参数值类型，参数名获取常规参数持有者
     * @param requiredType 参数值类型
     * @param requiredName 参数名
     * @return
     */
    public ValueHolder getGenericArgumentValue(Class<?> requiredType,String requiredName){
        return getGenericArgumentValue(requiredType,requiredName,null);
    }

    /**
     * 根据索引、参数值类型获取参数持有者
     * @param index 索引
     * @param requiredType 参数值类型
     * @return
     */
    @Nullable
    public ValueHolder getArgumentValue(int index,Class<?> requiredType){
        Assert.isTrue(index >=0,"Index must be negative");
        return getArgumentValue(index,requiredType,null,null);
    }

    /**
     * 根据索引、参数值类名、参数名获取参数持有者
     * @param index 索引
     * @param requiredType 参数值类型
     * @param requiredName 参数名
     * @return
     */
    @Nullable
    public ValueHolder getArgumentValue(int index,Class<?> requiredType,String requiredName){
        Assert.isTrue(index >=0,"Index must be negative");
        return getArgumentValue(index,requiredType,requiredName,null);
    }

    /**
     * 获取参数值持有者
     * @param index 参数下标
     * @param requiredType 参数值目标类型
     * @param requiredName 参数名
     * @param usedValueHolders 正在使用的参数值持有者
     * @return
     */
    @Nullable
    public ValueHolder getArgumentValue(int index,@Nullable Class<?>  requiredType,@Nullable String requiredName,@Nullable
            Set<ValueHolder> usedValueHolders){
        Assert.isTrue(index >=0,"Index must be negative");
        ValueHolder valueHolder = getIndexedArgumentValue(index,requiredType,requiredName);
        if(valueHolder == null){
            valueHolder = getGenericArgumentValue(requiredType,requiredName,usedValueHolders);
        }
        return valueHolder;
    }

    /**
     * 清除
     */
    public void clear(){
        this.indexedArgumentValues.clear();
        this.genericArgumentValues.clear();
    }

    /**
     * 判断是否为同一个对象
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }

        if(!(other instanceof ConstructorArgumentValues)){
            return false;
        }

        ConstructorArgumentValues that = (ConstructorArgumentValues) other;
        if(this.genericArgumentValues.size() != that.genericArgumentValues.size() ||
        this.indexedArgumentValues.size() != that.indexedArgumentValues.size()){
            return false;
        }

        Iterator<ValueHolder> it1 = this.genericArgumentValues.iterator();
        Iterator<ValueHolder> it2 = this.genericArgumentValues.iterator();
        while(it1.hasNext()){
            ValueHolder vh1 = it1.next();
            ValueHolder vh2 = it2.next();
            if(!vh1.equals(vh2)){
                return false;
            }
        }

        for(Map.Entry<Integer,ValueHolder> entry : this.indexedArgumentValues.entrySet()){
            ValueHolder vh1 = entry.getValue();
            ValueHolder vh2 = that.indexedArgumentValues.get(entry.getKey());
            if(!vh1.equals(vh2)){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取哈希值
     * @return
     */
    @Override
    public int hashCode() {
        int hashCode = 7;
        for (ValueHolder valueHolder : this.genericArgumentValues) {
            hashCode = 31 * hashCode + valueHolder.contentHashCode();
        }
        hashCode = 29 * hashCode;
        for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentValues.entrySet()) {
            hashCode = 31 * hashCode + (entry.getValue().contentHashCode() ^ entry.getKey().hashCode());
        }
        return hashCode;
    }

    /**
     * 参数值持有者
     */
    public static class ValueHolder implements BeanMetadataElement {

        /**
         * 参数值
         */
        @Nullable
        private Object value;

        /**
         * 参数类型
         */
        @Nullable
        private String type;

        /**
         * 参数名称
         */
        private String name;

        /**
         * 元数据
         */
        private Object source;

        /**
         * 是否已转化
         */
        private boolean converted;

        /**
         * 转换为指定类型后的值
         */
        @Nullable
        private Object convertedValue;

        /**
         * 通过指定值构造参数值持有者
         * @param value 值
         */
        public ValueHolder(@Nullable Object value){
            this.value = value;
        }

        /**
         * 通过参数值和参数类型，构造参数值持有者
         * @param value
         * @param type
         */
        public ValueHolder(@Nullable Object value,@Nullable String type){
            this.value = value;
            this.type = type;
        }

        /**
         * 通过参数值、类型、参数名构造参数值持有者
         * @param value 值
         * @param type 类型
         * @param name 参数名
         */
        public ValueHolder(@Nullable Object value,@Nullable String type,@Nullable String name){
            this.value = value;
            this.type = type;
            this.name = name;
        }

        /**
         * 判断两个参数值持有者内容是否相等
         * @param other 另一个参数值持有者实例
         * @return
         */
        private boolean contentEquals(ValueHolder other){
            return this == other || (ObjectUtils.nullSafeEquals(this.value,other.value) && ObjectUtils.nullSafeEquals(this.type,other.type));
        }

        /**
         * 内容哈希值
         * @return
         */
        private int contentHashCode(){
            return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.type);
        }

        /**
         * 赋值
         * @return
         */
        public ValueHolder copy(){
            ValueHolder copy = new ValueHolder(this.value,this.type,this.name);
            copy.setSource(this.source);
            return copy;
        }


        /**
         * 设置value
         * @param value
         */
        public void setValue(@Nullable Object value){
            this.value = value;
        }

        /**
         * 获取value
         * @return
         */
        @Nullable
        public Object getValue(){
            return this.value;
        }

        /**
         * 设置参数类型
         * @param type
         */
        public void setType(@Nullable String type){
            this.type = type;
        }

        /**
         * 获取参数类型
         * @return
         */
        @Nullable
        public String getType(){
            return this.type;
        }

        /**
         * 设置参数名称
         * @param name 名称
         */
        public void setName(String name){
            this.name = name;
        }

        /**
         * 获取参数名称
         * @return
         */
        @Nullable
        public String getName(){
            return this.name;
        }

        /**
         * 设置值是否被转换
         * @param converted 是否被转换
         */
        public void setConverted(boolean converted){
            this.converted = converted;
        }

        /**
         * 判断值是否被转换
         * @return
         */
        public boolean isConverted(){
            return this.converted;
        }

        /**
         * 设置转换后的值
         * @param convertedValue 转换后的值
         */
        public void setConvertedValue(@Nullable Object convertedValue){
            this.convertedValue = convertedValue;
        }

        /**
         * 获取转换后的值
         * @return
         */
        public Object getConvertedValue(){
            return this.convertedValue;
        }


        public void setSource(@Nullable Object source){
            this.source = source;
        }

        @Override
        @Nullable
        public Object getSource(){
            return this.source;
        }
    }
}
