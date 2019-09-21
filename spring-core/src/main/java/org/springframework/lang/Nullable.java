package org.springframework.lang;


import javafx.beans.binding.When;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;
import java.lang.annotation.*;


/**
 * 允许为空
 */
@Target({ElementType.METHOD,ElementType.PARAMETER,ElementType.FIELD})//这个注解能够添加的位置，方法的参数的字段上
@Retention(RetentionPolicy.RUNTIME) //源文件保留注解、class文件中保留注解、加载到JVM虚拟机后任然保留
@Documented //文档显示
@Nonnull //编写的源码中如果传递参数为null,.idea会弹出警告
@TypeQualifierNickname //除了这个注解 其他注解都添加到目标上
public @interface Nullable {

}
