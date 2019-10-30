package org.springframework.beans;

import org.springframework.lang.Nullable;

/**
 * 获取数据源接口
 */
public interface BeanMetadataElement {

    /**
     * 获取数据源
     * @return
     */
    @Nullable
    default Object getSource(){
        return null;
    }
}
