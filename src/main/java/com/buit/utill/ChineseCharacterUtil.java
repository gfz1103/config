package com.buit.utill;

import java.lang.reflect.Method;

import com.buit.commons.BaseException;

/**
 * 汉字转拼音、五笔公共方法
 * @author laohu
 *
 */
public class ChineseCharacterUtil {

	/**
     * 设置对象的拼音、五笔编码值
     * @param obj	需要转拼音、五笔的对象
     * @param name	需要转音频、五笔的汉字
     */
    public static void setPyAndWb(Object obj, String name) {
        try {
        	String personNamePinyin = PinyinUtils.getSimplePinYin(name);
    		String personNameWubi = WubiUtils.getSimpleWuBi(name);
    		
			Method pyMethod = obj.getClass().getMethod("setPyCode", String.class);
			Method wbMethod = obj.getClass().getMethod("setWbCode", String.class);
			
			pyMethod.invoke(obj, personNamePinyin);
			wbMethod.invoke(obj, personNameWubi);
			
		} catch (Exception e) {
			throw BaseException.create("ERROR_USER_0012！");
		}
    }
    public static void main(String[] args) {
    	String name="手工输入零售价";
    	String personNamePinyin = PinyinUtils.getSimplePinYin(name);
		String personNameWubi = WubiUtils.getSimpleWuBi(name);
		
		System.out.println("拼音==="+personNamePinyin);
		System.out.println("五笔==="+personNameWubi);
	}
}
