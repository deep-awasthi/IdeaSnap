package com.ideasnap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IdeaSnapApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdeaSnapApplication.class, args);
    }
}
