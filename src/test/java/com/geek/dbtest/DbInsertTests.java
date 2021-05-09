package com.geek.dbtest;

import java.sql.*;
import java.util.UUID;

/**
 * 大批量数据插入测试类
 */
public class DbInsertTests extends DbTestApplicationTests {

    private static final String URL = "jdbc:mysql://129.204.220.248:3306/test?characterEncoding=utf-8&useSSL=false&autoReconnect=true&serverTimezone=GMT%2B8&rewriteBatchedStatements=true";

    private static final String USERNAME = "ceshi";

    private static final String PASSOWRD = "jianta@2020cs";

    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    /**
     * 插入100w条数据测试结果：
     * (1)normalInsert方法不管事务是否开启，插入效率极其低下，等待遥遥无期
     * (2)multiValuesInsert的方法耗时20秒左右，但是sql的长度被max_allowed_packet参数所限制，无法实现更大的批次数量，目前上限是50000
     * (3)batchInsert的方法耗时也是20秒左右，但是批次数量可以去到100000。经测试合适批次大小范围是10000~50000，过小或过大的批次大小将影响插入效率
     *
     * @param args
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        //normalInsert();
        //multiValuesInsert();
        batchInsert();
        long endTime = System.currentTimeMillis();
        System.out.println("用时" + (endTime - startTime) + "毫秒");

    }

    private static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DRIVER_CLASS_NAME);
            connection = DriverManager.getConnection(URL, USERNAME, PASSOWRD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 普通插入方式，即一条一条地来执行插入语句
     * (1)不开启事务
     * (2)开启事务
     */
    private static void normalInsert() {
        String sql = "INSERT INTO order_info (order_no, user_id, user_addr_id, pay_channel, total_num, total_amount, express_price, status) values (?,?,?,?,?,?,?,?)";

        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 10000; j++) {
                    String orderNo = UUID.randomUUID().toString().replace("-", "");
                    ps.setString(1, orderNo);
                    ps.setString(2, "1");
                    ps.setString(3, "1");
                    ps.setInt(4, 1);
                    ps.setInt(5, 1);
                    ps.setInt(6, 100000);
                    ps.setInt(7, 12000);
                    ps.setInt(8, 1);

                    ps.executeUpdate();
                }
                connection.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(ps, connection);
        }
    }

    /**
     * 通过multivalues形式的sql实现分批插入
     */
    private static void multiValuesInsert() {
        // 构建sql语句
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO order_info (order_no, user_id, user_addr_id, pay_channel, total_num, total_amount, express_price, status) values");
        for (int i = 0; i < 50000; i++) {
            sqlBuilder.append("(?,?,?,?,?,?,?,?),");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        String sql = sqlBuilder.toString();

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 50000; j++) {
                    int base = j * 8;
                    String orderNo = UUID.randomUUID().toString().replace("-", "");
                    ps.setString(base + 1, orderNo);
                    ps.setString(base + 2, "1");
                    ps.setString(base + 3, "1");
                    ps.setInt(base + 4, 1);
                    ps.setInt(base + 5, 1);
                    ps.setInt(base + 6, 100000);
                    ps.setInt(base + 7, 12000);
                    ps.setInt(base + 8, 1);
                }
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(ps, connection);
        }
    }

    /**
     * 通过addBatch方式分批插入
     */
    private static void batchInsert() {
        String sql = "INSERT INTO order_info (order_no, user_id, user_addr_id, pay_channel, total_num, total_amount, express_price, status) values (?,?,?,?,?,?,?,?)";

        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 50000; j++) {
                    String orderNo = UUID.randomUUID().toString().replace("-", "");
                    ps.setString(1, orderNo);
                    ps.setString(2, "1");
                    ps.setString(3, "1");
                    ps.setInt(4, 1);
                    ps.setInt(5, 1);
                    ps.setInt(6, 100000);
                    ps.setInt(7, 12000);
                    ps.setInt(8, 1);

                    ps.addBatch();
                }
                ps.executeBatch();
                ps.clearBatch();

                connection.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(ps, connection);
        }

    }

    private static void closeResource(PreparedStatement ps, Connection connection) {

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
