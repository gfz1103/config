package sqlt;


import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Ognl工具类，主要是为了在ognl表达式访问静态方法时可以减少长长的类名称编写
 * Ognl访问静态方法的表达式为: @class@method(args)
 * 
 * 示例使用: 
 * <pre>
 * 	&lt;if test="@Ognl@isNotEmpty(userId)">
 *		and user_id = #{userId}
 *	&lt;/if>
 * </pre>
 * @author wengl
 *
 */
public class Ognl {

static String reg = "(?:')|(?:--)|(?:#)|(\\*)|(\\b(select|select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
 
static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);//表示忽略大小写
 
/***************************************************************************
 * 参数校验
 */
public static boolean isSqlValid(String str) {
    Matcher matcher = sqlPattern.matcher(str);
    if (matcher.find()) {
        System.out.println("参数存在非法字符，请确认："+matcher.group());//获取非法字符：or
        return true;
    }
    return false;
}
public static boolean orderIsNotEmpty(Object o) {
    if(o == null) return false;
    if(o instanceof String) {
        String tempc=(String)o;
        if(tempc.length() == 0){
            return false;
        }else {
            if(isSqlValid(tempc)) {
                return false;
            }
            return true;
        }
    }
    return false;
    
}
	
	/**
	 * 可以用于判断String,Map,Collection,Array是否为空
	 * @param o
	 * @return
	 */
	public static boolean isEmpty(Object o)  {
		if(o == null) return true;

		if(o instanceof String) {
			if(((String)o).length() == 0){
				return true;
			}
		} else if(o instanceof Collection) {
			if(((Collection)o).isEmpty()){
				return true;
			}
		} else if(o.getClass().isArray()) {
			if(Array.getLength(o) == 0){
				return true;
			}
		} else if(o instanceof Map) {
			if(((Map)o).isEmpty()){
				return true;
			}
		}else {
			return false;
		}
		return false;
	}
	
	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * @param c
	 * @return
	 */	
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}
	
	public static boolean isNotBlank(Object o) {
		return !isBlank(o);
	}
	
	public static boolean isNumber(Object o) {
		if(o == null) return false;
		if(o instanceof Number) {
			return true;
		}
		if(o instanceof String) {
			String str = (String)o;
			if(str.length() == 0) return false;
			if(str.trim().length() == 0) return false;
			
			try {
				Double.parseDouble(str);
				return true;
			}catch(NumberFormatException e) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isBlank(Object o) {
		if(o == null)
			return true;
		if(o instanceof String) {
			String str = (String)o;
			return isBlank(str);
		}
		return false;
	}

	public static boolean isBlank(String str) {
		if(str == null || str.length() == 0) {
			return true;
		}
		
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
