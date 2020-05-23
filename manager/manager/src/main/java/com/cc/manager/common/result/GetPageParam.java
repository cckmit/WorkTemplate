package com.cc.manager.common.result;

import lombok.Data;

/**
 * 分页查询参数，多用于数据表分页查询
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-27 17:20
 */
@Data
public class GetPageParam {
//    /**
//     * 查询类型
//     */
//    private String queryType;
    /**
     * 查询参数，Json格式
     */
    private String queryData;
    /**
     * 排序条件，Json格式字符串：{"id":"ASC","name":"DESC"} -> ORDER BY ID ASC, name DESC <br/>
     * 具体参考：https://mp.baomidou.com/guide/wrapper.html#orderby
     */
    private String orderBy;
    /**
     * 分组方式，Json数组格式字符串：["id", "name"] -> GROUP BY id, name<br/>
     * 具体参考： https://mp.baomidou.com/guide/wrapper.html#groupby
     */
    private String groupBy;
    /**
     * 当前页
     */
    private int page;
    /**
     * 每页显示条数
     */
    private int limit;

}
