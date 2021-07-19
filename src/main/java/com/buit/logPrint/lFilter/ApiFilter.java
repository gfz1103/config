package com.buit.logPrint.lFilter;
import com.buit.commons.BaseException;
import com.buit.commons.SysUser;
import com.buit.his.dao.SysMenuApiDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author zhanggy
 * @title: Filter
 * @description: TODO
 * @date 2021/4/914:40
 */
@Component
public class ApiFilter implements Filter{
    private static final String DEVELOP_ENVIRONMENT = "dev";
    private static final String ACTIVE = "knife4j.production";
    @Autowired
    private Environment env;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SysMenuApiDao sysMenuDao;
    @Autowired
    private ObjectMapper objectMapper;

    //添加不需要拦截的接口
    private static final Set<String> ALLOWED_PATHS = new HashSet<>();
    static {
        ALLOWED_PATHS.add("/ex/");
        ALLOWED_PATHS.add("sysusermenu/");
        ALLOWED_PATHS.add("/sysusergroup/loginGroup");
        ALLOWED_PATHS.add("/sysuser/submitUser");
        ALLOWED_PATHS.add("sysmenu/");
        ALLOWED_PATHS.add("/sys/sysprimarydata/getAllMainDic");
        ALLOWED_PATHS.add("list/");
        ALLOWED_PATHS.add("pacs/");
        ALLOWED_PATHS.add("/sysrourt/findList");
        ALLOWED_PATHS.add("/dicywlb/getAuthority");//查询权限列表
        ALLOWED_PATHS.add("/dicywlb/getmorenAuthority");//查询用户默认权限
        ALLOWED_PATHS.add("/messjsr/messList");//当前登录用户消息列表
        ALLOWED_PATHS.add("/opghks/getOneByEntity");//查询挂号科室
        ALLOWED_PATHS.add("/sysuser/updataPassWord");//修改密码
        ALLOWED_PATHS.add("/webjars");
        ALLOWED_PATHS.add("/doc.html");
        ALLOWED_PATHS.add("/swagger-resources");
        ALLOWED_PATHS.add("/v2/api-docs");
        ALLOWED_PATHS.add("/sysuser/changeBid");

    }

    @Override
    public void init(FilterConfig filterConfig) {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)throws IOException, ServletException{
        HttpServletRequest httpReq = (HttpServletRequest)servletRequest;
        //获取url路径
        getUrl(httpReq);
        //Objects.requireNonNull(apiFilter.env.getProperty(ACTIVE)).startsWith(DEVELOP_ENVIRONMENT);
        if(env.getProperty(ACTIVE)==null||env.getProperty(ACTIVE).equals("false") || allowedPath(httpReq.getRequestURI())){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            String token = httpReq.getHeader("token");
            String refreshTokenKey = String.format("JWT_TOKEN::%s", token);
            SysUser loginUser=getValue(refreshTokenKey, SysUser.class);
            if(null==token || null==loginUser){
                //如果token为空则直接拦截
                throw BaseException.createByMessage("用户查询信息失败");
            }
            //根据token和url地址判断是否有查看数据权限
            Integer num=sysMenuDao.getApiCount(loginUser.getUserId(),getUrl(httpReq));
            if(num==0){
                //如果不在查看的权限内 则直接抛出
                throw BaseException.createByMessage("用户查询信息失败");
            }else{
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
    }
    @Override
    public void destroy() {
    }

    /**
     * 判断接口是否在不拦截的里面
     * @param requestURI
     * @return
     */
    private boolean allowedPath(String requestURI) {
        for (String ignoreUrl : ALLOWED_PATHS) {
            if(requestURI.contains(ignoreUrl)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取uid
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getValue(String key,Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        try {
            if(StringUtils.isEmpty(json)){
                return null;
            }
            return objectMapper.readValue(json,clazz);
        } catch (IOException e) {
            throw BaseException.createByMessage("json转对象失败");
        }
    }

    /**
     * 获取路径
     * @param httpReq
     * @return
     */
    public String getUrl(HttpServletRequest httpReq){
        String path = httpReq.getRequestURI();
        if(httpReq.getMethod().equals("GET")){
            String url = httpReq.getRequestURI();
            String regEx="[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(url);
            path= url.replace(m.replaceAll("").trim(),"");
        }
        return path;
    }

}
