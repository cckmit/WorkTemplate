package com.work;

import com.service.ThirdSendService;
import com.tool.ClassUtils;
import com.tool.Log4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author feng
 */
@WebListener
public class CmServletListener implements ServletContextListener {
    /**
     * 线程池
     */
    private static Logger LOG = LoggerFactory.getLogger(CmServletListener.class);
    /**
     * 线程池
     */
    public static ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(20, new BasicThreadFactory.Builder().namingPattern("worker-thread-%d").daemon(true).priority(Thread.MAX_PRIORITY).build());

    @Override
    public void contextDestroyed(ServletContextEvent arg) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg) {
        // 初始化用户信息
        //
        ThirdSendService.init();
    }
}
