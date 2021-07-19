package com.buit.commons;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.buit.utill.ReturnEntity;
/**
* @ClassName: BaseSpringController
* @Description: 所有 的基础类
* @author 神算子
* @date 2020年4月26日 下午3:34:33
*
 */
public abstract class BaseController {
	static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	/**
	* @Title: exp
	* @Description: 统一异常处理
	* @return ReturnEntity<String>    返回类型
	* @author 神算子
	* @throws
	 */
	@ExceptionHandler
	@ResponseBody
	public ReturnEntity<String> exp(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		ReturnEntity<String> ret = new ReturnEntity<>();
		StringBuilder sb = new StringBuilder();
		if (ex instanceof BindException) {
			BindException bdex = (BindException) ex;
			List<ObjectError> errorList = bdex.getBindingResult().getAllErrors();
			for (ObjectError objectError : errorList) {
				if(objectError.getArguments()!=null&&objectError.getArguments().length>0) {
					MessageSourceResolvable mes=(MessageSourceResolvable)objectError.getArguments()[0];
					sb.append(mes.getDefaultMessage());
				}
				sb.append(objectError.getDefaultMessage()).append(" ");
			}
			ret.setError("E000", sb.toString(),null);
		} else if (ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException bdex = (MethodArgumentNotValidException) ex;
			List<ObjectError> errorList = bdex.getBindingResult().getAllErrors();
			for (ObjectError objectError : errorList) {
				if(objectError.getArguments()!=null&&objectError.getArguments().length>0) {
					MessageSourceResolvable mes=(MessageSourceResolvable)objectError.getArguments()[0];
					sb.append(mes.getDefaultMessage());
				}
				sb.append(objectError.getDefaultMessage()).append(" ");
			}
			ret.setError("E000", sb.toString(),null);
		}else if (ex instanceof MissingServletRequestParameterException) {
			MissingServletRequestParameterException bdex = (MissingServletRequestParameterException) ex;
			ret.setError("E000", bdex.getMessage(),null);
		}else if (ex instanceof BaseException) {
			BaseException myex = (BaseException) ex;
			ret.setError(myex.getCode(), myex.getMessage(),null);
		}else if (ex instanceof ConstraintViolationException) {
			ConstraintViolationException myex = (ConstraintViolationException) ex;
			String mes=myex.getMessage();
			if(!StringUtils.isEmpty(mes)) {
				mes=mes.replace("queryData.req.", "");
			}
			ret.setError("E000", mes,null);
		}else{
			logger.error(ex.getMessage(), ex);
//			ret.setError("E000", "后台出错，请联系管理员",null);
			ret.setError("E000", ex.getMessage(),null);
		}
		return ret;
	}
	
	
	
}
