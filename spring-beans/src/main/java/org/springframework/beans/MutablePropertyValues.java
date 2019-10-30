package org.springframework.beans;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;

/**
 * 可变的属性信息列表
 */
public class MutablePropertyValues implements PropertyValues {

    /**
     * 属性信息列表
     */
    private final List<PropertyValue> propertyValueList;

    /**
     * 在使用中的属性名
     */
    private Set<String> processedProperties;

    /**
     * 是否被转换
     */
    private volatile boolean converted;

    /**
     * 无参沟槽属性信息列表
     */
    public MutablePropertyValues(){
        this.propertyValueList =  new ArrayList<>(0);
    }

    /**
     * 基于其他属性信息列表，复制一份
     * @param original
     */
    public MutablePropertyValues(@Nullable PropertyValues original){
        if(original != null){
            PropertyValue[] pvs = original.getPropetyValues();
            this.propertyValueList = new ArrayList<>(pvs.length);
            for(PropertyValue propertyValue : pvs){
                this.propertyValueList.add(new PropertyValue(propertyValue));
            }
        }else{
            this.propertyValueList = new ArrayList<>(0);
        }
    }

    /**
     * 基于集合构造属性信息列表
     * @param original
     */
    public MutablePropertyValues(@Nullable Map<?,?> original){
        if(original != null){
            this.propertyValueList = new ArrayList<>(original.size());
            original.forEach((attributeName,attributeValue) -> this.propertyValueList.add(new PropertyValue(attributeName.toString(),attributeValue)));
        }else{
            this.propertyValueList = new ArrayList<>(0);
        }
    }

    /**
     * 基于属性信息列表构建属性信息列表
     * @param propertyValueList 属性信息列表
     */
    public MutablePropertyValues(@Nullable List<PropertyValue> propertyValueList){
        this.propertyValueList = propertyValueList != null?propertyValueList : new ArrayList<>(0);
    }

    /**
     * 获取大小
     * @return
     */
    public int size(){
        return this.propertyValueList.size();
    }

    /**
     * 加上另一个属性信息列表
     * @param other 另一个属性列表
     * @return
     */
    public MutablePropertyValues addPropertyValues(@Nullable  PropertyValues other){
        if(other != null){
            PropertyValue[] propertyValues = other.getPropetyValues();
            for(PropertyValue propertyValue : propertyValues){
                this.propertyValueList.add(new PropertyValue(propertyValue));
            }
        }
        return this;
    }

    /**
     * 加上另一个属性信息列表
     * @param other 集合
     * @return
     */
    public MutablePropertyValues addPropertyValues(@Nullable Map<?,?> other){
        if(other != null){
            other.forEach((attributeName,attributeValue) -> this.propertyValueList.add(new PropertyValue(attributeName.toString(),attributeValue)));
        }
        return this;
    }

    /**
     * 添加一个PropertyValue
     * @param pv
     * @return
     */
    public MutablePropertyValues addPropertyValue(PropertyValue pv){
        for(int i=0;i<this.propertyValueList.size();i++){
            PropertyValue currentPv = this.propertyValueList.get(i);
            if(currentPv.getName().equals(pv.getName())){
                //判断是否可以合并
                pv = mergeIfRequired(currentPv,pv);
                setPropertyValueAt(pv,i);
                return this;
            }
        }

        this.propertyValueList.add(pv);
        return this;
    }

    public PropertyValue mergeIfRequired(PropertyValue currPv,PropertyValue newPv){
        Object value = newPv.getValue();
        if(value instanceof Mergeable){
            Mergeable mergeable = (Mergeable) value;
            if(mergeable.isMergeableEnabled()){
                return new PropertyValue(newPv.getName(),mergeable.merge(currPv.getValue()));
            }
        }

        return newPv;
    }

    /**
     * 设置指定下标处的PropertyValue
     * @param pv 属性信息
     * @param index 指定下标
     */
    public void setPropertyValueAt(PropertyValue pv,int index){
        this.propertyValueList.set(index,pv);
    }

    /**
     * 添加属性信息
     * @param propertyName 属性名
     * @param propertyValue 属性值
     */
    public void addPropertyValue(String propertyName,@Nullable Object propertyValue){
        addPropertyValue(new PropertyValue(propertyName,propertyValue));
    }

