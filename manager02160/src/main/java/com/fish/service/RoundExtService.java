package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.RoundExtMapper;
import com.fish.dao.primary.model.RoundExt;
import com.fish.protocols.GetParameter;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 赛制配置 Service
 * RoundExtService
 *
 * @author
 * @date
 */
@Service
public class RoundExtService implements BaseService<RoundExt> {

    @Autowired
    RoundExtMapper roundExtMapper;

    /**
     * 查询常规游戏赛制信息
     *
     * @param parameter
     * @return
     */
    @Override
    public List<RoundExt> selectAll(GetParameter parameter) {
        List<RoundExt> roundExts;
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null || search.getString("times").isEmpty()) {
            roundExts = roundExtMapper.selectAll();
        } else {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            roundExts = roundExtMapper.selectByTimes(format.format(parse[0]), format.format(parse[1]));
        }
        return roundExts;
    }

    /**
     * 新增
     *
     * @param record
     * @return
     */
    public int insert(RoundExt record) {

        //更新比赛奖励字段
        updateddReward(record);

        // 如果修改了比赛时长
        if (!StringUtils.isBlank(record.getRoundLength())) {
            // 更新比赛时长
            updateRoundTimeLength(record);
        }
        Integer ddgroup = record.getDdgroup();
        if (ddgroup == 1) {
            int maxId = roundExtMapper.selectGMaxId();
            record.setDdcode("G" + (maxId + 1));
            record.setDdgroup(1);
        } else {
            int maxId = roundExtMapper.selectSMaxId();
            record.setDdcode("S" + (maxId + 1));
            record.setDdgroup(0);
        }
        record.setDdstate(true);
        record.setInserttime(new Timestamp(System.currentTimeMillis()));
        int insert = roundExtMapper.insert(record);
        return insert;
    }

    /**
     * 修改
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(RoundExt record) {

        //更新比赛奖励字段
        updateddReward(record);

        // 如果修改了比赛时长
        if (!StringUtils.isBlank(record.getRoundLength())) {
            // 更新比赛时长
            updateRoundTimeLength(record);
        }
        record.setInserttime(new Timestamp(System.currentTimeMillis()));
        return roundExtMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("inserttime");
    }

    @Override
    public Class<RoundExt> getClassInfo() {
        return RoundExt.class;
    }

    @Override
    public boolean removeIf(RoundExt record, JSONObject searchData) {
        String code = "";
        String roundSelect = searchData.get("roundSelect").toString();
        if (roundSelect != null && roundSelect.contains("-")) {
            code = roundSelect.split("-")[0];
        }
        if (existValueFull(code, record.getDdcode())) {
            return true;
        }
        return false;
    }

    /**
     * 更新赛场奖励
     *
     * @param record
     */
    private void updateddReward(RoundExt record) {
        String ddReward = record.getDdreward();
        if (ddReward == null || ddReward.length() == 0 || "0".equals(ddReward)) {
            ddReward = "['0#0#coin#0']";
            record.setDdreward(ddReward);
        }
    }

    /**
     * 更新比赛时长
     *
     * @param record
     */
    private void updateRoundTimeLength(RoundExt record) {
        String roundLength = record.getRoundLength().toLowerCase();
        String timeValue = roundLength.substring(0, roundLength.length() - 1);
        String timeFormat = roundLength.substring(roundLength.length() - 1);
        switch (timeFormat) {
            case "s":
                record.setDdtime(Long.parseLong(timeValue));
                record.setTip(timeValue + "秒");
                break;
            case "m":
                record.setTip(timeValue + "分钟");
                record.setDdtime(Long.parseLong(timeValue) * 60);
                break;
            case "h":
                record.setTip(timeValue + "小时");
                record.setDdtime(Long.parseLong(timeValue) * 60 * 60);
                break;
            case "d":
                record.setTip(timeValue + "天");
                record.setDdtime(Long.parseLong(timeValue) * 60 * 60 * 24);
                break;
            default:
                break;
        }
    }
}
