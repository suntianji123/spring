package org.springframework.core.log;

import org.springframework.lang.Nullable;

import java.util.function.Supplier;

/**
 * 日志消息类
 */
public abstract class LogMessage implements CharSequence {

    /**
     * 结果字符串
     */
    @Nullable
    private String result;

    /**
     * 获取字符寻列某个下标的字符
     * @param index
     * @return
     */
    @Override
    public char charAt(int index) {
        return toString().charAt(index);
    }

    /**
     * 获取字符序列的长度
     * @return
     */
    @Override
    public int length() {
        return toString().length();
    }

    /**
     * 截取某段字符
     * @param start 起始位置
     * @param end 结束为止
     * @return
     */
    @Override
    public CharSequence subSequence(int start, int end) {
       return toString().subSequence(start,end);
    }

    @Override
    public String toString() {
        if(this.result == null){
            this.result = buildString();
        }
        return this.result;
    }

    abstract String buildString();

    /**
     * 根据Supplier对象获取CharSequence对象
     * @param supplier supplier对象
     * @return
     */
    public static final CharSequence of(Supplier<? extends CharSequence> supplier){
        return new SupplierMessage(supplier);
    }

    private static final class SupplierMessage extends LogMessage{

        private Supplier<? extends CharSequence> supplier;

        SupplierMessage(Supplier<? extends CharSequence> supplier){
            this.supplier = supplier;
        }

        @Override
        String buildString() {
           return supplier.get().toString();
        }
    }
}
