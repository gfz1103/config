package com.buit.utill;

import java.io.Serializable;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;

/**
 * @author
 */

public class ReturnEntity<E> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 错误码 */
	private String errorCode = "SUCCESS";
	/** 返回消息 */
	private String message;
	/** 数据 */
	private E data;

	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * 方法说明 ：设置错误消息<br>
	 * 参数说明 ：errorCode 错误码 创立日期 ：2015年4月20日 10:16:02<br>
	 */
	public ReturnEntity<E> setError(String errorCode, Object[] args ) {
		MessageSource  messageSource=SpringContextUtil.getApplicationContext().getBean(MessageSource.class);
		Locale locale = LocaleContextHolder.getLocale();
		this.message =  messageSource.getMessage("errorCode."+errorCode,args,locale);
		this.errorCode = errorCode;
		return this;
	}

	/**
	 * 方法说明 ：设置错误消息指定错误消息 参数说明 ：errorCode 错误码 创立日期 ：2015年4月20日 10:16:02<br>
	 */
	public void setError(String errorCode,String message, Object[] args) {
		this.errorCode = errorCode;
		this.message = message;
	}
	/**
	 * 方法说明 ：设置成功的提示信息<br>
	 * 参数说明 ：successCode 消息码 创立日期 ：2020年6月11日15:23:52<br>
	 */
	public ReturnEntity<E> setSuccess(String successCode, Object[] args ) {
		MessageSource  messageSource=SpringContextUtil.getApplicationContext().getBean(MessageSource.class);
		Locale locale = LocaleContextHolder.getLocale();
		this.message =  messageSource.getMessage("successCode."+successCode,args,locale);
		return this;
	}

	/**
	 * 方法说明 ：设置成功消息<br>
	 * 参数说明 ：<br>
	 * 创立日期 ：2015年4月20日 10:16:17<br>
	 * 创建人 ：文光临<br>
	 */
	public void setSuccess(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}
}
