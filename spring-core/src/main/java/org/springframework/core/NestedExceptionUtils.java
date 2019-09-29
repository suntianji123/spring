package org.springframework.core;

import org.springframework.lang.Nullable;

/**
 * 嵌套异常工具类
 */
public class NestedExceptionUtils {

    /**
     * 构建异常信息
     * @param message 自定义错误信息
     * @param exception 异常对象
     * @return
     */
    public static String buildMessage(@Nullable  String message,@Nullable Throwable exception){
        StringBuilder sb = new StringBuilder(64);
        if(message != null)
            sb.append(message).append(";");
        sb.append("nested exception is ").append(exception);
        return sb.toString();
    }

}
