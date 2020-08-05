package com.blaze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author cc
 */
@SpringBootApplication
@ServletComponentScan
public class GameServiceApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(GameServiceApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
