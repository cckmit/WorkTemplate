package com.fish.manger.v5;

import com.fish.dao.primary.mapper.RankingMapper;
import com.fish.dao.primary.model.Ranking;
import com.sun.glass.ui.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)

@MapperScan(basePackages = {"com.fish.dao.primary.mapper"})
public class testJDBC {
    @Autowired(required=true)
    private RankingMapper rankingMapper;
    @Test
    public void jdbcTest() {
        List<Ranking> rankings = rankingMapper.selectByDate("2019-12-12");

        for (Ranking ranking : rankings) {
            System.out.println(ranking.toString());
        }
    }

}
