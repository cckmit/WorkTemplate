package org.blaze.file.system.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xuwei
 */
@Component
@ConfigurationProperties(prefix = "persion")
public class BaseConfig {
    /**
     * 来源路径
     */
    private JSONObject srcPath;
    /**
     * 压缩路径
     */
    private String zipPath;
    /**
     * 文件上传地址
     */
    private String uploadFilePath;

    private String upload;

    private String domain;

    private JSONObject uploadJson;

    public JSONObject getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(JSONObject srcPath) {
        this.srcPath = srcPath;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public JSONObject getUploadJson() {
        return uploadJson;
    }

    public void setUploadJson(JSONObject uploadJson) {
        this.uploadJson = uploadJson;
    }
}
