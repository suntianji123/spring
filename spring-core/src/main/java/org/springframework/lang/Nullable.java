package org.springframework.lang;


import javafx.beans.binding.When;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 允许为空
 */
@Target({ElementType.METHOD,ElementType.PARAMETER,ElementType.FIELD})//这个注解能够添加的位置，方法的参数的字段上
@Retention(RetentionPolicy.RUNTIME) //源文件保留注解、class文件中保留注解、加载到JVM虚拟机后任然保留
public @interface Nullable {
}
