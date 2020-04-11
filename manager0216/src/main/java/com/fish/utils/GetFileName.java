package com.fish.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: CF
 * @date 2020/4/10 16:13
 */
public class GetFileName {
    /**
     * 获取某个文件夹下的所有文件
     *
     * @param path 文件夹的路径
     * @return
     */
    public static ArrayList<String> getAllFileName(String path) {
        ArrayList<String> logName = new ArrayList<>();
        File file = new File(path);
        File[] fs = file.listFiles();
        assert fs != null;
        for (File f : fs) {
            if (!f.isDirectory() && f.getName().toLowerCase().endsWith(".zip")) {
                logName.add(f.getName());
            }
        }
        return logName;
    }


    public static List<File> getFile(String path, String start, String end) {
        int startTime = Integer.parseInt(start);
        int endTime = Integer.parseInt(end);
        //获取文件夹下的日志名称
        ArrayList<String> allFileName = getAllFileName(path);
        //筛选复合时间的日志文件名称
        List<File> fileList = new ArrayList<File>();
        for (String name : allFileName) {
            int logTime = Integer.parseInt(name.substring(5, 15));
            // int logTime = Integer.parseInt(name.substring(5, 12));
            if (startTime <= logTime && logTime <= endTime) {
                fileList.add(new File(path + name));
            }
        }
        return fileList;
    }

    public static void main(String[] args) {
        String logPath = "";
        int start = Integer.parseInt("2020031220");
        int end = Integer.parseInt("2020031220");
        File[] srcFiles = {new File("E:\\a.zip"), new File("E:\\b.zip")};
        File zipFile = new File("E:\\ZipFile.zip");
        //获取文件夹下的日志名称
        ArrayList<String> allFileName = getAllFileName("E:\\");
        //筛选复合时间的日志文件名称
        List<File> fileList = new ArrayList<File>();
        for (String name : allFileName) {
            int logTime = Integer.parseInt(name.substring(5, 15));
            if (start < logTime && logTime < end) {
                fileList.add(new File(logPath + name));
            }
        }

    }
}
