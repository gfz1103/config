package com.buit.config.datasource;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.buit.config.properties.MultipleDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author yueyu
 * @Date 2021/3/7 15:16
 */
@Configuration
@ConditionalOnProperty("spring.datasource.relaySequence")
@Import(MonitorDataSourceAspect.class)
public class DynamicDataSourceConfig {

    @Autowired
    MultipleDataSourceProperties multipleDataSourceProperties;

    @Bean
    public Map<String,DruidDataSource> createAllDataSource(){
        Map<String,DruidDataSource> dataSourceMap = new HashMap<>();
        multipleDataSourceProperties.getMultiple().forEach((k,v)->{
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(v.getUrl());
            dataSource.setUsername(v.getUsername());
            dataSource.setPassword(v.getPassword());
            dataSource.setMaxActive(v.getMaxActive());
            dataSource.setDriverClassName(v.getDriverClassName());
            dataSource.setInitialSize(v.getInitialSize());
            dataSource.setMaxWait(v.getMaxWait());
            dataSource.setMinIdle(v.getMinIdle());
            dataSource.setTimeBetweenEvictionRunsMillis(v.getTimeBetweenEvictionRunsMillis());
            dataSource.setMinEvictableIdleTimeMillis(v.getMinEvictableIdleTimeMillis());
//            dataSource.setValidationQuery(v.getValidationQuery());
//            dataSource.setTestWhileIdle(v.getTestWhileIdle());
//            dataSource.setTestOnBorrow(v.getTestOnBorrow());
//            dataSource.setTestOnReturn(v.getTestOnReturn());
            dataSource.setPoolPreparedStatements(v.getPoolPreparedStatements());
            dataSource.setMaxOpenPreparedStatements(v.getMaxOpenPreparedStatements());
            dataSourceMap.put(k,dataSource);
        });
        return dataSourceMap;
    }
    @Bean
    public DataSource routingDataSource(Map<String,DruidDataSource> createAllDataSource){
        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        Map<Object,Object> targetDataSource = new HashMap<>();
        targetDataSource.putAll(createAllDataSource);
        routingDataSource.setDefaultKey(multipleDataSourceProperties.getDefaultName());
        routingDataSource.setTargetDataSources(targetDataSource);
        routingDataSource.setDefaultTargetDataSource(createAllDataSource.get(multipleDataSourceProperties.getDefaultName()));
        return routingDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(routingDataSource(createAllDataSource()));
    }

}
