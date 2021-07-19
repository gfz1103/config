package com.buit.config.mvc;

import java.sql.Date;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
@Order(value = 1)
/**
* @ClassName: EndRunner
* @Description: 启动后设置一些属性
* @author 神算子
* @date 2020年4月26日 下午3:41:17
*
 */
public class EndRunner implements ApplicationRunner {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    @Autowired
    DataSource dataSource;
    

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        String letter = "oracle.jdbc";
        if (objectMapper == null) {
            return;
        }
        SimpleModule serializerModule = new SimpleModule("SqlDateDeserializer", PackageVersion.VERSION);
        serializerModule.addDeserializer(Timestamp.class, new MySqlTimeDeSerializer());
        serializerModule.addDeserializer(Date.class, new MySqlDateDeSerializer());
        objectMapper.registerModule(serializerModule);
//        DruidDataSource dr = (DruidDataSource) dataSource;
//        if (dr.getDriverClassName().startsWith(letter)) {
//            sqlSessionFactory.getConfiguration().setJdbcTypeForNull(JdbcType.NULL);
//        }
        sqlSessionFactory.getConfiguration().setCallSettersOnNulls(true);
    }
}
