package com.fish.service;

import com.fish.dao.second.mapper.NoticeMapper;
import com.fish.dao.second.model.NoticeWithBLOBs;
import com.fish.protocols.GetParameter;
import com.fish.utils.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OperatorNoticeService implements BaseService<NoticeWithBLOBs>
{
    @Autowired
    NoticeMapper noticeMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {

    }

    @Override
    public Class<NoticeWithBLOBs> getClassInfo()
    {
        return NoticeWithBLOBs.class;
    }

    @Override
    public boolean removeIf(NoticeWithBLOBs noticeWithBLOBs, Map<String, String> searchData)
    {
        return false;
    }

    @Override
    public List<NoticeWithBLOBs> selectAll(GetParameter parameter)
    {
        StringBuilder append = new StringBuilder();
        if (parameter.getSort() != null)
        {
            append.append("order by ").append(parameter.getSort()).append(" ").append(parameter.getOrder());
            parameter.setOrder(null);
            parameter.setSort(null);
        } else
        {
            append.append("order by id desc");
        }
        return noticeMapper.selectAll(append.toString());
    }

    /**
     * 进行保存数据
     *
     * @param notice 文件解析
     */
    public void saveNotice(NoticeWithBLOBs notice)
    {
        try
        {
            //先进行删除插入数据
            NoticeWithBLOBs element = noticeMapper.selectByPrimaryKey(notice.getId());
            if (element == null)
                noticeMapper.insert(notice);
            noticeMapper.updateByPrimaryKey(notice);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
    }
}
