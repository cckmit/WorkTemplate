package com.fish.service;

import com.fish.dao.second.mapper.RecordBookMapper;
import com.fish.dao.second.model.RecordBook;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BasicUserBookService implements BaseService<RecordBook>
{
    @Autowired
    RecordBookMapper bookMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("id");

    }

    @Override
    public Class<RecordBook> getClassInfo()
    {
        return RecordBook.class;
    }

    @Override
    public boolean removeIf(RecordBook recordBook, Map<String, String> searchData)
    {
        if (existTimeFalse(recordBook.getUpdatetime(), searchData.get("times")))
            return true;
        if (existValueFalse(searchData.get("userId"), recordBook.getUserid()))
            return true;
        if (existValueFalse(searchData.get("nickName"), recordBook.getNickname()))
            return true;
        if (existValueFalse(searchData.get("book"), recordBook.getBook()))
            return true;
        return existValueFalse(searchData.get("basinid"), recordBook.getBasinid());
    }

    @Override
    public List<RecordBook> selectAll(GetParameter parameter)
    {
        return bookMapper.selectAll();
    }
}
