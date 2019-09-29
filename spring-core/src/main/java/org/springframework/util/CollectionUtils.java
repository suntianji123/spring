package org.springframework.util;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * 集合工具类
 */
public abstract class CollectionUtils {

    /**
     * 判断集合是否为空调
     * @param collections 集合
     * @return
     */
    public static boolean isEmpty(@Nullable  Collection<?> collections){
        return collections == null || collections.isEmpty();
    }
}
