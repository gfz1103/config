package com.buit.logPrint.lInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.buit.logPrint.lConfig.PrintParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther: 神算子
 * @Date: 2020年7月13日10:25:29
 * @Description: 日志过滤器打印日志
 */
public class LogPrintInterceptor extends HandlerInterceptorAdapter {

    
    @Autowired
    private PrintParam properties=new PrintParam();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	 if (properties.isRequestExcluded(request)){
             return true;
         }
    	 if (handler instanceof ResourceHttpRequestHandler) {
    		 return true;
		}
    	if (!(handler instanceof HandlerMethod)) {
    		return true;
    	}
		if (properties.getEnableInputParam()) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			// 获取类上注解
			Class<?> clazz = handlerMethod.getBeanType();
			Logger log = LoggerFactory.getLogger("http-parameter--"+MDC.get("lUser"));
			if (clazz.isAnnotationPresent(Api.class)) {
				Api api = (Api) clazz.getAnnotation(Api.class);
				// 获取方法上的注解
				ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
				if(apiOperation!=null)
					log.info("{}-{}{}", api.tags(), api.value(), apiOperation.value());
			}			
		}
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  throws Exception {
    }
}
