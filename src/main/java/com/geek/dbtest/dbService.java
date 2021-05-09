package com.geek.dbtest;

public interface dbService {

    /**
     * 显示主库连接信息
     *
     * @throws Exception
     */
    void showMasterConnectionInfo() throws Exception;

    /**
     * 显示从库连接信息
     *
     * @throws Exception
     */
    void showSlaveConnectionInfo() throws Exception;
}
