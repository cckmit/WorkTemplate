package org.blaze.file.system.utils;

import org.blaze.file.system.utils.ToolsException.ToolsExceptionConstant;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * zip压缩
 *
 * @author xuwei
 */
@Component
public class ZipUtils {
    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @param fileName       :压缩后文件的名称
     * @return 是否壓縮
     */
    public static boolean folderToZip(String sourceFilePath, String zipFilePath, String fileName) throws ToolsException {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (!sourceFile.exists()) {
            System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
            throw new ToolsException(ToolsExceptionConstant.NOTEXSITERROR_CODE,
                    String.format(ToolsExceptionConstant.NOTEXSITERROR_MSG, sourceFilePath));
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
                    throw new ToolsException(ToolsExceptionConstant.EXSITERROR_CODE,
                            String.format(ToolsExceptionConstant.EXSITERROR_MSG, zipFilePath, fileName + ".zip"));
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if (null == sourceFiles || sourceFiles.length < 1) {
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                        throw new ToolsException(ToolsExceptionConstant.EMPTYERROR_CODE,
                                String.format(ToolsExceptionConstant.EMPTYERROR_MSG, sourceFilePath));
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024 * 10];
                        for (File file : sourceFiles) {
                            // 创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(file.getName());
                            zos.putNextEntry(zipEntry);
                            // 读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(file);
                            bis = new BufferedInputStream(fis, 1024 * 10);
                            int read = 0;
                            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                        }
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                // 关闭流
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != zos) {
                        zos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将sourceFilePath文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @param fileName       :压缩后文件的名称
     * @return
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) throws ToolsException {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        if (!sourceFile.exists()) {
            System.out.println("待压缩的文件：" + sourceFilePath + "不存在.");
            throw new ToolsException(ToolsExceptionConstant.NOTEXSITERROR_CODE,
                    String.format(ToolsExceptionConstant.NOTEXSITERROR_MSG, sourceFilePath));
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
                    throw new ToolsException(ToolsExceptionConstant.EXSITERROR_CODE,
                            String.format(ToolsExceptionConstant.EXSITERROR_MSG, zipFilePath, fileName + ".zip"));
                } else {
                    fos = new FileOutputStream(zipFile);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    byte[] bufs = new byte[1024 * 10];
                    // 创建ZIP实体，并添加进压缩包
                    ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
                    zos.putNextEntry(zipEntry);
                    // 读取待压缩的文件并写进压缩包里
                    fis = new FileInputStream(sourceFile);
                    bis = new BufferedInputStream(fis, 1024 * 10);
                    int read = 0;
                    while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                        zos.write(bufs, 0, read);
                    }
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                // 关闭流
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != zos) {
                        zos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将流的内容打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param fis            :待压缩的文件路径
     * @param streamfilename :压缩后存放路径
     * @param zipFilePath    zip文件路径
     * @param fileName       :压缩后文件的名称
     * @return
     */
    public static boolean streamToZip(InputStream fis, String streamfilename, String zipFilePath, String fileName) {
        boolean flag = false;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
            if (zipFile.exists()) {
                System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
                throw new ToolsException(ToolsExceptionConstant.EXSITERROR_CODE,
                        String.format(ToolsExceptionConstant.EXSITERROR_MSG, zipFilePath, fileName + ".zip"));
            } else {
                fos = new FileOutputStream(zipFile);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                byte[] bufs = new byte[1024 * 10];
                // 创建ZIP实体，并添加进压缩包
                ZipEntry zipEntry = new ZipEntry(streamfilename);
                zos.putNextEntry(zipEntry);
                // 读取待压缩的文件并写进压缩包里
                bis = new BufferedInputStream(fis, 1024 * 10);
                int read = 0;
                while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                    zos.write(bufs, 0, read);
                }
                flag = true;
            }
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ToolsException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != zos) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 将流转成zip文件输出
     *
     * @param inputstream    文件流
     * @param streamfilename 流文件的名称
     * @param fileName       zip包的名称
     * @param response
     * @return
     */
    public static boolean streamToZipStream(InputStream inputstream, String streamfilename, String fileName,
                                            HttpServletResponse response) {
        boolean flag = false;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            response.reset();
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("GB2312"), StandardCharsets.ISO_8859_1));
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            zos = new ZipOutputStream(out);
            byte[] bufs = new byte[1024 * 10];
            // 创建ZIP实体，并添加进压缩包
            ZipEntry zipEntry = new ZipEntry(streamfilename);
            zos.putNextEntry(zipEntry);
            // 读取待压缩的文件并写进压缩包里
            bis = new BufferedInputStream(inputstream, 1024 * 10);
            int read = 0;
            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                zos.write(bufs, 0, read);
            }
            flag = true;
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // 关闭流
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != zos) {
                    zos.close();
                }
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 将多个流转成zip文件输出
     *
     * @param listStream 文件流实体类对象
     * @param fileName   zip包的名称
     * @param response
     * @return
     */
    public static boolean listStreamToZipStream(List<ZipStreamEntity> listStream, String fileName, HttpServletResponse response) {
        boolean flag = false;
        BufferedInputStream bis = null;
        ZipOutputStream zos = null;
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            response.reset();
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("GB2312"), StandardCharsets.ISO_8859_1));
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            zos = new ZipOutputStream(out);
            byte[] bufs = new byte[1024 * 10];
            for (ZipStreamEntity zipstream : listStream) {
                String streamfilename = zipstream.getName();
                // 创建ZIP实体，并添加进压缩包
                ZipEntry zipEntry = new ZipEntry(streamfilename);
                zos.putNextEntry(zipEntry);
                // 读取待压缩的文件并写进压缩包里
                bis = new BufferedInputStream(zipstream.getInputstream(), 1024 * 10);
                int read = 0;
                while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                    zos.write(bufs, 0, read);
                }
            }
            flag = true;
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // 关闭流
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != zos) {
                    zos.close();
                }
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

}
