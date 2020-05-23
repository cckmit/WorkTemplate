package com.cc.manager.common.mvc;

import lombok.Data;

/**
 * 统计数据查询基础Entity
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-01 18:19
 */
@Data
public abstract class BaseStatsEntity<E> {

    /**
     * 这里使用抽象类，定义一个是否有详情的标识，用于动态控制每条数据是否有向下一步的详情操作
     */
    private boolean haveDetail = true;

}
