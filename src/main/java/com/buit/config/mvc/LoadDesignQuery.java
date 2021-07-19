//package com.buit.config.mvc;
//
//import java.io.ByteArrayInputStream;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.ibatis.builder.xml.XMLMapperBuilder;
//import org.apache.ibatis.session.Configuration;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//@Component
//@Order(value = 2)
///**
//* @ClassName: EndRunner
//* @Description: 启动后设置一些属性
//* @author 神算子
//* @date 2020年4月26日 下午3:41:17
//*
// */
//public class LoadDesignQuery implements ApplicationRunner {
//	static final Logger logger = LoggerFactory.getLogger(LoadDesignQuery.class);
//    @Autowired
//    SqlSessionFactory sessionFactory;
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Override
//    public void run(ApplicationArguments applicationArguments) throws Exception {
//
//    	if(applicationContext.containsBean("queCxfbSer")) {
//    		SqlSession sqlSession=sessionFactory.openSession(true);
//    		Configuration configuration= sessionFactory.getConfiguration();
//    		 List<Map<String, Object>> ret= sqlSession.selectList("com.buit.his.sys.adminms.dao.QueCxfbDao.findAllDesign");
//    		 if(ret != null &&! ret.isEmpty()) {
//    			 for (Map<String, Object> map : ret) {
//    				try {
//    					StringBuilder sqlMaper= new StringBuilder();
//    		    		sqlMaper.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?> ");
//    		    		sqlMaper.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\"> ");
//    		    		sqlMaper.append("<mapper namespace=\"com.buit.design.query.");
//    		    		sqlMaper.append(map.get("enName").toString());
//    		    		sqlMaper.append("\"> ");
//    	    			sqlMaper.append(" <select id=\"query\" resultType=\"Map\"> ");
//    	    			sqlMaper.append(map.get("selectSql").toString());
//    	    			sqlMaper.append(" </select> ");
//    		    		sqlMaper.append(" </mapper>");
//    		      	  	String assistant="com.buit.design.query"+map.get("enName").toString();
////    		      	  	System.out.println(sqlMaper.toString());
//    		        	ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sqlMaper.toString().getBytes());
//    		        	XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(byteArrayInputStream, configuration, assistant, configuration.getSqlFragments());
//    		            xmlMapperBuilder.parse();
//					} catch (Exception e) {
//						logger.error("自定义查询="+map.get("enName").toString());
//						logger.error(e.getMessage(),e);
//					}
//    			}
//    		 }
//    		 sqlSession.close();
//    	}
//    }
//}