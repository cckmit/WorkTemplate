package com.blaze;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 服务器监听
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 21:40
 */
@Component
@WebListener
public class GameServiceListener implements ServletContextListener {

    /**
     * 初始化一个线程池
     */
    public static ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(20,
            new BasicThreadFactory.Builder().namingPattern("worker-thread-%d").daemon(true).priority(
                    Thread.MAX_PRIORITY).build());

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("ProjectServletListener.contextInitialized~");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("ProjectServletListener.contextDestroyed~");
    }


}
