package com.geek.dbtest;

/**
 * 动态数据源上下文控制类
 */
public class DbContextHolder {

    private static final ThreadLocal<DbIndex> contextHolder = new ThreadLocal<>();

    /**
     * 设置数据源
     *
     * @param dbIndex
     */
    public static void setDbIndex(DbIndex dbIndex) {
        contextHolder.set(dbIndex);
    }

    /**
     * 取得当前数据源
     *
     * @return
     */
    public static DbIndex getDbIndex() {
        return contextHolder.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbIndex() {
        contextHolder.remove();
    }
}
