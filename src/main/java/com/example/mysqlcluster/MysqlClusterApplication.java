package com.example.mysqlcluster;

import com.example.mysqlcluster.db.DBContextHolder;
import com.example.mysqlcluster.mapper.AppMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.*;

@MapperScan("com.example.mysqlcluster.mapper")
@SpringBootApplication
public class MysqlClusterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MysqlClusterApplication.class, args);
        DBContextHolder.set(DBContextHolder.MASTER);
        AppMapper mapper = context.getBean(AppMapper.class);
        List<Map<String, Object>> maps = mapper.executeQuery("select * from user");
        System.out.println(maps);
        System.out.println("abccccccccccccc");
    }
}
