package com.buit.config.annotation;

import java.lang.annotation.*;

/**
 * @Author sunjx
 * @Date 2020-12-14 15:36
 * @Description
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApi {

    String role();
}
