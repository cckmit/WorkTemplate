package com.fish.service;

import com.fish.dao.primary.mapper.StatisticsPayMapper;
import com.fish.protocols.GetParameter;
import com.fish.protocols.StatisticsPayVO;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsPayService implements BaseService<StatisticsPayVO>
{
    @Autowired
    StatisticsPayMapper payMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        switch (parameter.getDatagrid())
        {
            case "day":
                parameter.setSort("payDate");
                break;
            case "month":
                parameter.setSort("newMonth");
                break;
            case "week":
                parameter.setSort("newWeek");
                break;
            default:
                break;
        }

        parameter.setOrder("desc");
    }

    @Override
    public Class<StatisticsPayVO> getClassInfo()
    {
        return StatisticsPayVO.class;
    }

    @Override
    public boolean removeIf(StatisticsPayVO vo, Map<String, String> searchData)
    {
        String times = searchData.get("times");
        if (times == null || times.trim().isEmpty())
            return false;
        if (vo.getPayDate() != null)
        {
            Date[] parse = XwhTool.parseDate(times);
            Date date = vo.getPayDate();
            return date.before(parse[0]) || date.after(parse[1]);
        }
        if (vo.getNewWeek() != null)
        {
            int[] weeks = XwhTool.parseWeek(times);
            int week = vo.getNewWeek();
            return week < weeks[0] || week > weeks[1];
        }
        if (vo.getNewMonth() != null)
        {
            int[] months = XwhTool.parseMonth(times);
            int month = vo.getNewMonth();
            return month < months[0] || month > months[1];
        }
        return false;
    }

    @Override
    public List<StatisticsPayVO> selectAll(GetParameter parameter)
    {
        //use SQL query!!!
        switch (parameter.getDatagrid())
        {
            case "day":
                return payMapper.selectByDay();
            case "week":
                return payMapper.selectByWeek();
            case "month":
                return payMapper.selectByMonth();
        }
        return null;
    }
}
