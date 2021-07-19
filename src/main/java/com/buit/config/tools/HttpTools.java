package com.buit.config.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
/**
* @ClassName: HttpTools
* @Description: http请求类
* @author 神算子
* @date 2020年4月26日 下午3:50:54
*
 */
@Configuration
public class HttpTools {
	@Autowired  
    private RestTemplateBuilder builder;  
    @Bean  
    public RestTemplate restTemplate() {  
        return builder.build();  
    }  
}
