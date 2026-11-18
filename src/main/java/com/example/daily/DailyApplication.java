package com.example.daily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/** Spring Boot 启动入口；启动时扫描当前包及子包中的组件。 */
@SpringBootApplication
public class DailyApplication {
    /** 启动 Spring 容器和内嵌 Web 服务器。 */
    public static void main(String[] args) { SpringApplication.run(DailyApplication.class, args); }

    @Bean
    InitializingBean databaseStartupCheck(DataSource dataSource) {
        return () -> {
            try (Connection connection = dataSource.getConnection()) {
                if (!connection.isValid(5)) {
                    throw new SQLException("数据库连接无效");
                }
            } catch (SQLException | RuntimeException ex) {
                throw new IllegalStateException("数据库连接失败，请检查数据库是否启动及连接配置", ex);
            }
        };
    }
}
