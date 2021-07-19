package com.buit.config.servlet;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
/**
* @ClassName: ServletConfig
* @Description: 设置静态资源路径
* @author 神算子
* @date 2020年4月26日 下午3:43:29
 */
@Configuration
public class ServletConfig {
	@Bean
	public ConfigurableServletWebServerFactory containerCustomizer() {
	  TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	  factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/disk/index.html"));
	  return factory;
	}
}
