package com.chikacow.kohimana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KohimanaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KohimanaApplication.class, args);
    }

}
