package com.fish.controller;

import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.ShowRanking;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.RankingService;
import com.fish.service.cache.CacheService;
import com.fish.utils.ExcelUtils;
import com.fish.utils.ExportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 比赛结果
 * GameRankingController
 *
 * @author
 * @date
 */
@RestController
@RequestMapping(value = "/manage")
public class GameRankingController {

    @Autowired
    RankingService rankingService;
    @Autowired
    CacheService cacheService;

    /**
     * 展示比赛结果
     *
     * @param parameter
     * @return
     */
    @GetMapping(value = "/ranking")
    public GetResult getRanking(GetParameter parameter) {
        return rankingService.findAll(parameter);
    }

    /**
     * 导出Excel结果
     *
     * @param ranking
     * @return
     */
    @GetMapping(value = "/ranking/result")
    public void getRankingResult(ShowRanking ranking, HttpServletResponse response) {
        List<ExportResult> rankings = rankingService.selectResult(ranking);
        String roundName = ranking.getRoundName();
        Date matchdate = ranking.getMatchdate();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String gameTime = format.format(matchdate);
        Integer gamesCode = ranking.getGamesCode();
        ArcadeGames games = cacheService.getGames(gamesCode);
        String gameName = games.getDdname();
        ExcelUtils.writeExcel(rankings, gameTime + "-" + gameName + "-" + roundName, response);
    }

}
