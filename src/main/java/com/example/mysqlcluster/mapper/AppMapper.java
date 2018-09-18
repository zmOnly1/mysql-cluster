package com.example.mysqlcluster.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * Created by zm on 2018/9/18.
 */
public interface AppMapper {

    List<Map<String, Object>> executeQuery(@Param("sql")String sql);
}
