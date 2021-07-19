/*
 * Copyright (c) 2019
 * User:mleo
 * File:BeanUtil.java
 * Date:2019/01/02 17:25:02
 */

package com.buit.utill;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;


/**
 * @author All
 */
public class BeanUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();


//    public static void setStopStatus(Object obj)  {
//        try {
//            PropertyDescriptor endDateProperty = new PropertyDescriptor("endDate",obj.getClass());
//            PropertyDescriptor stopStatusProperty = new PropertyDescriptor("stopStatus",obj.getClass());
//            Timestamp timestamp = (Timestamp) endDateProperty.getReadMethod().invoke(obj);
//            stopStatusProperty.getWriteMethod().invoke(obj,DateUtil.compareTo(timestamp));
//        }catch (Exception e){
//            e.printStackTrace();
//            BaseException.createByMessage(e.getMessage());
//        }
//    }
//
//    public static <T> void setStopStatus(List<T> list){
//       if(CollectionUtils.isEmpty(list)){
//           return;
//       }
//       list.forEach(item ->{
//           setStopStatus(item);
//       });
//    }

//    public static <T> void setStopStatus(PageInfo<T> page){
//        setStopStatus(page.getList());
//    }

    /**
     * 将对象转换为新对象
     *
     * @param object 原对象
     * @param t      新对象类型
     * @param <T>    新类型
     * @return 新对象
     */
    public static <T> T toBean(Object object, T t) {
        if (null == object) {
            return null;
        }
        BeanUtils.copyProperties(object, t);
        return t;
    }

    /**
     * 将对象转换为新对象
     *
     * @param object 原对象
     * @param clazz  新对象类型
     * @param <T>    新类型
     * @return 新对象
     */
    public static <T> T toBean(Object object, Class<T> clazz) {
        if (null == object) {
            return null;
        }
        T t = BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(object, t);
        return t;
    }

    /**
     * 将对象集合转换为新对象集合
     *
     * @param <T>   新类型
     * @param list  对象集合
     * @param clazz 新对象Class
     * @return 新对象集合
     */
    public static <T> List<T> toBeans(List list, Class<T> clazz) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }
        List<T> results = new ArrayList<>(list.size());
        list.forEach(item -> {
            T response = BeanUtil.toBean(item, clazz);
            results.add(response);
        });
        return results;
    }

    /**
     * 分页对象转换
     *
     * @param page  分页对象
     * @param clazz 新对象Class
     * @param <T>   新类型
     * @return 新分页对象
     */
    public static <T> PageInfo<T> toPage(PageInfo page, Class<T> clazz) {
        page.setList(toBeans(page.getList(), clazz));
        return page;
    }

    /**
     * 将任意类型转换成字符串
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String beanToString(T value) {
        ObjectMapper objectMapper = SpringContextUtil.getApplicationContext().getBean(ObjectMapper.class);
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return value + "";
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return value + "";
        } else {
            String aa = null;
            try {
                aa = objectMapper.writeValueAsString(value);
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
            return aa;
        }
    }

    /**
     * 把一个字符串转换成bean对象
     *
     * @param str
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String str, Class<T> clazz) {
        ObjectMapper objectMapper = SpringContextUtil.getApplicationContext().getBean(ObjectMapper.class);
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return objectMapper.convertValue(str, clazz);
        }
    }

    /**
     * 将对象转成字符串
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String objectToString(Object obj) throws Exception {
        return MAPPER.writeValueAsString(obj);
    }

    /**
     * 将Bean转成Map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map beanToMap(Object obj) throws Exception {
        return MAPPER.readValue(objectToString(obj), Map.class);
    }

    /**
     * 将Bean转成Map 去除bean对象中value为空的字段
     *
     * @param requestParameters
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMap(Object requestParameters) throws IllegalAccessException {
    	Field[] fields = requestParameters.getClass().getDeclaredFields();
        Map<String, String> map = new HashMap<>(fields.length);
        // 获取f对象对应类中的所有属性域
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            fields[i].setAccessible(true);
            // 获取在对象f中属性fields[i]对应的对象中的变量
            Object o = fields[i].get(requestParameters);
            if (o != null && StringUtils.isNotBlank(o.toString().trim())) {
                map.put(varName, o.toString().trim());
            }
        }
        return map;
    }

}
