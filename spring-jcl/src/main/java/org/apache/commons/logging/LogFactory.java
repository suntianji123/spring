package org.apache.commons.logging;

/**
 * 日志工厂类
 */
public abstract  class LogFactory {

    /**
     * 获取日志对象
     * @param name 被记录日志类的类名
     * @return
     */
    public static Log getLog(String name){
        return LogAdapter.createLog(name);
    }

    /**
     * 获取日志对象
     * @param cls 被记录日志的class
     * @return
     */
    public static Log getLog(Class<?> cls){
        return getLog(cls.getName());
    }

    @Deprecated
    public static Log getInstance(String name){
        return LogAdapter.createLog(name);
    }

    @Deprecated
    public static Log getInstance(Class<?> cls){
        return getInstance(cls.getName());
    }

    @Deprecated
    public static LogFactory getLogFactory(){
        return new LogFactory(){};
    }
}
