package org.springframework.core.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.function.Supplier;

/**
 * 日志访问类
 */
public class LogAccessor {

    /**
     * 日志实例
     */
    private final Log log;

    /**
     * 构造日志访问实例
     * @param log 日志对象
     */
    public LogAccessor(Log log){
        this.log = log;
    }

    /**
     * 构造日志访问实例
     * @param logCategory 被记录日志的类
     */
    public LogAccessor(Class<?> logCategory){
        this.log = LogFactory.getLog(logCategory);
    }

    /**
     * 构造日志访问实例
     * @param logCategory 被记录日志的类名
     */
    public LogAccessor(String logCategory){
        this.log = LogFactory.getLog(logCategory);
    }


    /**
     * 记录致命级别的日志
     * @param message 消息序列
     */
    public void fatal(CharSequence message){
        this.log.fatal(message);
    }

    /**
     * 记录致命级别的日志
     * @param exception 异常
     * @param message 消息序列
     */
    public void fatal(Throwable exception,CharSequence message){
        this.log.fatal(message,exception);
    }

    /**
     * 记录致命级别的日志
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void fatal(Supplier<? extends CharSequence> supplier){
        this.log.fatal(LogMessage.of(supplier));
    }

    /**
     * 记录致命级别的日志
     * @param exception 异常
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void fatal(Throwable exception,Supplier<? extends CharSequence> supplier){
        this.log.fatal(LogMessage.of(supplier),exception);
    }

    /**
     * 记录错误级别的日志
     * @param message 消息序列
     */
    public void error(CharSequence message){
        this.log.error(message);
    }

    /**
     * 记录错误级别的日志
     * @param exception 异常
     * @param message 消息序列
     */
    public void error(Throwable exception,CharSequence message){
        this.log.error(message,exception);
    }

    /**
     * 记录错误级别的日志
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void error(Supplier<? extends CharSequence> supplier){
        this.log.error(LogMessage.of(supplier));
    }

    /**
     * 记录错误级别的日志
     * @param exception 异常
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void error(Throwable exception,Supplier<? extends CharSequence> supplier){
        this.log.error(LogMessage.of(supplier),exception);
    }

    /**
     * 记录警告级别的日志
     * @param message 消息序列
     */
    public void warn(CharSequence message){
        this.log.warn(message);
    }

    /**
     * 记录警告级别的日志
     * @param exception 异常
     * @param message 消息序列
     */
    public void warn(Throwable exception,CharSequence message){
        this.log.warn(message,exception);
    }

    /**
     * 记录警告级别的日志
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void warn(Supplier<? extends CharSequence> supplier){
        this.log.warn(LogMessage.of(supplier));
    }

    /**
     * 记录警告级别的日志
     * @param exception 异常
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void warn(Throwable exception,Supplier<? extends CharSequence> supplier){
        this.log.warn(LogMessage.of(supplier),exception);
    }

    /**
     * 记录信息级别的日志
     * @param message 消息序列
     */
    public void info(CharSequence message){
        this.log.info(message);
    }

    /**
     * 记录信息级别的日志
     * @param exception 异常
     * @param message 消息序列
     */
    public void info(Throwable exception,CharSequence message){
        this.log.info(message,exception);
    }

    /**
     * 记录信息级别的日志
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void info(Supplier<? extends CharSequence> supplier){
        this.log.info(LogMessage.of(supplier));
    }

    /**
     * 记录信息级别的日志
     * @param exception 异常
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void info(Throwable exception,Supplier<? extends CharSequence> supplier){
        this.log.info(LogMessage.of(supplier),exception);
    }

    /**
     * 记录调试级别的日志
     * @param message 消息序列
     */
    public void debug(CharSequence message){
        this.log.debug(message);
    }

    /**
     * 记录调试级别的日志
     * @param exception 异常
     * @param message 消息序列
     */
    public void debug(Throwable exception,CharSequence message){
        this.log.debug(message,exception);
    }

    /**
     * 记录调试级别的日志
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void debug(Supplier<? extends CharSequence> supplier){
        this.log.debug(LogMessage.of(supplier));
    }

    /**
     * 记录调试级别的日志
     * @param exception 异常
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void debug(Throwable exception,Supplier<? extends CharSequence> supplier){
        this.log.debug(LogMessage.of(supplier),exception);
    }

    /**
     * 记录控制台级别的日志
     * @param message 消息序列
     */
    public void trace(CharSequence message){
        this.log.trace(message);
    }

    /**
     * 记录控制台级别的日志
     * @param exception 异常
     * @param message 消息序列
     */
    public void trace(Throwable exception,CharSequence message){
        this.log.trace(message,exception);
    }

    /**
     * 记录控制台级别的日志
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void trace(Supplier<? extends CharSequence> supplier){
        this.log.trace(LogMessage.of(supplier));
    }

    /**
     * 记录控制台级别的日志
     * @param exception 异常
     * @param supplier 实现Supplier接口，并继承CharSeque的实例的日志信息
     */
    public void trace(Throwable exception,Supplier<? extends CharSequence> supplier){
        this.log.trace(LogMessage.of(supplier),exception);
    }

    /**
     * 是否可以记录致命级别的日志记录
     * @return
     */
    public boolean isFatalEnabled(){
        return this.log.isFatalEnabled();
    }

    /**
     * 是否可以记录错误级别的日志记录
     * @return
     */
    public boolean isErrorEnabled(){
        return this.log.isErrorEnabled();
    }

    /**
     * 是否可以记录警告级别的日志记录
     * @return
     */
    public boolean isWarnEnabled(){
        return this.log.isWarnEnabled();
    }

    /**
     * 是否可以记录信息级别的日志记录
     * @return
     */
    public boolean isInfoEnabled(){
        return this.log.isInfoEnabled();
    }

    /**
     * 是否可以记录调试级别的日志记录
     * @return
     */
    public boolean isDebugEnabled(){
        return this.log.isDebugEnabled();
    }

    /**
     * 是否可以记录控制台级别的日志记录
     * @return
     */
    public boolean isTraceEnabled(){
        return this.log.isTraceEnabled();
    }

}
