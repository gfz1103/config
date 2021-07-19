package com.buit.config.authority;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * 登录配置
 * @author 神算子
 */
@Component 
@ConfigurationProperties("buit.login") 
public class LoginCheckConfig {
    private boolean check=true;

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
	 
	 
}
