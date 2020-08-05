package com.config;

import com.tool.Log4j;
import org.apache.commons.beanutils.converters.NumberConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件读取
 *
 * @author xuwei
 */
public class ReadConfig {
    /**
     * 配置文件路径
     */
    private static final String CONFIG_FILE_NAME = "config.properties";

    /**
     * 唯一事例
     */
    private static ReadConfig instance;
    /**
     * 配置缓存
     */
    private Map<String, String> configMap;

    private static final Logger LOG = LoggerFactory.getLogger(ReadConfig.class);

    static {
        init();
    }

    /**
     * 初始化配置
     */
    public static void init() {
        instance = new ReadConfig();
    }

    private ReadConfig() {
        configMap = new HashMap<>();
        try {
            InputStream in = ReadConfig.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
            Properties properties = new Properties();
            properties.load(in);
            for (Object key : properties.keySet()) {
                configMap.put((String) key, properties.getProperty((String) key));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行匹配KEY值
     */
    public static boolean containsKey(String key) {
        return instance.configMap.containsKey(key);
    }

    /**
     * 获取values值
     */
    public static String get(String key) {
        return instance.configMap.get(key);
    }

    /**
     * 获取数字
     *
     * @param key 关键字
     * @return 结果
     */
    public static long getLong(String key) {
        try {
            if (instance.configMap.containsKey(key)) {
                return Long.parseLong(instance.configMap.get(key).trim());
            }
        } catch (Exception e) {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return 0L;
    }

    /**
     * 获取状态判断
     *
     * @param key 关键字
     * @return 状态
     */
    public static boolean getBoolean(String key) {
        try {
            if (instance.configMap.containsKey(key)) {
                return Boolean.parseBoolean(instance.configMap.get(key).trim());
            }
        } catch (Exception e) {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return false;
    }
}
