package com.tools.unzip;

/**
 * 压缩文件读取接口
 *
 * @author xuweihua
 */
public interface ZipFileReadInterface {
    /**
     * 进行读取压缩文件中单行
     *
     * @param line
     */
    void readLogLine(String line);

    /**
     * 压缩文件读取完成
     */
    void readFileFinish();

}
