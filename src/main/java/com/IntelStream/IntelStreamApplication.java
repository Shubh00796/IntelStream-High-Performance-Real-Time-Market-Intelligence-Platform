package com.IntelStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IntelStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelStreamApplication.class, args);
    }
}
