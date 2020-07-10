package com.cc.manager;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-18 10:46
 */
public class ServletInitializer extends SpringBootServletInitializer implements WebApplicationInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ManagerSysApplication.class);
    }

}
