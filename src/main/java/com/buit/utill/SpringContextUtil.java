package com.buit.utill;

import org.springframework.context.ConfigurableApplicationContext;
/**
 * @author 神算子
 */
public class SpringContextUtil {
	private static ConfigurableApplicationContext applicationContext;
	public static ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
		SpringContextUtil.applicationContext = applicationContext;
	}
}
