package com.IntelStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@EnableJpaAuditing
@EnableCaching
@ComponentScan(basePackages = "com.IntelStream")
public class IntelStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelStreamApplication.class, args);
    }
}
