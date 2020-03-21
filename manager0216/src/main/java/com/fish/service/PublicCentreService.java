package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGameSetMapper;
import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.mapper.PublicCentreMapper;
import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.PublicCentre;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.service.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 公众号配置中心
 * Service
 *
 * @author
 * @date
 */
@Service
public class PublicCentreService implements BaseService<PublicCentre> {
    @Autowired
    PublicCentreMapper publicCentreMapper;
    @Autowired
    ArcadeGameSetMapper arcadeGameSetMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    CacheService cacheService;

    /**
     * 查询所有公众号配置信息
     *
     * @param parameter
     * @return
     */
    @Override
    public List<PublicCentre> selectAll(GetParameter parameter) {
        List<PublicCentre> publicCentre = new ArrayList<>();
        List<PublicCentre> banners;
        List<PublicCentre> recommends;
        List<PublicCentre> games;
        //根据推荐位类型做数据排序处理
        banners = publicCentreMapper.selectAllBanner();
        publicCentre.addAll(banners);
        recommends = publicCentreMapper.selectAllRecommend();
        publicCentre.addAll(recommends);
        games = publicCentreMapper.selectAllGame();
        publicCentre.addAll(games);
        return publicCentre;
    }

    /**
     * 查询所有数据上传JSON到客户端
     *
     * @param parameter
     * @return
     */
    public void selectAllForJson(GetParameter parameter) {

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
            // 输出新的json文件
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter("/data/tomcat8/apache-public/webapps/public/mui_wxoa/config.json"));
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
            LOGGER.error("提交公众号JSON文件失败" + ", 详细信息:{}", e.getMessage());
        }
    }

    /**
     * 游戏处理成所需json信息
     *
     * @param publicGames
     * @return
     */
    private List<Object> gamesDeal(List<PublicCentre> publicGames) {
        List<Object> games = new ArrayList<>();
        for (PublicCentre publicGame : publicGames) {
            String flag;
            Integer skipSet = publicGame.getSkipSet();
            String img = publicGame.getResourceName();
            ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(skipSet);
            String content = arcadeGameSet.getDdcontent512a();
            String[] splitGame = content.split("#");
            for (String gameCode : splitGame) {
                ArcadeGames game = cacheService.getGames(Integer.parseInt(gameCode));
                String name = game.getDdname();
                Integer isPk = game.getDdispk();
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
     * @param publicRecommends
     * @return
     */
    private List<Object> recommendsDeal(List<PublicCentre> publicRecommends) {
        List<Object> recommends = new ArrayList<>();
        for (PublicCentre publicRecommend : publicRecommends) {
            String img = publicRecommend.getResourceName();
            Integer skipSet = publicRecommend.getSkipSet();
            ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(skipSet);
            String content = arcadeGameSet.getDdcontent512a();
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
     * @param publicBanners
     * @return
     */
    private List<Object> bannersDeal(List<PublicCentre> publicBanners) {
        List<Object> banners = new ArrayList<>();
        for (PublicCentre publicBanner : publicBanners) {
            String img = publicBanner.getResourceName();
            String detailImg = publicBanner.getDetailName();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("img", "images/banner/" + img);
            jsonObject.put("detailimg", "images/banner/" + detailImg);
            banners.add(jsonObject);
        }
        return banners;
    }

    /**
     * 新增
     *
     * @param record
     * @return
     */
    public int insert(PublicCentre record) {
        Byte skipType = record.getSkipType();
        if (skipType == 0) {
            record.setSkipSet(0);
        }
        return publicCentreMapper.insert(record);
    }

    /**
     * 修改
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(PublicCentre record) {
        return publicCentreMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null) {
            return;
        }
        parameter.setOrder("desc");
        parameter.setSort("ddcode");
    }

    @Override
    public Class<PublicCentre> getClassInfo() {
        return PublicCentre.class;
    }

    @Override
    public boolean removeIf(PublicCentre centre, JSONObject searchData) {
        if (existValueFalse(searchData.getString("gameId"), centre.getId())) {
            return true;
        }
        return existValueFalse(searchData.getString("gameName"), centre.getDetailName());
    }

    /**
     * 删除选中的公众号配置内容
     *
     * @param jsonObject
     * @return
     */
    public PostResult deleteSelective(JSONObject jsonObject) {
        PostResult result = new PostResult();
        String ddId = jsonObject.getString("deleteIds");
        int delete = publicCentreMapper.deleteByPrimaryKey(Integer.parseInt(ddId));
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        return result;
    }

    /**
     * 更新展示顺序
     *
     * @param jsonObject
     * @return
     */
    public PostResult updateShowId(JSONObject jsonObject) {
        PostResult result = new PostResult();
        PublicCentre publicCentre = new PublicCentre();
        publicCentre.setId(jsonObject.getInteger("id"));
        publicCentre.setShowId(jsonObject.getInteger("showId"));
        int updateShowId = publicCentreMapper.updateShowId(publicCentre);
        if (updateShowId <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，更新展示位顺序失败！");
        }
        return result;
    }
}
