package org.springframework.core;

/**
 * 别名注册器
 */
public interface AliasRegistry {

    /**
     * 注册别名
     * @param name 名称
     * @param alias 别名
     */
    void registryAlias(String name,String alias);

    /**
     * 移除别名
     * @param alias
     */
    void removeAlias(String alias);

    /**
     * 指定的名称是否有别名
     * @param name
     * @return
     */
    boolean isAlias(String name);

    /**
     * 获取指定名称的所以别名
     * @param name 名称
     * @return
     */
    String[] getAliases(String name);
}
