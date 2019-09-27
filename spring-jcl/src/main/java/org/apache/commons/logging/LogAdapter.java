package org.apache.commons.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

import java.io.Serializable;
import java.util.logging.LogRecord;

/**
 * 日志适配器
 */
public abstract class LogAdapter {

    /**
     * LOG4J框架日志类
     */
    private static final String LOG4J = "org.apache.logging.log4j.spi.ExtendedLogger";

    /**
     * 将LOG4J转成SLF4J框架使用主要类
     */
    private static final String SLF4J_PROVIDER = "org.apache.logging.slf4j.SLF4JProvider";


    /**
     * 将LOG4J转成SLF4J框架使用主要类
     */
    private static final String SLF4J_SPI = "org.slf4j.spi.LocationAwareLogger";

    /**
     * SLF4J框架日志类
     */
    private static final String SLF4J = "org.slf4j.spi.LocationAwareLogger";

    /**
     * 当前使用的日志框架
     */
    private static final LogApi logApi;

    static {
        if(isPresent(LOG4J)){
            if(isPresent(SLF4J_PROVIDER) && isPresent(SLF4J_SPI)){
                logApi = LogApi.SLF4J_LAL;
            }else{
                logApi = LogApi.LOG4J;
            }
        }else if(isPresent(SLF4J_SPI)) {
            logApi = LogApi.SLF4J_LAL;
        }else if(isPresent(SLF4J)){
            logApi = LogApi.SLF4J;
        }else{
            logApi = LogApi.JUL;
        }
    }

    /**
     * 判断是否可以加载某个类
     * @param name
     * @return
     */
    private static boolean isPresent(String name){
        try{
            //第二次参数表示，如果需要加载的类之前没有被加载，是否加载
            Class.forName(name,false,LogAdapter.class.getClassLoader());
            return false;
        }catch (ClassNotFoundException e){
            return false;
        }
    }


    /**
     * 第三方日志框架枚举值
     * LOG4J : 检测是否存在 “org.apache.logging.log4j.spi.ExtendedLogger”类
     * SLF4J_LAL : 检测是否存在 “org.apache.logging.slf4j.SLF4JProvider”
     *  “org.slf4j.spi.LocationAwareLogger” org.apache.logging.log4j.spi.ExtendedLogger”
     *  或者 “org.slf4j.spi.LocationAwareLogger”类
     *  SFL4J : 检测是否存在 "org.slf4j.Logger" 类
     *  JUL : 以上条件不满足，调用JAVA底层的日志框架
     */
    private enum  LogApi{LOG4J,SLF4J_LAL,SLF4J,JUL}

    public static Log createLog(String name){
        switch (logApi){
            case LOG4J:
               return Log4jAdapter.createLog(name);
            case SLF4J_LAL:
               return SLF4JAdapter.createLocationAwareLog(name);
            case SLF4J:
                return SLF4JAdapter.createSL4JLog(name);
            default:
                return JavaUtilLogAdapter.createLog(name);
        }
    }


    /**
     * log4j日志适配器
     */
    private static class Log4jAdapter{
        public static Log createLog(String name){
            return new Log4jLog(name);
        }
    }

    /**
     * SLF4JLocationAwareLogAdapter日志适配器
     */
    private static class SLF4JAdapter{
        /**
         * 创建SLEFJLocationAwareLog日志对象
         * @param name 被记录日志的类名
         * @return
         */
        public static Log createLocationAwareLog(String name) {
            //根据类名构建需要的SLEF4J日志对象
            Logger logger = LoggerFactory.getLogger(name);
            return logger instanceof LocationAwareLogger ? new SLF4JLocationAwareLog((LocationAwareLogger) logger) : new SLF4JLog<>(logger);
        }

        /**
         * 创建SLEFJLog日志对象
         * @param name 被记录日志的类名
         * @return
         */
        public static Log createSL4JLog(String name){
            return new SLF4JLog<>(LoggerFactory.getLogger(name));
        }
    }

    /**
     * JavaUtilLog适配器
     */
    private static  class JavaUtilLogAdapter{
        /**
         * 创建JavaUtilLog对象
         * @param name 被记录日志的类名
         * @return
         */
        public static Log createLog(String name){
            return new JavaUtilLog(name);
        }
    }


    /**
     * log4jLog日志类
     */
    private static class Log4jLog implements Log, Serializable {

        /**
         * 记录日志的公用描述，指明由哪个类实例化的对象
         */
        private static final String FECQ = Log4jLog.class.getName();


