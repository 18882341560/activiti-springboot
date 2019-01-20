package com.gelin.activitispringboot;

import com.alibaba.druid.pool.DruidDataSource;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("com.gelin.activitispringboot.dao")
@EnableTransactionManagement
public class ActivitiSpringbootApplication {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource=new DruidDataSource();
        druidDataSource.setDbType("com.alibaba.druid.pool.DruidDataSource");
        druidDataSource.setInitialSize(10);
        druidDataSource.setMinIdle(10);
        druidDataSource.setMaxActive(200);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("SELECT 1 FROM DUAL");
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        druidDataSource.setFilters("stat,log4j");
        druidDataSource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
        return druidDataSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(ActivitiSpringbootApplication.class, args);
    }
}
