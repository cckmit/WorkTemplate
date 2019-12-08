package com.fish.dao.second.mapper;

import com.fish.dao.second.model.Notice;
import com.fish.dao.second.model.NoticeWithBLOBs;

import java.util.List;

public interface NoticeMapper
{
    int deleteByPrimaryKey(Integer id);

    int insert(NoticeWithBLOBs record);

    int insertSelective(NoticeWithBLOBs record);

    NoticeWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NoticeWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(NoticeWithBLOBs record);

    int updateByPrimaryKey(Notice record);

    List<NoticeWithBLOBs> selectAll(String append);
}