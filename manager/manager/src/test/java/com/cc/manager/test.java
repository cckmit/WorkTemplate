package com.cc.manager;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author: CF
 * @date 2020/5/11 11:54
 */
public class test {
    public static void main(String[] args) {
        List<String> selectList = Lists.newArrayList("sum(showNum) as showNum", "sum(clickNum) as clickNum",
                "sum(promoteShowNum) as promoteShowNum", "sum(promoteClickNum) as promoteClickNum",
                "sum(targetShowNum) as targetShowNum");

        System.out.println(selectList);
        List<String> idList = Lists.newArrayList(StringUtils.split(","));
    }
}
