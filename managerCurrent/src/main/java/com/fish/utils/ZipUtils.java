package com.fish.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @Description TODO
 * @author Host-0311
 * @date 2019年5月21日 下午1:00:26
 * @version V 1.0
 */
public class ZipUtils {
    /** 缓冲器大小 */
    private static final int BUFFER = 512;


    private ZipUtils() {

    }

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath :压缩后存放路径
     * @param fileName :压缩后文件的名称
     * @return
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        if (sourceFile.exists() == false) {
            System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath + " 不存在. <<<<<<");
            flag = false;
            return flag;
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    zipFile.delete();
                }
                File[] sourceFiles = sourceFile.listFiles();
                if (null == sourceFiles || sourceFiles.length < 1) {
                    System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath + " 里面不存在文件,无需压缩. <<<<<<");
                    flag = false;
                    return flag;
                } else {
                    ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
                    // 用到时才申明，否则容易出现问题，记得先开后关，后开先关
                    byte[] bufs = new byte[1024 * 10]; // 缓冲块
                    for (int i = 0; i < sourceFiles.length; i++) {
                        // 创建ZIP实体,并添加进压缩包
                        ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                        zos.putNextEntry(zipEntry);
                        // 读取待压缩的文件并写进压缩包里
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFiles[i]),
                                1024 * 10);
                        // 用到时才申明，否则容易出现问题，记得先开后关，后开先关
                        int read = 0;
                        while ((read = (bis.read(bufs, 0, 1024 * 10))) != -1) {
                            zos.write(bufs, 0, read);
                        }
                        if (null != bis)
                            bis.close(); // 关闭
                    }
                    flag = true;
                    if (null != zos)
                        zos.close(); // 关闭
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        return flag;
    }

    /**
     *@param destPath 解压目标路径
     *@param fileName 解压文件的相对路径
     * */
    public static File createFile(String destPath, String fileName){

        String[] dirs = fileName.split("/");//将文件名的各级目录分解
        File file = new File(destPath);

        if (dirs.length > 1) {//文件有上级目录
            for (int i = 0; i < dirs.length - 1; i++) {
                file = new File(file, dirs[i]);//依次创建文件对象知道文件的上一级目录
            }

            if (!file.exists()) {
                file.mkdirs();//文件对应目录若不存在，则创建
                try {
                    System.out.println("mkdirs: " + file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            file = new File(file, dirs[dirs.length - 1]);//创建文件

            return file;
        } else {
            if (!file.exists()) {//若目标路径的目录不存在，则创建
                file.mkdirs();
                try {
                    System.out.println("mkdirs: " + file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            file = new File(file, dirs[0]);//创建文件

            return file;
        }

    }


    /**
     * 解压
     * */
    public static void decompress(String zipFileName,String destPath){

        try {
            System.out.println(zipFileName);
            ZipInputStream zis=new ZipInputStream(new FileInputStream(zipFileName));
            ZipEntry       zipEntry       = null;
            byte[]         buffer         = new byte[BUFFER];//缓冲器
            int            readLength     = 0;//每次读出来的长度
            while ((zipEntry=zis.getNextEntry())!=null){
                if(zipEntry.isDirectory()){//若是目录
                    File file=new File(destPath+"/"+zipEntry.getName());
                    if(!file.exists()){
                        file.mkdirs();
                        System.out.println("mkdirs:"+file.getCanonicalPath());
                        continue;
                    }
                }//若是文件
                File file = createFile(destPath,zipEntry.getName());
                System.out.println("file created: " + file.getCanonicalPath());
                OutputStream os=new FileOutputStream(file);
                while ((readLength=zis.read(buffer,0,BUFFER))!=-1){
                    os.write(buffer,0,readLength);
                }
                os.close();
                System.out.println("file uncompressed: " + file.getCanonicalPath());
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //String sourceFilePath = "D:\\codId\\111";
        String zipFilePath = "d:\\ccc";
        String fileName = "c:\\Users\\ASUS\\Desktop\\kf_2020022.zip";
	/*boolean flag = ZipUtils.fileToZip(sourceFilePath, zipFilePath, fileName);
	if (flag) {
	    System.out.println("文件打包成功!");
	} else {
	    System.out.println("文件打包失败!");
	}*/

        decompress(fileName,zipFilePath);
    }

}
