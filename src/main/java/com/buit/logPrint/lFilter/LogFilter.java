package com.buit.logPrint.lFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.buit.commons.BaseException;
import com.buit.commons.SysUser;
import com.buit.his.dao.SysMenuApiDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.buit.logPrint.lConfig.PrintParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Auther: 神算子
 * @Date: 2020年7月13日10:25:29
 * @Description: 日志过滤器打印日志
 */
public class LogFilter implements Filter {

	private PrintParam properties;
	private String secretKey = "abcdef";

	public LogFilter(PrintParam properties) {
		this.properties = properties;
	}

	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (properties == null) {
			properties = new PrintParam();
		}
		HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
		ServletRequest nextReq = servletRequest;
		if (properties.isRequestExcluded(httpReq)) {
			filterChain.doFilter(nextReq, servletResponse);
		} else {
			long startTime = System.currentTimeMillis();
			String token = httpReq.getHeader("token");
			String user="";
			if(StringUtils.isNotEmpty(token)) {
				user=getUser(token);
				MDC.put("lUser",user);				  
	            MDC.put("clientIp", getIpAddress(httpReq));//客户端IP        
//				TraceId.logTraceID.set(String.valueOf(user));
			}
			Logger LOG = LoggerFactory.getLogger("http-parameter--"+user);
			if (properties.getEnableInputParam()) {
				if (properties.getEnableHead()) {//打印头信息
					LOG.info("request uri:{}", httpReq.getQueryString());
				}
				String url = httpReq.getRequestURI();
				LOG.info("request uri:{}", url);
				if (properties.getEnableHead()) {//打印头信息
					Map<String, String> map = new HashMap<String, String>();
					Enumeration <String> headerNames = httpReq.getHeaderNames();
					while (headerNames.hasMoreElements()) {
						String key = (String) headerNames.nextElement();
						String value = httpReq.getHeader(key);
						map.put(key, value);
					}
					LOG.info("request header:{}", map.toString());	
				}
				String contentType = httpReq.getHeader("Content-Type");
				if (StringUtils.isNotEmpty(contentType) && contentType.contains("application/json")) {
					ParamRequestWrapper requestWrapper = new ParamRequestWrapper(httpReq);
					BufferedReader bufferedReader = requestWrapper.getReader();
					String line;
					StringBuilder sb = new StringBuilder();
					while ((line = bufferedReader.readLine()) != null) {
						sb.append(line);
					}
					LOG.info("request json:{}" , sb.toString());
					nextReq = requestWrapper;
				} else {
					LOG.info("request form:{}" , getParamString(httpReq.getParameterMap()));
				}
			}
			ServletResponse nextRes = servletResponse;
			if (properties.getEnableOutputResult()) {
				nextRes = new ResponseWrapper((HttpServletResponse) servletResponse);
			}
			filterChain.doFilter(nextReq, nextRes);
			if (properties.getEnableOutputResult()) {
				String result = new String(((ResponseWrapper) nextRes).getResponseData());
				servletResponse.setContentLength(-1);
				servletResponse.setCharacterEncoding("UTF-8");
				PrintWriter out = null;
				try {
					out = servletResponse.getWriter();
					out.write(result);
					out.flush();
				} finally {
					if (out != null) {
						out.close();
					}
				}
				LOG.info("response data:" + result);
			}
	        long endTime = System.currentTimeMillis();
	        long executeTime = (endTime - startTime);
	        LOG.info("run time:{} 毫秒", executeTime);
	        MDC.clear();
		}
	}
	private String getParamString(Map<String, String[]> map) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String[]> e : map.entrySet()) {
			sb.append(e.getKey()).append("=");
			String[] value = e.getValue();
			if (value != null && value.length == 1) {
				sb.append(value[0]).append("&");
			} else {
				sb.append(Arrays.toString(value)).append("&");
			}
		}
		return sb.toString();
	}
   /**
	 * 取用户信息
	 */
	private String getUser(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		JWTVerifier verifier = JWT.require(algorithm).withIssuer("MING").build();
		DecodedJWT jwt = verifier.verify(token);
		StringBuilder sb = new StringBuilder();
		sb.append(jwt.getClaim("userName").asString());
		sb.append(":");
		sb.append(jwt.getClaim("userId").asInt());		
		MDC.put("userName", jwt.getClaim("userName").asString());//用户名
        MDC.put("userId", String.valueOf(jwt.getClaim("userId").asInt()));//用户ID
		return sb.toString();
	}
	/**
	 * 获取请求方IP
	 *
	 * @return 客户端Ip
	 */
	private String getIpAddress(HttpServletRequest httpReq) {
		String xff = httpReq.getHeader("x-forwarded-for");
		if (xff == null) {
			return "8.8.8.8";
		}else{
			int len=15;
			if(xff.length() > len ){
				xff = xff.substring(0, 14).split(",")[0];
			}
		}
		return xff;
	}


	public void destroy() {
	}

}
