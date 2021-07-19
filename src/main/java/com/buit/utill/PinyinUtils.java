package com.buit.utill;


import com.buit.utill.pinyin.PinYinHelper;

/**
 * @author 老花生
 */
public class PinyinUtils {

    /**
     * 获取全拼
     * @param sentence 字符串
     * @return
     */
    public static String getFullPinYin(String sentence ){
        return PinYinHelper.getInstance().getFullPinYin(sentence);
    }

    /**
     * 将输入字符串中的中文转换成对应拼音的首字符.
     *
     * @param sentence 待翻译的字符串
     * @return 翻译好的字符串
     */
    public static String getSimplePinYin(String sentence )
    {
        return PinYinHelper.getInstance().getSimplePinYin(sentence);
    }
}
