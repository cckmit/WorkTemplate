package com.blaze.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 简易业务工具类
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-17 18:05
 */
public class SimpleUtil {

    /**
     * 序列化版本号，用于比较
     *
     * @param version 版本号
     * @return 序列值
     */
    public static long convertVersion(String version) {
        String[] values = version.split("[.]");
        int len = values.length;
        long value = 0;
        for (int i = 0; i < values.length; i++) {
            value += Integer.parseInt(values[i]) << 4 * (len - i - 1);
        }
        return value;
    }

    /**
     * 解析Json数组，fastJson的解析效率有问题
     *
     * @param s 字符串
     * @return 数组
     */
    public static String[] parseJsonArray(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replace("[", "").replace("]", "").split(",");
        }
        return new String[0];
    }

}
