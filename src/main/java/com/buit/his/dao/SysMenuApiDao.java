package com.buit.his.dao;

import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysMenuApiDao {
    Integer getApiCount(@Param("uid") Integer uid, @Param("apiName")String apiName);
}
