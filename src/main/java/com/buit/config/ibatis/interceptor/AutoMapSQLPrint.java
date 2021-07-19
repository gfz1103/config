package com.buit.config.ibatis.interceptor;

import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author 神算子
 * @Date 2020年8月11日20:12:16
 */
//@Intercepts({@Signature(type = StatementHandler.class, method = "parameterize", args = {Statement.class})})

@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
@Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
@Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class AutoMapSQLPrint implements Interceptor {

    DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
    DateFormat timeFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 执行目标方法
		
		long startTime = System.currentTimeMillis();
		Object result = invocation.proceed();
		long endTime = System.currentTimeMillis();
        long sqlCost = endTime - startTime;
       
		StatementHandler sh = (StatementHandler) invocation.getTarget();
//      ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
//      ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
//      ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
//      MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
		StringBuilder loginfo = new  StringBuilder("===>  ");
		String fsql = showSql(configuration, sh.getBoundSql());
		loginfo.append(fsql);
		
		String queryId = mappedStatement.getId();
		StringBuilder methodName = new  StringBuilder("ack.");
		SqlCommandType sqltype=	mappedStatement.getSqlCommandType();
		if(sqltype==sqltype.SELECT) {
			methodName.append("s.");
			
			if(result  instanceof Collection&& result !=null){
				if(result  instanceof List&& result !=null){
					List col=	(List) result;
					if(col.size()>0) {
						Object con=col.get(0);
						if(con  instanceof Number&& con !=null){
							loginfo.append(" 结果:");
							loginfo.append(con.toString());
						}else {
							loginfo.append(" 返回条数:");
							loginfo.append(((Collection) result).size());
						}
					}
				}else {
					loginfo.append(" 返回条数:");
					loginfo.append(((Collection) result).size());	
				}
			}
			if(result  instanceof Number&& result !=null){
				loginfo.append(" 结果:");
				loginfo.append(result.toString());
			}
			
		}else{
			methodName.append("u.");
			if(result  instanceof Number&& result !=null){
				loginfo.append(" 受影响:");
				loginfo.append(result.toString());
				loginfo.append("行");
			}
		}
		String[] strArr = queryId.split("\\.");
		
		if(queryId.length()>2) {
			methodName.append(strArr[strArr.length - 2]).append("_").append(strArr[strArr.length - 1]);	
		}
		Logger log = LoggerFactory.getLogger(methodName.toString());
		
//	    String formatSql = com.alibaba.druid.sql.SQLUtils.format(fsql, "mysql");
		loginfo.append(" 耗时:");
		loginfo.append(sqlCost);
		loginfo.append("毫秒");
		log.info(loginfo.toString());
		return result;
	}

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
    
    
    /**
     * 解析sql语句
     * @param configuration
     * @param boundSql
     * @return
     */
    public String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));

            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    }
                }
            }
        }
        return sql;
    }

    /**
     * 参数解析
     * @param obj
     * @return
     */
    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof java.sql.Date) {
            value = "'" + dateFormat.format(obj) + "'";
        } else if (obj instanceof Date||obj instanceof Timestamp) {
            value = "'" + timeFormat.format(obj) + "'";
        }else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

//    private String setParameters(StatementHandler sh) throws Exception {
//        BoundSql boundSql = sh.getBoundSql();
//        Object parameterObject = boundSql.getParameterObject();
//        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
//        StringBuilder fsql  = new StringBuilder();
//        if (parameterMappings != null) {
//            for (int i = 0; i < parameterMappings.size(); i++) {
//                ParameterMapping parameterMapping = parameterMappings.get(i);
//                if (parameterMapping.getMode() != ParameterMode.OUT) {
//                    Object value;
//                    String propertyName = parameterMapping.getProperty();
//                    TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
//                    if (boundSql.hasAdditionalParameter(propertyName)) {
//                        value = boundSql.getAdditionalParameter(propertyName);
//                    } else if (parameterObject == null) {
//                        value = null;
//                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
//                        value = parameterObject;
//                    } else {
//                        MetaObject metaObject = SystemMetaObject.forObject(parameterObject);
//                        value = metaObject.getValue(propertyName);
//                    }
//                    setParameter(boundSql, parameterMappings.size(), i, value,fsql);
//                }
//            }
//        }
//        if(fsql.length()==0) {
//        	return boundSql.getSql();
//        }
//       return fsql.toString();
//    }

//    private void setParameter(BoundSql boundSql, int length, int index, Object value,StringBuilder fsql) {
//        String sql = boundSql.getSql();
//        String[] split = sql.split("\\?");
//        fsql.append(split[index])
//                .append("'")
//                .append(value)
//                .append("'");
//        if (split.length > length ) {
//            fsql.append(split[length]);
//        }
//    }

}