    /**
     * 添加属性信息
     * @param propertyName 属性名
     * @param propertyValue 属性值
     * @return
     */
    public MutablePropertyValues add(String propertyName,@Nullable Object propertyValue){
        addPropertyValue(new PropertyValue(propertyName,propertyValue));
        return this;
    }

    /**
     * 删除属性信息
     * @param pv 属性信息
     */
    public void removePropertyValue(PropertyValue pv){
        this.propertyValueList.remove(pv);
    }

    /**
     * 根据属性名删除属性
     * @param propertyName 属性名
     */
    public void removePropertyValue(String propertyName){
        this.propertyValueList.remove(getPropertyValue(propertyName));
    }

    /**
     * 获取顺序的迭代器
     * @return
     */
    @Override
    public Iterator<PropertyValue> iterator() {
        return Collections.unmodifiableList(this.propertyValueList).iterator();
    }

    /**
     * 获取并行的迭代器
     * @return
     */
    @Override
    public Spliterator<PropertyValue> spliterator() {
        return Spliterators.spliterator(this.propertyValueList,0);
    }

    /**
     * 获取流对象
     * @return
     */
    @Override
    public Stream<PropertyValue> stream() {
        return this.propertyValueList.stream();
    }

    /**
     * 根据属性名获取属性信息
     * @param name 属性名
     * @return
     */
    @Override
    public PropertyValue getPropertyValue(String name) {
        for(PropertyValue pv : this.propertyValueList){
            if(pv.getName().equals(name)){
                return pv;
            }
        }
        return null;
    }

    /**
     * 获取属性值
     * @param propertyName 属性名
     * @return
     */
    @Nullable
    public Object get(String propertyName){
        PropertyValue pv = getPropertyValue(propertyName);
        return pv != null ? pv.getValue() : null;
    }

    /**
     * 获取改变的属性列表
     * @param old 旧的PropertyValues
     * @return
     */
    @Override
    public PropertyValues changesSince(PropertyValues old) {
        MutablePropertyValues changes = new MutablePropertyValues();
        if(this == old){
            return changes;
        }

        for(PropertyValue currPv : this.propertyValueList){
            PropertyValue oldPv = old.getPropertyValue(currPv.getName());
            if(oldPv == null || !currPv.equals(oldPv)){
                changes.addPropertyValue(currPv);
            }
        }
        return changes;
    }

    /**
     * 是否包含某个属性
     * @param propertyName 属性名
     * @return
     */
    @Override
    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName) != null || (processedProperties != null && processedProperties.contains(propertyName));
    }

    /**
     * 注册正在使用的属性名
     * @param propertyName 属性名
     */
    public void registerProcessedProperties(String propertyName){
        if(this.processedProperties == null){
            this.processedProperties = new HashSet<>(4);
        }
        this.processedProperties.add(propertyName);
    }

    /**
     * 删除正在使用的属性
     * @param propertyName 属性名
     */
    public void removeProcessedProperties(String propertyName){
        if(this.processedProperties != null){
            this.processedProperties.remove(propertyName);
        }
    }

    /**
     * 设置转换完成
     */
    public void setConverted(){
        this.converted = true;
    }

    /**
     * 判断两个对象相等
     * @param other 另一个对象
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof MutablePropertyValues
                && this.propertyValueList.equals(((MutablePropertyValues) other).propertyValueList));
    }

    /**
     * 获取哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return this.propertyValueList.hashCode();
    }

    @Override
    public String toString() {
        PropertyValue[] propertyValues = getPropetyValues();
        if(propertyValues.length > 0){
            return "PropertyValues  : length = " + propertyValues.length +";" + StringUtils.arrayToDelimitedString(propertyValues,";");
        }else{
            return "PropertyValues  : length = 0";
        }
    }

    /**
     * 获取属性值信息列表
     * @return
     */
    public List<PropertyValue> getPropertyValueList(){
        return this.propertyValueList;
    }

    /**
     * 是否转换
     * @return
     */
    public boolean isConverted(){
        return this.converted;
    }

    /**
     * 获取属性信息数组
     * @return
     */
    @Override
    public PropertyValue[] getPropetyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    /**
     * 判断是否为空调
     * @return
     */
    @Override
    public boolean isEmpty() {
        return this.propertyValueList.isEmpty();
    }
}

