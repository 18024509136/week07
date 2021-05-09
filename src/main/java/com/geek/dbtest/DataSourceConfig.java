package com.geek.dbtest;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置类，配置信息复用shardingSphere的master和slave0部分
 */
@Configuration
public class DataSourceConfig {

    /**
     * 注册主库数据源
     *
     * @return
     */
    @Bean("mysqlMaster")
    @ConfigurationProperties(prefix = "spring.shardingsphere.datasource.master")
    public DataSource mysqlMaster() {
        return new DruidDataSource();
    }

    /**
     * 注册从库数据源
     *
     * @return
     */
    @Bean("mysqlSlave")
    @ConfigurationProperties(prefix = "spring.shardingsphere.datasource.slave0")
    public DataSource mysqlSlave() {
        return new DruidDataSource();
    }

    /**
     * 定义动态数据源，将主库和从库置入动态数据源
     *
     * @param mysqlMaster
     * @param mysqlSlave
     * @return
     */
    @Bean("readWriteManualDataSource")
    public DataSource multipleDataSource(@Qualifier("mysqlMaster") DataSource mysqlMaster,
                                         @Qualifier("mysqlSlave") DataSource mysqlSlave) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>(6);
        targetDataSources.put(DbIndex.MYSQL_MASTER, mysqlMaster);
        targetDataSources.put(DbIndex.MYSQL_SLAVE, mysqlSlave);

        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(mysqlSlave);
        return dynamicDataSource;
    }
}
