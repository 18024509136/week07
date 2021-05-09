package com.geek.dbtest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 定义基于注解@DbSwitch的切面
 */
@Aspect
@Component
public class DataSourceSwitchAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceSwitchAspect.class);

    @Pointcut("@annotation(com.geek.dbtest.DbSwitch)")
    public void dbSwitchPointCut() {

    }

    /**
     * 调用业务方法前，将数据源切换至注解指定的数据源
     *
     * @param joinPoint
     */
    @Before("dbSwitchPointCut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DbSwitch annotation = method.getAnnotation(DbSwitch.class);
        DbIndex dbIndex = annotation.value();
        DbContextHolder.setDbIndex(dbIndex);
        LOGGER.info("切换至" + dbIndex);
    }

    /**
     * 业务方法调用完毕后，清除自定义路由数据源的上下文
     *
     * @param joinPoint
     */
    @After("dbSwitchPointCut()")
    public void after(JoinPoint joinPoint) {
        DbContextHolder.clearDbIndex();
        LOGGER.info("清除DataSource自定义路由");
    }
}
