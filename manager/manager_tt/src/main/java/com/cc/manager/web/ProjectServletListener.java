package com.cc.manager.web;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-15 16:54
 */
@WebListener
public class ProjectServletListener implements ServletContextListener {

    /**
     * 初始化一个线程池
     */
    public static ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(20,
            new BasicThreadFactory.Builder().namingPattern("worker-thread-%d").daemon(true).priority(Thread.MAX_PRIORITY).build());

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("ProjectServletListener.contextInitialized~");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

}