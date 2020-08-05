package com.blazefire.dao.second.model;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-07-17 18:02
 */
@Data
public class TtAdClipboard {
    /**
     * 日期
     */
    private int dateVal;
    /**
     * 平台
     */
    private String platform;

    private String appId;
    /**
     * 版本
     */
    private String version;
    /**
     * 广告类型
     */
    private String adType;
    /**
     *
     */
    private String adStatus;
    /**
     * 数量
     */
    private int counts = 1;

    public void countsIncrement() {
        this.counts++;
    }

    public String getKey() {
        return this.dateVal + "_" + this.platform + "_" + this.appId + "_" + this.version + "_" + this.adType + "_" + this.adStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TtAdClipboard that = (TtAdClipboard) o;

        return new EqualsBuilder()
                .append(dateVal, that.dateVal)
                .append(platform, that.platform)
                .append(appId, that.appId)
                .append(version, that.version)
                .append(adType, that.adType)
                .append(adStatus, that.adStatus)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(dateVal)
                .append(platform)
                .append(appId)
                .append(version)
                .append(adType)
                .append(adStatus)
                .toHashCode();
    }
}
