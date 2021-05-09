package com.geek.dbtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReadWriteManualTests extends DbTestApplicationTests {

    @Autowired
    private MyService myService;

    @Test
    public void readWriteManualTestMaster() throws Exception {
        // 主库url:jdbc:mysql://localhost:3306/write_db?characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=UTC
        myService.showMasterConnectionInfo();
    }

    @Test
    public void readWriteManualTestSlave() throws Exception {
        // 打印从库url:jdbc:mysql://localhost:3306/read_db?characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=UTC
        myService.showSlaveConnectionInfo();
    }
}
