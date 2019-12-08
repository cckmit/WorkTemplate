package com.fish.service;

import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.protocols.GetParameter;
import com.fish.protocols.UserPayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BasicUserPayService implements BaseService<UserPayVO>
{
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("ranking");
        parameter.setOrder("desc");
    }

    @Override
    public Class<UserPayVO> getClassInfo()
    {
        return UserPayVO.class;
    }

    @Override
    public boolean removeIf(UserPayVO userPayVO, Map<String, String> searchData)
    {
        if (existValueFalse(searchData.get("userId"), userPayVO.getUserId()))
            return true;
        if (existValueFalse(searchData.get("basin"), userPayVO.getBasin()))
            return true;
        //充值金额
        String costFormula = searchData.get("cost_formula");
        String cost = searchData.get("cost");
        if (cost != null && !cost.trim().isEmpty())
        {
            BigDecimal _value = userPayVO.getPayCost();
            double value = 0;
            if (_value != null)
                value = _value.doubleValue();
            double temp = Double.parseDouble(cost);
            switch (costFormula)
            {
                //小于
                case "0":
                    if (value >= temp)
                        return true;
                    break;
                case "1":
                    if (value != temp)
                        return true;
                    break;
                case "2":
                    if (value <= temp)
                        return true;
                    break;
                default:
                    break;

            }
        }
        return existValueFalse(searchData.get("nickName"), userPayVO.getNickName());
    }

    @Override
    public List<UserPayVO> selectAll(GetParameter parameter)
    {
        List<UserPayVO> list = userInfoMapper.selectMonitorUser();
        AtomicLong atomic = new AtomicLong();
        list.forEach(vo ->
                vo.setRanking(atomic.incrementAndGet()));
        return list;
    }

}
