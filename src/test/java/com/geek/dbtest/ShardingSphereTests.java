package com.geek.dbtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * shardingSphere代理的读写分离数据源名称是masterSlaveDataSource
 * sharding sphere框架读写分离测试，多个读库的均衡策略默认采用的是轮询
 */
public class ShardingSphereTests extends DbTestApplicationTests {

    @Autowired
    @Qualifier("masterSlaveDataSource")
    private DataSource dataSource;

    @Test
    public void readWriteSeperatedTest() throws Exception {
        Connection connection = dataSource.getConnection();

        for (int i = 0; i < 4; i++) {
            PreparedStatement ps = connection.prepareStatement("select * from person");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt(1);
                String username = rs.getString(2);
                int age = rs.getInt(3);
                System.out.println("userId:" + userId);
                System.out.println("username:" + username);
                System.out.println("age:" + age);
                System.out.println("=================");
            }

            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }

        }
        if (connection != null) {
            connection.close();
        }
    }
}
