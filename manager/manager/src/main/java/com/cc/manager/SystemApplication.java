package com.cc.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author cc
 */
@SpringBootApplication
public class SystemApplication {

    public static void main(String[] args) {

//            System.setProperty("spring.devtools.restart.enabled", "false");
            SpringApplication.run(SystemApplication.class, args);
    }

}
