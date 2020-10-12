package com.saebyeok.saebyeok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SaebyeokApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaebyeokApplication.class, args);
    }

}
