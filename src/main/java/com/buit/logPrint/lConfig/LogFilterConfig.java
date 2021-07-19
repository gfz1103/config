package com.buit.logPrint.lConfig;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.buit.logPrint.lFilter.LogFilter;

/**
 * @Auther: 神算子
 * @Date: 2020年7月13日10:25:29
 * @Description: 日志过滤器打印日志
 */
@Configuration
@ConditionalOnClass(LogFilter.class)
@EnableConfigurationProperties(PrintParam.class)
public class LogFilterConfig {
    @Resource
    private PrintParam properties;

    public LogFilterConfig() {
    }

    @Bean
    public FilterRegistrationBean<LogFilter> addFilter() {
        LogFilter filter  = new LogFilter(properties);
        FilterRegistrationBean<LogFilter> registration = new FilterRegistrationBean<LogFilter>(filter);
        registration.addUrlPatterns(properties.getFilterIncludePattern());
        return registration;
    }
}