package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.GameSet;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.entity.PublicCentre;
import com.cc.manager.modules.jj.mapper.PublicCentreMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cf
 * @since 2020-05-13
 */
@Service
@DS("jj")
public class PublicCentreService extends BaseCrudService<PublicCentre, PublicCentreMapper> {

    private PublicCentreMapper publicCentreMapper;
    private GamesService gamesService;
    private GameSetService gameSetService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<PublicCentre> queryWrapper) {

    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<PublicCentre> entityList) {
        List<PublicCentre> banners;
        List<PublicCentre> recommends;
        List<PublicCentre> games;
        //根据推荐位类型做数据排序处理
        banners = publicCentreMapper.selectAllBanner();
        List<PublicCentre> publicCentre = new ArrayList<>(banners);
        recommends = publicCentreMapper.selectAllRecommend();
        publicCentre.addAll(recommends);
        games = publicCentreMapper.selectAllGame();
        publicCentre.addAll(games);
        entityList.clear();
        entityList.addAll(publicCentre);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<PublicCentre> deleteWrapper) {
        return false;
    }


    @Override
    protected void updateInsertEntity(String requestParam, PublicCentre entity) {
        Integer skipType = entity.getSkipType();
        if (skipType == 0) {
            entity.setSkipSet(0);
        }
    }


    public void selectAllForJson() {
        //查询不同类型的数据分别处理
        List<PublicCentre> publicBanners = publicCentreMapper.selectAllBanner();
        List<PublicCentre> publicRecommends = publicCentreMapper.selectAllRecommend();
        List<PublicCentre> publicGames = publicCentreMapper.selectAllGame();

        List<Object> banners = bannersDeal(publicBanners);
        List<Object> recommends = recommendsDeal(publicRecommends);
        List<Object> games = gamesDeal(publicGames);
        // 读取原始json文件并进行操作和输出
        try {
            // 读取原始json文件
            BufferedReader br = new BufferedReader(
                    new FileReader(PublicCentreService.class.getResource("/").getPath() + "config.json"));
            // 输出新的json文件  mui_wxoa_debug 测试 mui_wxoa 线上实际使用
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter("/data/tomcat8/apache-public/webapps/public/mui_wxoa_debug/config.json"));
            String ws = null;
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonObject = JSONObject.parseObject(sb.toString());
            jsonObject.put("banners", banners);
            jsonObject.put("recommends", recommends);
            jsonObject.put("games", games);
            ws = jsonObject.toString();
            bw.write(ws);
            bw.flush();
            br.close();
            bw.close();
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 游戏处理成所需json信息
     *
     * @param publicGames publicGames
     * @return gamesList
     */
    private List<Object> gamesDeal(List<PublicCentre> publicGames) {
        List<Object> games = new ArrayList<>();
        for (PublicCentre publicGame : publicGames) {
            String flag;
            Integer skipSet = publicGame.getSkipSet();
            String img = publicGame.getResourceName();
            GameSet gameSet = this.gameSetService.getCacheEntity(GameSet.class, skipSet.toString());
            String content = gameSet.getDdContent512a();
            String[] splitGame = content.split("#");
            for (String gameCode : splitGame) {
                Games game = this.gamesService.getCacheEntity(Games.class, gameCode);
                String name = game.getDdName();
                Integer isPk = game.getDdIsPk();
                if (isPk == 1) {
                    flag = "pk";
                } else {
                    flag = "team";
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("gameid", Integer.parseInt(gameCode));
                jsonObject.put("icon", "images/game/" + img);
                jsonObject.put("name", name);
                jsonObject.put("desc", name);
                jsonObject.put("flag", flag);
                jsonObject.put("checkVersion", "");
                games.add(jsonObject);
            }
        }
        return games;
    }

    /**
     * 热门推荐处理成所需json信息
     *
     * @param publicRecommends publicRecommends
     * @return recommendsList
     */
    private List<Object> recommendsDeal(List<PublicCentre> publicRecommends) {
        List<Object> recommends = new ArrayList<>();
        for (PublicCentre publicRecommend : publicRecommends) {
            String img = publicRecommend.getResourceName();
            Integer skipSet = publicRecommend.getSkipSet();
            GameSet gameSet = this.gameSetService.getCacheEntity(GameSet.class, skipSet.toString());
            String content = gameSet.getDdContent512a();
            String[] split = content.split("#");
            List<Integer> gameyList = new ArrayList<>();
            for (String gameId : split) {
                Integer integer = Integer.valueOf(gameId);
                gameyList.add(integer);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gameid", gameyList);
            jsonObject.put("icon", "images/recommend/" + img);
            jsonObject.put("name", "111");
            recommends.add(jsonObject);
        }
        return recommends;
    }

    /**
     * Banner处理成所需json信息
     *
     * @param publicBanners publicBanners
     * @return bannersList
     */
    private List<Object> bannersDeal(List<PublicCentre> publicBanners) {
        List<Object> banners = new ArrayList<>();
        for (PublicCentre publicBanner : publicBanners) {
            String img = publicBanner.getResourceName();
            String detailImg = publicBanner.getDetailName();
            Integer bannerType = publicBanner.getBannerType();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("img", "images/banner/" + img);
            if (bannerType == 0) {
                jsonObject.put("detailimg", detailImg);
                jsonObject.put("jumpType", "url");
            } else {
                jsonObject.put("detailimg", "images/banner/" + detailImg);
                jsonObject.put("jumpType", "banner");
            }
            banners.add(jsonObject);
        }
        return banners;
    }

    @Autowired
    public void setPublicCentreMapper(PublicCentreMapper publicCentreMapper) {
        this.publicCentreMapper = publicCentreMapper;
    }

    @Autowired
    public void setGamesService(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Autowired
    public void setGameSetService(GameSetService gameSetService) {
        this.gameSetService = gameSetService;
    }

}
