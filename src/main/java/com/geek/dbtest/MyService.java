package com.geek.dbtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
public class MyService implements dbService {

    @Autowired
    @Qualifier("readWriteManualDataSource")
    private DataSource dataSource;

    @DbSwitch(DbIndex.MYSQL_MASTER)
    @Override
    public void showMasterConnectionInfo() throws Exception {
        Connection connection = dataSource.getConnection();
        String url = connection.getMetaData().getURL();
        System.out.println("主库url:" + url);
    }

    @DbSwitch(DbIndex.MYSQL_SLAVE)
    @Override
    public void showSlaveConnectionInfo() throws Exception {
        Connection connection = dataSource.getConnection();
        String url = connection.getMetaData().getURL();
        System.out.println("从库url:" + url);
    }
}
