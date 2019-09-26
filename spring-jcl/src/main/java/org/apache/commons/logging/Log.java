package org.apache.commons.logging;

/**
 * 日志接口
 */
public interface Log {

    /**
     * 记录一条致命级别的日志记录
     * @param message 日志记录内容
     */
    void fatal(Object message);

    /**
     * 记录一条致命级别的日志记录,包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    void fatal(Object message,Throwable t);

    /**
     * 记录一条错误级别的日志记录
     * @param message 日志记录内容
     */
    void error(Object message);

    /**
     * 记录一条错误级别的日志记录，包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    void error(Object message,Throwable t);

    /**
     * 记录一条警告级别的日志记录
     * @param message 日志记录内容
     */
    void warn(Object message);

    /**
     * 记录一条警告级别的日志记录，包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    void warn(Object message,Throwable t);

    /**
     * 记录一条消息级别的日志记录
     * @param message 日志记录内容
     */
    void info(Object message);

    /**
     * 记录一条消息级别的日志记录，包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    void into(Object message,Throwable t);

    /**
     * 记录一条调试级别的日志记录
     * @param message 日志记录内容
     */
    void debug(Object message);


    /**
     * 记录一条调试级别的日志记录，包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    void debug(Object message, Throwable t);

    /**
     * 记录一条控制台级别的日志记录
     * @param message 日志记录内容
     */
    void trace(Object message);

    /**
     * 记录一条控制台级别的日志记录，包括异常信息
     * @param message 日志记录内容
     * @param t 异常信息
     */
    void trace(Object message,Throwable t);

    /**
     * 是否可以记录致命级别的日志记录
     * @return
     */
    boolean isFatalEnabled();

    /**
     * 是否可以记录错误级别的日志记录
     * @return
     */
    boolean isErrorEnabled();

    /**
     * 是否可以记录警告级别的日志记录
     * @return
     */
    boolean isWarnEnabled();

    /**
     * 是否可以记录信息级别的日志记录
     * @return
     */
    boolean isInfoEnabled();

    /**
     * 是否可以记录调试级别的日志记录
     * @return
     */
    boolean isDebugEnabled();

    /**
     * 是否可以记录控制台级别的日志记录
     * @return
     */
    boolean isTraceEnabled();

}
