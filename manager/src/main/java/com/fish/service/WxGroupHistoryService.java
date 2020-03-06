package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.WxGroupHistoryMapper;
import com.fish.dao.second.model.WxGroupHistory;
import com.fish.dao.second.model.WxGroupManager;
import com.fish.protocols.GetParameter;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class WxGroupHistoryService implements BaseService<WxGroupHistory>{

    @Autowired
    WxGroupHistoryMapper wxGroupHistoryMapper;

    @Override
    public List<WxGroupHistory> selectAll(GetParameter parameter) {
        List<WxGroupHistory> wxGroupHistories;

        JSONObject search = getSearchData(parameter.getSearchData());
        if(search == null){
            wxGroupHistories = wxGroupHistoryMapper.selectAll();
        }else {
            String times = search.getString("times");
            if (StringUtils.isNotBlank(times)){
                Date[] parse = XwhTool.parseDate(search.getString("times"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                wxGroupHistories = wxGroupHistoryMapper.selectSearchTime(format.format(parse[0]), format.format(parse[1]));
            }else {
                wxGroupHistories = wxGroupHistoryMapper.selectAll();
            }
        }
        return wxGroupHistories;
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null){
            return;
        }
        parameter.setOrder("desc");
        parameter.setSort("id");
    }

    @Override
    public Class<WxGroupHistory> getClassInfo() {
        return WxGroupHistory.class;
    }

    @Override
    public boolean removeIf(WxGroupHistory wxGroupHistory, JSONObject searchData) {
        if (existValueFalse(searchData.getString("wxGroupName"), wxGroupHistory.getWxGroupName()))
        {
            return true;
        }

        return existValueFalse(searchData.getString("wxGroupManager"), wxGroupHistory.getWxGroupManager());
    }
}
