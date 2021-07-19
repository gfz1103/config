package com.buit.config.authority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
* @ClassName: ClientInterceptorConf
* @Description: 拦截方法
* @author 神算子
* @date 2020年4月26日 下午3:40:41
 */
@Configuration
public class ClientInterceptorConf implements WebMvcConfigurer {
	static final Logger logger = LoggerFactory.getLogger(ClientInterceptorConf.class);
	@Autowired
	LoginCheckConfig loginCheckConfig;
	@Bean
	public AuthorityInterceptor authorityInterceptor() {
		return new AuthorityInterceptor();
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if(loginCheckConfig.isCheck()) {
			registry.addInterceptor(authorityInterceptor()).addPathPatterns("/**");	
		}
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}
