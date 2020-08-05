package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.WxNotice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-30 21:03
 */
@Repository
public interface WxNoticeMapper {

    /**
     *
     */
    void insert(WxNotice wxNotice);

    /**
     * 根据noticeId查询是否记录过
     *
     * @param noticeId noticeId
     * @return WxNotice
     */
    WxNotice select(@Param("noticeId") String noticeId);

}
