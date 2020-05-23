package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.mapper.MiniGameMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
public class MiniGameService extends BaseCrudService<MiniGame, MiniGameMapper> {
    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<MiniGame> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(wx_date)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<MiniGame> entityList) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<MiniGame> deleteWrapper) {
        return false;
    }

}
