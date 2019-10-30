package org.springframework.beans;

import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * bean的多个属性信息列表
 */
public interface PropertyValues extends Iterable<PropertyValue> {

    /**
     * 获取顺序迭代器
     * @return
     */
    @Override
    default Iterator<PropertyValue> iterator(){
        return Arrays.asList(getPropetyValues()).iterator();
    };


    /**
     * 获取并行的迭代器
     * @return
     */
    @Override
    default Spliterator<PropertyValue> spliterator(){
        return Spliterators.spliterator(getPropetyValues(),0);
    }

    /**
     * 获取Stream流
     * @return
     */
    default Stream<PropertyValue> stream(){
        return StreamSupport.stream(spliterator(),false);
    }

    /**
     * 获取所有的属性信息
     * @return
     */
    PropertyValue[] getPropetyValues();

    /**
     * 获取单个属性信息
     * @param name 属性名
     * @return
     */
    @Nullable
    PropertyValue getPropertyValue(String name);

    /**
     * 获取改变的属性信息列表
     * @param old 旧的PropertyValues
     * @return
     */
    PropertyValues changesSince(PropertyValues old);

    /**
     * 是否包含某个属性名
     * @param propertyName
     * @return
     */
    boolean contains(String propertyName);

    /**
     * 判断是否为空
     * @return
     */
    boolean isEmpty();

}
