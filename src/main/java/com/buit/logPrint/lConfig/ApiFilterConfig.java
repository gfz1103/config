package com.buit.logPrint.lConfig;

import com.buit.logPrint.lFilter.ApiFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author huoshanshan
 * @Date 2021-04-12
 * @Description 过滤器
 **/
@Configuration
@ConditionalOnClass(ApiFilter.class)
public class ApiFilterConfig {

    @Bean
    @Autowired
    public FilterRegistrationBean<ApiFilter> regist(ApiFilter filter) {
        FilterRegistrationBean<ApiFilter> registration = new FilterRegistrationBean<ApiFilter>(filter);
        registration.addUrlPatterns("/*");
        return registration;
    }

}
