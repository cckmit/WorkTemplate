package com.blazefire.service.tt;

import com.blazefire.dao.second.mapper.TtAdClipboardMapper;
import com.blazefire.dao.second.model.TtAdClipboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-07-20 13:53
 */
@Service
public class TtAdClipboardService {

    private TtAdClipboardMapper mapper;

    public void insert(List<TtAdClipboard> collectList) {
        if (!collectList.isEmpty()) {
            this.mapper.insert(collectList);
        }
    }

    @Autowired
    public void setMapper(TtAdClipboardMapper mapper) {
        this.mapper = mapper;
    }
}
