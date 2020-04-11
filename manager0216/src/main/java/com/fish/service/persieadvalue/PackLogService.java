package com.fish.service.persieadvalue;

import com.fish.utils.GetFileName;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author cf
 * @date
 */
@Service
public class PackLogService {
    public static void main(String[] args) {
        File[] srcFiles = {new File("E:\\a.zip"), new File("E:\\b.zip")};
        File zipFile = new File("E:\\ZipFile.zip");
        packLog("E:\\", "2020041010", "2020041013");
    }

    public static void packLog(String logPath, String startTime, String endTime) {
        String start = startTime.replace("-", "").replace(" ", "");
        String end = endTime.replace("-", "").replace(" ", "");
        System.out.println("--------" + startTime + "------" + endTime);
        //创建新的zip位置名称
        File zipFile = new File("E:\\ZipFile.zip");
        List<File> file = GetFileName.getFile(logPath, startTime, endTime);
        // 调用压缩方法
        zipFiles(file, zipFile);
    }

    private static void zipFiles(List<File> srcFiles, File zipFile) {
        // 判断压缩后的文件存在不，不存在则创建
        if (!zipFile.exists()) {
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 创建 FileOutputStream 对象
        FileOutputStream fileOutputStream = null;
        // 创建 ZipOutputStream
        ZipOutputStream zipOutputStream = null;
        // 创建 FileInputStream 对象
        FileInputStream fileInputStream = null;

        try {
            // 实例化 FileOutputStream 对象
            fileOutputStream = new FileOutputStream(zipFile);
            // 实例化 ZipOutputStream 对象
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            // 创建 ZipEntry 对象
            ZipEntry zipEntry = null;
            // 遍历源文件数组
            for (File srcFile : srcFiles) {
                // 将源文件数组中的当前文件读入 FileInputStream 流中
                fileInputStream = new FileInputStream(srcFile);
                // 实例化 ZipEntry 对象，源文件数组中的当前文件
                zipEntry = new ZipEntry(srcFile.getName());
                zipOutputStream.putNextEntry(zipEntry);
                // 该变量记录每次真正读的字节个数
                int len;
                // 定义每次读取的字节数组
                byte[] buffer = new byte[1024];
                while ((len = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
