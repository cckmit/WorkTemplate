package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.WxVisitPage;
import org.springframework.stereotype.Repository;

/**
 * WxVisitPage Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 18:14
 */
@Repository
public interface WxVisitPageMapper {

    /**
     * 插入访问页面
     *
     * @param wxVisitPage 访问页面
     * @return id
     */
    int insert(WxVisitPage wxVisitPage);

    /**
     * 删除访问页面
     *
     * @param wxVisitPage 访问页面
     * @return 删除结果
     */
    int delete(WxVisitPage wxVisitPage);

}
