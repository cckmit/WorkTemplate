package com.blazefire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-09 13:15
 */
@SpringBootApplication
@EnableScheduling
@ServletComponentScan
public class FcAnalysisApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(FcAnalysisApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
