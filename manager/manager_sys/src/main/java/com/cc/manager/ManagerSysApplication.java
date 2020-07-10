package com.cc.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author cc
 */
@ServletComponentScan
//@MapperScan("com.cc.manager.modules.sys.mapper")
@SpringBootApplication
public class ManagerSysApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ManagerSysApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
