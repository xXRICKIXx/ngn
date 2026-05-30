package com.gs.ngn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NgnApplication {
    public static void main(String[] args) {
        SpringApplication.run(NgnApplication.class, args);
    }
}
