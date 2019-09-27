package org.springframework.core.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
     * 记录一条致命级别的日志记录
     * @param message 日志记录内容
     */
    public void fatal(Object message){
        this.log.fatal(message);
    }

    /**
     * 记录一条致命级别的日志记录,包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    public void fatal(Object message,Throwable t){
        this.log.fatal(message,t);
    }

    /**
     * 记录一条错误级别的日志记录
     * @param message 日志记录内容
     */
    public void error(Object message){
        this.log.error(message);
    }

    /**
     * 记录一条错误级别的日志记录，包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    public void error(Object message,Throwable t){
        this.log.error(message,t);
    }

    /**
     * 记录一条警告级别的日志记录
     * @param message 日志记录内容
     */
    public void warn(Object message){
        this.log.warn(message);
    }

    /**
     * 记录一条警告级别的日志记录，包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    public void warn(Object message,Throwable t){
        this.log.warn(message,t);
    }

    /**
     * 记录一条消息级别的日志记录
     * @param message 日志记录内容
     */
    public void info(Object message){
        this.log.info(message);
    }

    /**
     * 记录一条消息级别的日志记录，包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    public void into(Object message,Throwable t){
        this.log.info(message,t);
    }

    /**
     * 记录一条调试级别的日志记录
     * @param message 日志记录内容
     */
    public void debug(Object message){
        this.log.debug(message);
    }


    /**
     * 记录一条调试级别的日志记录，包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    public void debug(Object message, Throwable t){
        this.log.debug(message,t);
    }

    /**
     * 记录一条控制台级别的日志记录
     * @param message 日志记录内容
     */
    public void trace(Object message){
        this.log.trace(message);
    }

    /**
     * 记录一条控制台级别的日志记录，包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    public void trace(Object message,Throwable t){
        this.log.trace(message,t);
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
