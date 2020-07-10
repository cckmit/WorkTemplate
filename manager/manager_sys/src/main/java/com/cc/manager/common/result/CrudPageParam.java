package com.cc.manager.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * CRUD操作中的分页查询请求参数
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-27 17:20
 */
@Data
public class CrudPageParam implements Serializable {
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
     * 当前页
     */
    private int page;
    /**
     * 每页显示条数
     */
    private int limit;

}
