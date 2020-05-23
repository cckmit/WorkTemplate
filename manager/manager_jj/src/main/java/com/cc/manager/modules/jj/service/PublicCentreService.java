package com.cc.manager.modules.jj.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.GameSet;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.entity.PublicCentre;
import com.cc.manager.modules.jj.mapper.PublicCentreMapper;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
@DS("jj")
public class PublicCentreService extends BaseCrudService<PublicCentre, PublicCentreMapper> {

    private GamesService gamesService;
    private GameSetService gameSetService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<PublicCentre> queryWrapper) {
        queryWrapper.orderByAsc("recommend_type", "show_id");
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<PublicCentre> entityList) {

    }

    @Override
    protected void updateInsertEntity(String requestParam, PublicCentre entity) {
        if (entity.getSkipType() == 0) {
            entity.setSkipSet(0);
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<PublicCentre> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            String list = StrUtil.sub(requestParam, 1, -1);
            List<String> idList = Lists.newArrayList(StringUtils.split(list, ","));
            return this.removeByIds(idList);
        }
        return false;
    }

    /**
     * 公众号内容提交JSON文件到客户端
     */
    public PostResult selectAllForJson() {
        PostResult postResult = new PostResult();

        // 查询数据
        List<PublicCentre> publicCentreList = this.list();

        // 根据推荐位类整理数据
        JSONArray banners = new JSONArray();
        JSONArray recommends = new JSONArray();
        JSONArray games = new JSONArray();
        publicCentreList.forEach(publicCentre -> {
            if (publicCentre.getRecommendType() == 0) {
                JSONObject jsonObject = bannersDeal(publicCentre);
                banners.add(jsonObject);
            } else if (publicCentre.getRecommendType() == 1) {
                JSONObject jsonObject = recommendsDeal(publicCentre);
                recommends.add(jsonObject);
            } else if (publicCentre.getRecommendType() == 2) {
                JSONObject jsonObject = gamesDeal(publicCentre);
                games.add(jsonObject);
            }
        });
        // 读取原始json文件并进行操作和输出
        try {
            String sb = FileUtils.readFileToString(new File(PublicCentreService.class.getResource("/").getPath() + "config.json"), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(sb);
            jsonObject.put("banners", banners);
            jsonObject.put("recommends", recommends);
            jsonObject.put("games", games);
            FileUtils.write(new File("/data/tomcat8/apache-public/webapps/public/mui_wxoa_debug/config.json"), jsonObject.toJSONString(), "UTF-8");
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            postResult.setCode(2);
            postResult.setMsg("Json文件写入失败");
        }
        return postResult;
    }

    /**
     * 游戏处理成所需json信息
     *
     * @param publicCentre publicGames
     * @return gamesList
     */
    private JSONObject gamesDeal(PublicCentre publicCentre) {
        JSONObject jsonObject = new JSONObject();
        String flag = "team";
        Integer skipSet = publicCentre.getSkipSet();
        String img = publicCentre.getResourceName();
        GameSet gameSet = this.gameSetService.getCacheEntity(GameSet.class, skipSet.toString());
        String content = gameSet.getDdContent512a();
        String[] splitGame = content.split("#");
        for (String gameCode : splitGame) {
            Games game = this.gamesService.getCacheEntity(Games.class, gameCode);
            Integer isPk = game.getDdIsPk();
            if (isPk == 1) {
                flag = "pk";
            }
            jsonObject.put("gameid", Integer.parseInt(gameCode));
            jsonObject.put("icon", "images/game/" + img);
            jsonObject.put("name", game.getDdName());
            jsonObject.put("desc", game.getDdName());
            jsonObject.put("flag", flag);
            jsonObject.put("checkVersion", "");
        }
        return jsonObject;
    }

    /**
     * 热门推荐处理成所需json信息
     *
     * @param publicCentre publicRecommends
     * @return recommendsList
     */
    private JSONObject recommendsDeal(PublicCentre publicCentre) {
        String img = publicCentre.getResourceName();
        Integer skipSet = publicCentre.getSkipSet();
        GameSet gameSet = this.gameSetService.getCacheEntity(GameSet.class, skipSet.toString());
        String content = gameSet.getDdContent512a();
        String[] split = content.split("#");
        List<Integer> gamesList = new ArrayList<>();
        for (String gameId : split) {
            Integer integer = Integer.valueOf(gameId);
            gamesList.add(integer);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gameid", gamesList);
        jsonObject.put("icon", "images/recommend/" + img);
        jsonObject.put("name", "111");

        return jsonObject;
    }

    /**
     * Banner处理成所需json信息
     *
     * @param publicCentre publicCentre
     * @return bannersList
     */
    private JSONObject bannersDeal(PublicCentre publicCentre) {
        JSONObject jsonObject = new JSONObject();
        String img = publicCentre.getResourceName();
        String detailImg = publicCentre.getDetailName();
        Integer bannerType = publicCentre.getBannerType();
        jsonObject.put("img", "images/banner/" + img);
        if (bannerType == 0) {
            jsonObject.put("detailimg", detailImg);
            jsonObject.put("jumpType", "url");
        } else {
            jsonObject.put("detailimg", "images/banner/" + detailImg);
            jsonObject.put("jumpType", "banner");
        }
        return jsonObject;
    }

    /**
     * 更新展示顺序
     *
     * @param jsonObject jsonObject
     * @return PostResult
     */
    public PostResult updateShowId(JSONObject jsonObject) {
        PostResult result = new PostResult();
        PublicCentre publicCentre = new PublicCentre();
        publicCentre.setId(jsonObject.getInteger("id"));
        publicCentre.setShowId(jsonObject.getInteger("showId"));
        QueryWrapper<PublicCentre> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", jsonObject.getInteger("id"));
        boolean update = this.update(publicCentre, queryWrapper);
        if (!update) {
            result.setCode(2);
            result.setMsg("操作失败，更新展示位顺序失败！");
        }
        return result;
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
