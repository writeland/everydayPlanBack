package com.example.daily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Spring Boot 启动入口；启动时扫描当前包及子包中的组件。 */
@SpringBootApplication
public class DailyApplication {
    /** 启动 Spring 容器和内嵌 Web 服务器。 */
    public static void main(String[] args) { SpringApplication.run(DailyApplication.class, args); }
}
