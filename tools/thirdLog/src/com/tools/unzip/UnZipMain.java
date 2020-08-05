package com.tools.unzip;

import com.tools.log4j.Log4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * 解压文件到指定文件夹中
 *
 * @author xuweihua
 */
public class UnZipMain {
    ZipFileReadInterface zipFileRead;

    public UnZipMain(ZipFileReadInterface readLog) {
        this.zipFileRead = readLog;
    }

    /**
     * 依次读取压缩包中各文件内容
     *
     * @param file 压缩文件
     */
    public void display(File file) throws IOException {
        long starttime = System.currentTimeMillis();
        try {
            // 由指定的File对象打开供阅读的ZIP文件
            ZipFile zip = new ZipFile(file);
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip
                    .entries();
            while (entries.hasMoreElements()) {// 依次访问各条目
                ZipEntry ze = entries.nextElement();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        zip.getInputStream(ze)));
                System.out.println("\n" + ze.getName() + ":");
                String line;
                while ((line = br.readLine()) != null) {
                    zipFileRead.readLogLine(line);
                }
                br.close();
            }
            zip.close();
//            // 获取zip文件中的各条目（子文件）
//            zip.stream().forEach(ze ->
//            {
//                try
//                {
//                    System.out.println("\n" + ze.getName() + ":");
//                    BufferedReader br = new BufferedReader(new InputStreamReader(zip.getInputStream(ze)));
//                    String line;
//                    while ((line = br.readLine()) != null)
//                    {
//                        zipFileRead.readLogLine(line);
//                    }
//                    br.close();
//                } catch (Exception e)
//                {
//                    Log4j.NAME.EXCEPTION_LOG.error(Log4j.getExceptionInfo(e));
//                }
//
//            });
//            zip.close();
        } catch (ZipException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            Log4j.NAME.EXCEPTION_LOG.info("解析" + file.getPath() + "完成！耗时:" + (System.currentTimeMillis() - starttime) + "ms!");
            zipFileRead.readFileFinish();
        }
    }

}
