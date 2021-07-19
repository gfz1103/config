package com.buit.commons;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;

import com.buit.utill.SpringContextUtil;

/**
* @ClassName: BaseException
* @Description: 自定义异常 code,msg
* @author 神算子
* @date 2020年4月26日 下午3:33:12
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_CODE = "E000";
    private String code;
    public String getCode() {
        return code;
    }
    private BaseException(String code, String msg) {
        super(msg);
        this.code = code;
    }
    public static BaseException create(String errorCode) {
        String errorMsg = getmes(errorCode,null);
        return new BaseException(errorCode, errorMsg);
    }
    /**
   	 * 变量替换的错误码
   	 * 定义:login.fail=登录{1}失败{0}
   	 * 传参: Object[] par=new Object[]{"第一个","第二个"};
   	 * 输出:登录第二个失败第一个
   	 */
    public static BaseException create(String errorCode,Object[] args) {
    	String errorMsg= getmes(errorCode, args);
    	return new BaseException(errorCode,errorMsg );
    }
    
    private static String getmes(String code, Object[] args) {
    	MessageSource  messageSource=SpringContextUtil.getApplicationContext().getBean(MessageSource.class);
		Locale locale = LocaleContextHolder.getLocale();
		return  messageSource.getMessage("errorCode."+code,args,locale);
    }
    
    /**
     * @Description: 尽量使用  create(String errorCode) 方法创建异常
     */
    @Deprecated
    public static BaseException create(String errorCode, String message) {
        return new BaseException(errorCode, message);
    }
    /**
     * @Description: 尽量使用  create(String errorCode) 方法创建异常
     */
    @Deprecated
    public static BaseException createByMessage(String message) {
        return new BaseException(DEFAULT_CODE, message);
    }

}
