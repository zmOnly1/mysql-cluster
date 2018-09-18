package com.example.mysqlcluster.config;

/**
 * Created by zm on 2018/9/18.
 */
public class DBContextHolder {


    public static final String MASTER = "master";
    public static final String SLAVE = "slave";

    private static ThreadLocal<String> dataSource = new ThreadLocal<>();

    public static Object get() {
        return dataSource.get();
    }

    public static void set(String name) {
        dataSource.set(name);
    }
}
