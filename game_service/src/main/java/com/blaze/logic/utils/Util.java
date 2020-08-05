package com.blaze.logic.utils;

/**
 * 业务工具类
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-17 18:05
 */
public class Util {

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

}
