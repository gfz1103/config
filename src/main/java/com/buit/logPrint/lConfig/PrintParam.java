package com.buit.logPrint.lConfig;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @Auther: 神算子
 * @Date: 2020年7月13日10:25:29
 * @Description: 打印参数配置
 */
@ConfigurationProperties(prefix = "log-param")
public class PrintParam {
    private boolean enableInputParam = true;
    private boolean enableOutputResult = false;
    private boolean enableHead = false;
    private boolean enableQueryString = false;
    private String filterIncludePattern = "/*";
    private String filterExcludePattern = "(/swagger-resources/.*|/webjars/.*|/css/.*|/images/.*|/fonts/.*|/js/.*)";


    public boolean getEnableQueryString() {
		return enableQueryString;
	}

	public void setEnableQueryString(boolean enableQueryString) {
		this.enableQueryString = enableQueryString;
	}

	public boolean getEnableHead() {
		return enableHead;
	}

	public void setEnableHead(boolean enableHead) {
		this.enableHead = enableHead;
	}

	public Boolean getEnableInputParam() {
        return enableInputParam;
    }

    public void setEnableInputParam(Boolean enableInputParam) {
        this.enableInputParam = enableInputParam;
    }

    public Boolean getEnableOutputResult() {
        return enableOutputResult;
    }

    public void setEnableOutputResult(Boolean enableOutputResult) {
        this.enableOutputResult = enableOutputResult;
    }

    public String getFilterIncludePattern() {
        return filterIncludePattern;
    }

    public void setFilterIncludePattern(String filterIncludePattern) {
        this.filterIncludePattern = filterIncludePattern;
    }

    public String getFilterExcludePattern() {
        return filterExcludePattern;
    }

    public void setFilterExcludePattern(String filterExcludePattern) {
        this.filterExcludePattern = filterExcludePattern;
    }
    
    
    public boolean isRequestExcluded(HttpServletRequest httpRequest) {
        boolean ret= filterExcludePattern != null
                && Pattern.compile(filterExcludePattern)
                        .matcher(httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()))
                        .matches();
        if(!ret) {
        	String url = httpRequest.getRequestURI();
        	String contentType= httpRequest.getHeader("Content-Type");
        	if(StringUtils.isNotEmpty(contentType)&&contentType.contains("multipart/form-data")) {
        		return true;
        	}
        	if(url.contains("swagger")) {
        		return true;
        	}
        	if(url.endsWith(".js")) {
        		return true;
        	}
        	if(url.endsWith(".txt")) {
        		return true;
        	}
        	if(url.endsWith(".html")) {
        		return true;
        	}
        	if(url.endsWith(".json")) {
        		return true;
        	}
        	if(url.endsWith(".ico")) {
        		return true;
        	}
        	if(url.contains("/v2/api-docs")) {
        		return true;
        	}
        }
        return ret;
    }
}
