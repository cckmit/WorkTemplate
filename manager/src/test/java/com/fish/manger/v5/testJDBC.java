package com.fish.manger.v5;

import com.fish.dao.primary.mapper.RankingMapper;
import com.fish.dao.primary.model.Ranking;
import com.sun.glass.ui.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@MapperScan("com.fish.manger.v5.mapper")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class testJDBC {
    @Autowired
    private RankingMapper rankingMapper;
    @Test
    public void jdbcTest() {
//        List<Ranking> rankings = rankingMapper.selectByDate("");
//
//        for (Ranking ranking : rankings) {
//            System.out.println(ranking.toString());
//        }
    }
    /**
     * 根据 id 生成分表编号的方法
     */
    public static String generateTableCodeByKey(String key) {
        if (key.length() < 16) {
            return "0";
        }

        int index = key.length() - 4;
        int c0 = (int) key.charAt(index);
        int c1 = (int) key.charAt(index + 1);
        int c2 = (int) key.charAt(index + 2);
        int c3 = (int) key.charAt(index + 3);
        int value = (c0 % 10) * 1000 + (c1 % 10) * 100 + (c2 % 10) * 10 + (c3 % 10);
        if (value < 0) {
            value = -value;
        }

        return String.valueOf(value);
    }


    public static String timeStampDate(String time) {
        Long timeLong = Long.parseLong(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(timeLong));
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
