package org.springframework.beans;

import org.springframework.lang.Nullable;

/**
 * 可合并的接口
 */
public interface Mergeable {

    /**
     * 是否可合并
     * @return
     */
    boolean isMergeableEnabled();

    /**
     * 合并为新的对象
     */
    Object merge(@Nullable Object parent);
}
