package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-30 21:01
 */
@Data
public class WxNotice {

    private String appId;
    private String noticeId;
    private long createTime;
    private String title;
    private boolean isMarked;
    private String content;

}
