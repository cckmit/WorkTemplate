package com.cc.manager.modules.jj.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.RoundExt;
import com.cc.manager.modules.jj.mapper.RoundExtMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * @author cf
 * @since 2020-05-11
 */
@Service
public class RoundExtService extends BaseCrudService<RoundExt, RoundExtMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<RoundExt> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(insertTime)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            String roundName = queryObject.getString("roundName");
            queryWrapper.eq(StringUtils.isNotBlank(roundName), "ddCode", roundName);
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, RoundExt entity) {
        //更新比赛奖励字段
        updateReward(entity);
        // 如果修改了比赛时长
        if (!StringUtils.isBlank(entity.getRoundLength())) {
            // 更新比赛时长
            updateRoundTimeLength(entity);
        }
        Integer ddGroup = entity.getDdGroup();
        if (ddGroup == 1) {
            int maxId = this.selectMaxIdProgram();
            entity.setId("G" + (maxId + 1));
            entity.setDdGroup(1);
        } else {
            int maxId = this.selectMaxIdGame();
            entity.setId("S" + (maxId + 1));
            entity.setDdGroup(0);
        }
        entity.setDdState(true);

    }

    /**
     * 小游戏当前最大ID
     *
     * @return int
     */
    private int selectMaxIdGame() {
        QueryWrapper<RoundExt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ddGroup", FALSE);
        queryWrapper.select("COUNT(*) AS id");
        Map<String, Object> map = this.mapper.selectMaps(queryWrapper).get(0);
        return new Integer(String.valueOf(map.get("id")));
    }

    /**
     * 小程序当前最大ID
     *
     * @return int
     */
    private int selectMaxIdProgram() {
        QueryWrapper<RoundExt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ddGroup", TRUE);
        queryWrapper.select("COUNT(*) AS id");
        Map<String, Object> map = this.mapper.selectMaps(queryWrapper).get(0);
        return new Integer(String.valueOf(map.get("id")));
    }

    @Override
    protected boolean update(String requestParam, RoundExt entity, UpdateWrapper<RoundExt> updateWrapper) {
        RoundExt roundExt = this.getById(entity.getId());
        //更新比赛奖励字段
        updateReward(entity);
        // 如果修改了比赛时长
        if (StringUtils.isNotBlank(entity.getRoundLength())) {
            // 更新比赛时长
            updateRoundTimeLength(entity);
        } else {
            entity.setDdTime(roundExt.getDdTime());
            entity.setTip(roundExt.getTip());
        }
        entity.setDdState(true);
        updateWrapper.eq("ddCode", roundExt.getId());
        return this.update(entity, updateWrapper);

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<RoundExt> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            String list = StrUtil.sub(requestParam, 1, -1);
            List<String> idList = Lists.newArrayList(StringUtils.split(list, ","));
            List<String> newList = new ArrayList<>();
            for (String str : idList) {
                String substring = str.substring(1, str.length() - 1);
                newList.add(substring);
            }
            return this.removeByIds(newList);
        }
        return false;
    }

    /**
     * 更新赛场奖励
     *
     * @param record record
     */
    private void updateReward(RoundExt record) {
        String ddReward = record.getDdReward();
        if (ddReward == null || ddReward.length() == 0 || "0".equals(ddReward)) {
            ddReward = "[" + '"' + "0#0#coin#0" + '"' + "]";
            record.setDdReward(ddReward);
        }
    }

    /**
     * 更新比赛时长
     *
     * @param record record
     */
    private void updateRoundTimeLength(RoundExt record) {
        String roundLength = record.getRoundLength().toLowerCase();
        String timeValue = roundLength.substring(0, roundLength.length() - 1);
        String timeFormat = roundLength.substring(roundLength.length() - 1);
        switch (timeFormat) {
            case "s":
                record.setDdTime(Long.parseLong(timeValue));
                record.setTip(timeValue + "秒");
                break;
            case "m":
                record.setTip(timeValue + "分钟");
                record.setDdTime(Long.parseLong(timeValue) * 60);
                break;
            case "h":
                record.setTip(timeValue + "小时");
                record.setDdTime(Long.parseLong(timeValue) * 60 * 60);
                break;
            case "d":
                record.setTip(timeValue + "天");
                record.setDdTime(Long.parseLong(timeValue) * 60 * 60 * 24);
                break;
            default:
                break;
        }
    }

}
