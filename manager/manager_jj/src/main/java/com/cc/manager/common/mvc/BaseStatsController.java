package com.cc.manager.common.mvc;

import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;

/**
 * 数据统计查询Controller，主要实现分页查询
 * 如果需要其它数据，请自行根据业务扩展
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-03 19:59
 */
public interface BaseStatsController {

    /**
     * 分页查询
     * 方法注解：@GetMapping(value = "/getList/{queryData}")
     * 参数注解：@PathVariable
     *
     * @param statsListParam 数据查询参数
     * @return 数据查询结果
     */
    StatsListResult getList(StatsListParam statsListParam);

}
