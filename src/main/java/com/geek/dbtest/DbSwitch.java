package com.geek.dbtest;

import java.lang.annotation.*;

/**
 * 数据库切换注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DbSwitch {

    DbIndex value() default DbIndex.MYSQL_SLAVE;
}