        /**
         * 读取配置文件，解析配置文件的上下文LoggerContext对象
         */
        private static final  LoggerContext loggerContext = LogManager.getContext(Log4jLog.class.getClassLoader(),false);

        /**
         * log4j日志类
         */
        private final ExtendedLogger logger;

        /**
         * 实例化日志对象
         * @param name 需要被记录日志的类名
         */
        public Log4jLog(String name){
            this.logger =  loggerContext.getLogger(name);
        }

        @Override
        public void fatal(Object message) {
            log(message,Level.FATAL,null);
        }

        @Override
        public void fatal(Object message, Throwable t) {
            log(message,Level.FATAL,t);
        }

        @Override
        public void error(Object message) {
            log(message,Level.ERROR,null);
        }

        @Override
        public void error(Object message, Throwable t) {
            log(message,Level.ERROR,t);
        }

        @Override
        public void warn(Object message) {
            log(message,Level.WARN,null);
        }

        @Override
        public void warn(Object message, Throwable t) {
            log(message,Level.WARN,t);
        }

        @Override
        public void info(Object message) {
            log(message,Level.INFO,null);
        }

        @Override
        public void into(Object message, Throwable t) {
            log(message,Level.INFO,t);
        }

        @Override
        public void debug(Object message) {
            log(message,Level.DEBUG,null);
        }

        @Override
        public void debug(Object message, Throwable t) {
            log(message,Level.DEBUG,t);
        }

        @Override
        public void trace(Object message) {
            log(message,Level.TRACE,null);
        }

        @Override
        public void trace(Object message, Throwable t) {
            log(message,Level.TRACE,t);
        }

        @Override
        public boolean isFatalEnabled() {
            return false;
        }

        @Override
        public boolean isErrorEnabled() {
            return false;
        }

        @Override
        public boolean isWarnEnabled() {
            return false;
        }

        @Override
        public boolean isInfoEnabled() {
            return false;
        }

        @Override
        public boolean isDebugEnabled() {
            return false;
        }

        @Override
        public boolean isTraceEnabled() {
            return false;
        }

        /**
         * 日志记录
         * @param message
         * @param level
         * @param exception
         */
        private void log(Object message, Level level,Throwable exception){
            if(message instanceof String){
                if(exception != null){
                    this.logger.logIfEnabled(FECQ,level,null,(String)message,exception);
                }else{
                    this.logger.logIfEnabled(FECQ,level,null,(String)message);
                }
            }else{
                this.logger.logIfEnabled(FECQ,level,null,message,exception);
            }
        }
    }

    /**
     * SLF4JLog日志类
     * @param <T>
     */
    private static class SLF4JLog<T extends Logger> implements  Log,Serializable{

        /**
         *  被记录日志的类类名
         */
        protected final String name;

        /**
         * 日志对象
         */
        protected final transient T logger;

        public SLF4JLog(T logger){
            this.name = logger.getName();
            this.logger = logger;
        }

        @Override
        public void fatal(Object message) {
            if(message instanceof  String || this.logger.isErrorEnabled()){
                this.logger.error(String.valueOf(message));
            }
        }

        @Override
        public void fatal(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isErrorEnabled()){
                this.logger.error(String.valueOf(message),t);
            }
        }

        @Override
        public void error(Object message) {
            if(message instanceof  String || this.logger.isErrorEnabled()){
                this.logger.error(String.valueOf(message));
            }
        }

