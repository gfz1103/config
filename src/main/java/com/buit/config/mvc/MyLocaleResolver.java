package com.buit.config.mvc;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.LocaleResolver;

/**
* @ClassName: MyLocaleResolver
* @Description: 国际化配置
* @author 神算子
* @date 2020年4月26日 下午3:41:17
*
 */
public class  MyLocaleResolver implements LocaleResolver {
    private static final String LANG = "lang";
    private static final String LANG_SESSION = "lang_session";
 
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(LANG);
        Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        if (lang != null && lang != ""){
            String[] langueage = lang.split("_");
            locale = new Locale(langueage[0],langueage[1]);
            HttpSession session = request.getSession();
            session.setAttribute(LANG_SESSION,locale);
        }
        else{
            HttpSession session = request.getSession();
            //本session以前某次设置的国际化
            Locale localeInSession = (Locale) session.getAttribute(LANG_SESSION);
            if (localeInSession != null){
                locale = localeInSession;
            }
        }
        return locale;
    }
 
    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
 
    }
}
