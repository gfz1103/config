package com.buit.config.authority;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.buit.config.annotation.OpenApi;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.buit.utill.SpringContextUtil;
/**
* @ClassName: AuthorityInterceptor
* @Description: 登录权限拦截
* @author 神算子
* @date 2020年4月26日 下午3:40:08
*
 */
public class AuthorityInterceptor implements HandlerInterceptor {
	/**
	 * 开发环境
	 */
	static final String DEVELOP_ENVIRONMENT = "dev";
	static final String ACTIVE = "knife4j.production";

	static final Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);
	private StringRedisTemplate redisTemplate;
	
	private StringRedisTemplate getRedisTemplate() {
		if(redisTemplate==null) {
			redisTemplate  =SpringContextUtil.getApplicationContext().getBean(StringRedisTemplate.class);
		}
		return redisTemplate;
	}

    /**
     * 该方法将在请求处理之前进行调用。
     * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行，而且所有的Interceptor中的preHandle方法都会在
     * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的，这种中断方式是令preHandle的返
     * 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			//放过openapi, openapi的权限校验在 com.buit.openapi.OpenApiControllerAdvice
			if(handlerMethod.getBean().getClass().isAnnotationPresent(OpenApi.class)){
				return true;
			}
		}

//    	request.getHeader("Origin");// 限制只能微信发出
    	Environment env = SpringContextUtil.getApplicationContext().getBean(Environment.class);
    	boolean flag=true;
    	String mess=null;
    	String token=request.getHeader("token");
    	if(StringUtils.isEmpty(token)) {
    		token=request.getParameter("token");
    	}
		String exStr = "ex/";
		if (request.getRequestURI().contains("druid2")) {
			return true;
		}
		if (!request.getRequestURI().contains(exStr)) {
			int timeout = 120;
			if (StringUtils.isNotBlank(token)) {
				//判断sesion是否失效
				String refreshTokenKey = String.format("JWT_TOKEN::%s", token);		
				if(getRedisTemplate().expire(refreshTokenKey, timeout, TimeUnit.MINUTES)) {
					
				}else {//失效提示重新登录
					mess=env.getProperty("errorCode.ERROR_USER_0008");
				}
			} else {
				 //判断开发环境
				//如果开发环境 为了测试方便
				if(env.getProperty(ACTIVE)!=null&&env.getProperty(ACTIVE).equals("true")) {
					 flag=false;
					 mess=env.getProperty("errorCode.ERROR_USER_0009");
	            }
			}
		}
		if(flag==false) {
			PrintWriter out = null;
	        try {
	        	response.setCharacterEncoding("UTF-8");
	        	response.setContentType("application/json");
	            out = response.getWriter();
	            out.println("{\"errorCode\": \"ERROR_USER_0000\",\"message\": \""+mess+"\",\"data\": null}");
	        } catch (Exception e) {
	        	logger.error(e.getMessage(), e);
	        } finally {
	            if (null != out) {
	                out.flush();
	                out.close();
	            }
	        }
		}
        return flag;
    }
 
    /**
     * 在当前请求进行处理之后，也就是Controller 方法调用之后执行，但是它会在DispatcherServlet 进行视图返回渲染之前被调用，所以我们可以在这个方法中对Controller 处理之后的ModelAndView 对象进行操作。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
 
 
    /**
     * 该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行。这个方法的主要作用是用于进行资源清理工作的。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }


}
