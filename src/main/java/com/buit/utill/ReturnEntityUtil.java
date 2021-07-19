package com.buit.utill;

/**
 * @author All
 */
public class ReturnEntityUtil {

    public static <T> ReturnEntity<T> success(T t) {
        ReturnEntity<T> result = new ReturnEntity<>();
        result.setData(t);
        return result;
    }

    public static <T> ReturnEntity<T> success() {
        return success(null);
    }
    /**
	 * 保留原因:远程调用时(RPC,http等),对错误二次封装时使用.
	 * 少数情况下使用
	 */
    public static <T> ReturnEntity<T> error(String code, String msg) {
        ReturnEntity<T> result = new ReturnEntity<>();
        result.setError(code, msg,null);
        return result;
    }
    /**
   	 * 大多数使用情况
   	 */
    public static <T> ReturnEntity<T> error(String errorCode) {
        ReturnEntity<T> result = new ReturnEntity<>();
        result.setError(errorCode,null);
        return result;
    }
    /**
   	 * 变量替换的错误码
   	 * 定义:login.fail=登录{1}失败{0}
   	 * 传参: Object[] par=new Object[]{"第一个","第二个"};
   	 * 输出:登录第二个失败第一个
   	 */
    public static <T> ReturnEntity<T> error(String errorCode,Object[] args) {
        ReturnEntity<T> result = new ReturnEntity<>();
        if(args == null || args.length==0) {
        	result.setError(errorCode,null);
        }else {
        	result.setError(errorCode,args);	
        }
        return result;
    }
    /**
   	 * 业务操作成功.但有信息需要提示给前台
   	 * 定义:successCode.fail=登录{1}失败{0}
   	 * 传参: Object[] par=new Object[]{"第一个","第二个"};
   	 * 输出:登录第二个失败第一个
   	 */
    public static <T> ReturnEntity<T> success(String successCode,Object[] args) {
        ReturnEntity<T> result = new ReturnEntity<>();
        result.setSuccess(successCode, args);
        return result;
    }

    public static Boolean isSuccess(ReturnEntity entity){
        return "SUCCESS".equals(entity.getErrorCode());
    }

    public static Boolean isFailed(ReturnEntity entity){
        return !"SUCCESS".equals(entity.getErrorCode());
    }

}
