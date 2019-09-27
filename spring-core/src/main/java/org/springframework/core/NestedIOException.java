package org.springframework.core;

import java.io.IOException;

/**
 * 防止IOException信息直接暴露，嵌套一层Exception
 */
public class NestedIOException extends IOException {

    static {
        //防止类加载出现死锁调用getMessage方法出错，提前加载NestedExceptionUtils
        NestedExceptionUtils.class.getName();
    }

    public NestedIOException(String message){
        super(message);
    }

    public NestedIOException(String message,Throwable exception){
        super(message,exception);
    }

    /**
     * 获取描述信息
     * @return
     */
    public String getMessage(){
        return NestedExceptionUtils.buildMessage(super.getMessage(),super.getCause());
    }
}
