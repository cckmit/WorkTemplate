package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.TtAdClipboard;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-07-20 13:39
 */
@Repository
public interface TtAdClipboardMapper {

    void insert(Collection<TtAdClipboard> list);

}
