package com.example.mysqlcluster.mapper;

import com.example.mysqlcluster.config.DBContextHolder;
import com.example.mysqlcluster.config.Datasource;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * Created by zm on 2018/9/18.
 */
public interface AppMapper {

    @Datasource(DBContextHolder.MASTER)
    List<Map<String, Object>> executeQuery(@Param("sql")String sql);

    @Datasource(DBContextHolder.SLAVE)
    List<Map<String, Object>> executeQuery2(@Param("sql")String sql);
}
