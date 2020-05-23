package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.mapper.MiniGameMapper;
import com.cc.manager.modules.fc.mapper.MinitjWxMapper;
import com.cc.manager.modules.fc.service.IMiniGameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cc.manager.modules.jj.entity.WxConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cf
 * @since 2020-05-13
 */
@Service
public class MiniGameService extends BaseCrudService<MiniGame, MiniGameMapper>  {
    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<MiniGame> queryWrapper) {
        // 前端提交的条件
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {
            String times = queryData.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(wx_date)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
        }else {
            String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
            queryWrapper.between("DATE(wx_date)", beginTime, endTime);
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
