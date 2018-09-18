package com.example.mysqlcluster.config;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by zm on 2018/9/18.
 */
@Aspect
@Component
public class DataSourceAspect {

    @Pointcut("execution(public * com.example.mysqlcluster.mapper..*.*(..))")
    public void datasourcePointcut() {
    }

    @Before("datasourcePointcut()")
    public void before(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Datasource datasource = method.getAnnotation(Datasource.class);

        String datasourceKey = datasource.value();

        DBContextHolder.set(datasourceKey);
    }

}
