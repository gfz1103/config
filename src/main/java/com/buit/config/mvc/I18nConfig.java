package com.buit.config.mvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
* @ClassName: I18nConfig
* @Description: 国际化配置
* @author 神算子
* @date 2020年4月26日 下午3:41:17
*
 */
@Configuration
public class I18nConfig  implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocaleResolver();
//        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
//        sessionLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
//        return  sessionLocaleResolver;
    }
 
    //注册到拦截器中
 
    //@Bean
    //public LocaleChangeInterceptor localeChangeInterceptor(){
    //    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    //    localeChangeInterceptor.setParamName("lang");
    //    return localeChangeInterceptor;
    //}
 
    //@Override
    //protected void addInterceptors(InterceptorRegistry registry) {
    //    registry.addInterceptor(localeChangeInterceptor());
    //}
    
    
   


}