package com.buit.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.pool.vendor.MySqlValidConnectionChecker;
import com.buit.config.properties.MultipleDataSourceProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author yueyu
 * @Date 2021/3/8 11:16
 */
@Aspect
public class MonitorDataSourceAspect {

    static final Logger log = LoggerFactory.getLogger(MonitorDataSourceAspect.class);
    @Autowired
    DynamicRoutingDataSource dynamicRoutingDataSource;
    @Autowired
    Map<String, DruidDataSource> druidDataSourceMap;
    @Autowired
    MultipleDataSourceProperties dataSourceProperties;
    MySqlValidConnectionChecker checker = new MySqlValidConnectionChecker();
    String curDataBase;
    List<String> disableDataBase = new ArrayList<>();
    ThreadLocal<AtomicInteger> threadLevel = new ThreadLocal<>();

    @Around("execution(public * com.buit..*.service..*.*(..)) || execution(public * com.buit..*.service.*.*(..)) || @annotation(com.buit.config.datasource.DynamicDataSource)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        AtomicInteger level = threadLevel.get();
        if(level==null){
            level= new AtomicInteger(0);
            threadLevel.set(level);
            setEnableDataBase();
        }else{
            level.addAndGet(1);
        }

        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable throwable) {
            throw throwable;
        }finally {
            if(level.getAndAdd(-1)==0){
                dynamicRoutingDataSource.removeLookupKey();
                threadLevel.remove();
            }
        }
    }

    private void setEnableDataBase(){
        if(dynamicRoutingDataSource.isNotSetKey()){
            log.info("检查数据库状态");
            if(curDataBase==null){
                curDataBase=dynamicRoutingDataSource.getDefaultKey();
            }
            if(disableDataBase.contains(curDataBase)||!checkDataBaseStatus(curDataBase)){
                Optional<String> key = dataSourceProperties.getRelaySequence().stream()
                        .filter(k->!k.equals(curDataBase)&&!disableDataBase.contains(k))
                        .filter(k->checkDataBaseStatus(k))
                        .findFirst();
                if(key.isPresent()){
                    log.info("切换数据库[{}]",key.get());
                    curDataBase=key.get();
                }else{
                    log.error("无可用数据源");
                }
            }
        }
    }

    public boolean checkDataBaseStatus(String routingKey){
        DruidDataSource druidDataSource = druidDataSourceMap.get(routingKey);
        try {
            DruidPooledConnection connection = druidDataSource.getConnection();
            checker.isValidConnection(connection,druidDataSource.getValidationQuery(),druidDataSource.getValidationQueryTimeout());
            connection.close();
            dynamicRoutingDataSource.setLookupKey(routingKey);
            return true;
        } catch (Exception e) {
            log.error("数据库异常[{}]",routingKey,e);
            disableDataBase.add(routingKey);
            druidDataSourceMap.get(routingKey).close();
            return false;
        }
    }
}
