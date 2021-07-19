package com.buit.logPrint.lConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.buit.logPrint.lInterceptor.LogPrintInterceptor;

/**
 * @Auther: 神算子
 * @Date: 2020年7月13日10:25:29
 * @Description: 日志过滤器配置
 */
@Configuration
public class LogInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogPrintInterceptor()).addPathPatterns("/**");
    }
}


