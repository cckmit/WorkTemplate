package com.blazefire.config;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-13 13:32
 */
@WebListener
public class ProjectServletListener implements ServletContextListener {

    /**
     * 线程池
     */
    public static ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(20,
            new BasicThreadFactory.Builder().namingPattern("worker-thread-%d").daemon(true).priority(Thread.MAX_PRIORITY).build());

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
