package com.geek.dbtest;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据库动态路由控制实现
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDbIndex();
    }
}