        @Override
        public void error(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isErrorEnabled()){
                this.logger.error(String.valueOf(message),t);
            }
        }

        @Override
        public void warn(Object message) {
            if(message instanceof  String || this.logger.isWarnEnabled()){
                this.logger.error(String.valueOf(message));
            }
        }

        @Override
        public void warn(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isWarnEnabled()){
                this.logger.error(String.valueOf(message),t);
            }
        }

        @Override
        public void info(Object message) {
            if(message instanceof  String || this.logger.isInfoEnabled()){
                this.logger.error(String.valueOf(message));
            }
        }

        @Override
        public void into(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isInfoEnabled()){
                this.logger.error(String.valueOf(message),t);
            }
        }

        @Override
        public void debug(Object message) {
            if(message instanceof  String || this.logger.isDebugEnabled()){
                this.logger.error(String.valueOf(message));
            }
        }

        @Override
        public void debug(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isDebugEnabled()){
                this.logger.error(String.valueOf(message),t);
            }
        }

        @Override
        public void trace(Object message) {
            if(message instanceof  String || this.logger.isTraceEnabled()){
                this.logger.error(String.valueOf(message));
            }
        }

        @Override
        public void trace(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isTraceEnabled()){
                this.logger.error(String.valueOf(message),t);
            }
        }

        @Override
        public boolean isFatalEnabled() {
           return this.logger.isErrorEnabled();
        }

        @Override
        public boolean isErrorEnabled() {
            return this.logger.isErrorEnabled();
        }

        @Override
        public boolean isWarnEnabled() {
            return this.logger.isWarnEnabled();
        }

        @Override
        public boolean isInfoEnabled() {
            return this.logger.isInfoEnabled();
        }

        @Override
        public boolean isDebugEnabled() {
            return this.logger.isDebugEnabled();
        }

        @Override
        public boolean isTraceEnabled() {
            return this.logger.isTraceEnabled();
        }
    }

    /**
     * SLF4JLocationAwareLog日志
     */
    private static class SLF4JLocationAwareLog extends SLF4JLog<LocationAwareLogger> implements Serializable{

        private static final String FECQ = SLF4JLocationAwareLog.class.getName();

        public SLF4JLocationAwareLog(LocationAwareLogger logger) {
            super(logger);
        }


        @Override
        public void fatal(Object message) {
            if(message instanceof  String || this.logger.isErrorEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.ERROR_INT,String.valueOf(message),null,null);
            }
        }

        @Override
        public void fatal(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isErrorEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.ERROR_INT,String.valueOf(message),null,t);
            }
        }

        @Override
        public void error(Object message) {
            if(message instanceof  String || this.logger.isErrorEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.ERROR_INT,String.valueOf(message),null,null);
            }
        }

        @Override
        public void error(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isErrorEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.ERROR_INT,String.valueOf(message),null,t);
            }
        }

        @Override
        public void warn(Object message) {
            if(message instanceof  String || this.logger.isWarnEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.WARN_INT,String.valueOf(message),null,null);
            }
        }

        @Override
        public void warn(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isWarnEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.WARN_INT,String.valueOf(message),null,t);
            }
        }

        @Override
        public void info(Object message) {
            if(message instanceof  String || this.logger.isInfoEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.INFO_INT,String.valueOf(message),null,null);
            }
        }

        @Override
        public void into(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isInfoEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.INFO_INT,String.valueOf(message),null,t);
            }
        }

        @Override
        public void debug(Object message) {
            if(message instanceof  String || this.logger.isDebugEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.DEBUG_INT,String.valueOf(message),null,null);
            }
        }

        @Override
        public void debug(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isDebugEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.DEBUG_INT,String.valueOf(message),null,t);
            }
        }

        @Override
        public void trace(Object message) {
            if(message instanceof  String || this.logger.isTraceEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.TRACE_INT,String.valueOf(message),null,null);
            }
        }

        @Override
        public void trace(Object message, Throwable t) {
            if(message instanceof  String || this.logger.isTraceEnabled()){
                this.logger.log(null,FECQ,LocationAwareLogger.TRACE_INT,String.valueOf(message),null,t);
            }
        }

        @Override
        public boolean isFatalEnabled() {
            return this.logger.isErrorEnabled();
        }

        @Override
        public boolean isErrorEnabled() {
            return this.logger.isErrorEnabled();
        }

        @Override
        public boolean isWarnEnabled() {
            return this.logger.isWarnEnabled();
        }

        @Override
        public boolean isInfoEnabled() {
            return this.logger.isInfoEnabled();
        }

        @Override
        public boolean isDebugEnabled() {
            return this.logger.isDebugEnabled();
        }

        @Override
        public boolean isTraceEnabled() {
            return this.logger.isTraceEnabled();
        }
    }

    /**
     * JavaUtilLog日志
     */
    private static class JavaUtilLog implements Log,Serializable{

        private final String name;

        private final java.util.logging.Logger logger;

        public JavaUtilLog (String name){
            this.name =  name;
            this.logger =  java.util.logging.Logger.getLogger(name);
        }

        @Override
        public void fatal(Object message) {
            log(java.util.logging.Level.SEVERE,message,null);
        }

        @Override
        public void fatal(Object message, Throwable t) {
            log(java.util.logging.Level.SEVERE,message,t);
        }

        @Override
        public void error(Object message) {
            log(java.util.logging.Level.SEVERE,message,null);
        }

        @Override
        public void error(Object message, Throwable t) {
            log(java.util.logging.Level.SEVERE,message,t);
        }

        @Override
        public void warn(Object message) {
            log(java.util.logging.Level.WARNING,message,null);
        }

        @Override
        public void warn(Object message, Throwable t) {
            log(java.util.logging.Level.WARNING,message,t);
        }

        @Override
        public void info(Object message) {
            log(java.util.logging.Level.INFO,message,null);
        }

        @Override
        public void into(Object message, Throwable t) {
            log(java.util.logging.Level.INFO,message,t);
        }

        @Override
        public void debug(Object message) {
            log(java.util.logging.Level.FINE,message,null);
        }

        @Override
        public void debug(Object message, Throwable t) {
            log(java.util.logging.Level.FINE,message,t);
        }

        @Override
        public void trace(Object message) {
            log(java.util.logging.Level.FINEST,message,null);
        }

        @Override
        public void trace(Object message, Throwable t) {
            log(java.util.logging.Level.FINEST,message,t);
        }

        @Override
        public boolean isFatalEnabled() {
           return this.logger.isLoggable(java.util.logging.Level.SEVERE);
        }

        @Override
        public boolean isErrorEnabled() {
            return this.logger.isLoggable(java.util.logging.Level.SEVERE);
        }

        @Override
        public boolean isWarnEnabled() {
            return this.logger.isLoggable(java.util.logging.Level.WARNING);
        }

        @Override
        public boolean isInfoEnabled() {
            return this.logger.isLoggable(java.util.logging.Level.INFO);
        }

        @Override
        public boolean isDebugEnabled() {
            return this.logger.isLoggable(java.util.logging.Level.FINE);
        }

        @Override
        public boolean isTraceEnabled() {
            return this.logger.isLoggable(java.util.logging.Level.FINEST);
        }

        private void log(java.util.logging.Level level,Object message,Throwable exception){
            if(this.logger.isLoggable(level)){
                LogRecord rec;
                if(message instanceof LogRecord){
                   rec = (LogRecord)message;
                }else{
                    rec = new LocationResolvingLogRecord(level,String.valueOf(message));
                    rec.setLoggerName(this.name);
                    rec.setResourceBundleName(this.logger.getResourceBundleName());
                    rec.setResourceBundle(this.logger.getResourceBundle());
                    rec.setThrown(exception);
                }
                this.logger.log(rec);
            }
        }

    }

    private static class LocationResolvingLogRecord extends LogRecord{
        private static final String FECQ = java.util.logging.Logger.class.getName();

        private volatile boolean resolved;

        public LocationResolvingLogRecord(java.util.logging.Level level, String msg) {
            super(level, msg);
        }

        @Override
        public void setSourceClassName(String sourceClassName) {
            super.setSourceClassName(sourceClassName);
            resolved = true;
        }

        @Override
        public void setSourceMethodName(String sourceMethodName) {
            super.setSourceMethodName(sourceMethodName);
            resolved = true;
        }

        @Override
        public String getSourceClassName() {
            if(!resolved)
                resolve();
            return super.getSourceClassName();
        }

        @Override
        public String getSourceMethodName() {
            if(!resolved)
                resolve();
            return super.getSourceMethodName();
        }

        private void resolve(){
            StackTraceElement[] elements = new Throwable().getStackTrace();
            boolean found = false;
            String sourceClassName = null;
            String sourceMethodName = null;
            for(StackTraceElement element : elements){
                if(FECQ.equals(element.getClassName())){
                    found = true;
                }else if(found){
                    sourceClassName = element.getClassName();
                    sourceMethodName = element.getMethodName();
                    break;
                }
            }
            setSourceClassName(sourceClassName);
            setSourceMethodName(sourceMethodName);
        }

        @SuppressWarnings("deprecation")  // setMillis is deprecated in JDK 9
        protected Object writeReplace() {
            LogRecord serialized = new LogRecord(getLevel(), getMessage());
            serialized.setLoggerName(getLoggerName());
            serialized.setResourceBundle(getResourceBundle());
            serialized.setResourceBundleName(getResourceBundleName());
            serialized.setSourceClassName(getSourceClassName());
            serialized.setSourceMethodName(getSourceMethodName());
            serialized.setSequenceNumber(getSequenceNumber());
            serialized.setParameters(getParameters());
            serialized.setThreadID(getThreadID());
            serialized.setMillis(getMillis());
            serialized.setThrown(getThrown());
            return serialized;
        }
    }

}
