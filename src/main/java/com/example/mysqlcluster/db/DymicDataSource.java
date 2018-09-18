package com.example.mysqlcluster.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;

/**
 * Created by zm on 2018/9/18.
 */
@Primary
@Component
public class DymicDataSource extends AbstractRoutingDataSource {

    @Qualifier("master")
    @Autowired
    private DataSource dataSourceMaster;

    @Qualifier("slave")
    @Autowired
    private DataSource dataSourceSlave;

    @PostConstruct
    public void init() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBContextHolder.MASTER, dataSourceMaster);
        targetDataSources.put(DBContextHolder.SLAVE, dataSourceSlave);

        setTargetDataSources(targetDataSources);
        setDefaultTargetDataSource(dataSourceMaster);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.get();
    }
}
