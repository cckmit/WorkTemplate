package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.mapper.RoundExtMapper;
import com.fish.dao.primary.mapper.RoundMatchMapper;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.RoundExt;
import com.fish.dao.primary.model.RoundMatch;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @pragram: RoundMatchService
 * @description: 现金赛制Service
 * @create:
 */
@Service
public class RoundMatchService implements BaseService<RoundMatch> {

    @Autowired
    RoundMatchMapper roundMatchMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    RoundExtMapper roundExtMapper;
    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    CacheService cacheService;

    /**
     * 查询小程序赛制配置 roundMatch
     *
     * @param parameter
     * @return
     */
    @Override
    public List<RoundMatch> selectAll(GetParameter parameter) {
        String url = baseConfig.getResHost();
        // 1、查询全部RoundMatch
        List<RoundMatch> roundMatchs = this.roundMatchMapper.selectAll();
        // 2、更新循环所需参数
        this.cacheService.updateAllArcadeGames();
        this.cacheService.updateAllRoundExt();
        this.cacheService.updateAllWxConfig();
        //int index = 1;
        for (RoundMatch roundMatch : roundMatchs) {
            // 更新游戏信息
            ArcadeGames arcadeGames = this.cacheService.getArcadeGames(roundMatch.getDdgame());
            if (arcadeGames == null) {
                continue;
            }
            roundMatch.setGameName(arcadeGames.getDdname());
            // 判断资源分享JSON字串
            if (StringUtils.isNotBlank(arcadeGames.getDdshareres())) {
                JSONArray shareLists = JSONObject.parseArray(arcadeGames.getDdshareres());
                for (int i = 0; i < shareLists.size(); i++) {
                    JSONObject jsonObject = JSONObject.parseObject(shareLists.getString(i));
                    if (jsonObject.getInteger("position") == 4) {
                        String icon = WxConfigService.concatUrl(this.baseConfig.getResHost(),
                                jsonObject.getString("url"), "g" + arcadeGames.getDdcode(), "share");
                        if (StringUtils.isNotBlank(icon)) {
                            roundMatch.setJumpDirect(icon);
                        }
                    }
                }
            }
            // 更新赛场信息
            RoundExt roundExt = this.cacheService.getRoundExt(roundMatch.getDdround());
            if (roundExt != null) {
                roundMatch.setRoundSelect(roundMatch.getDdround() + "-" + roundExt.getDdname());
                roundMatch.setRoundName(roundExt.getDdname());
                roundMatch.setDdreward(roundExt.getDdreward());
                roundMatch.setRoundLength(roundExt.getTip());
            }
            WxConfig wxConfig = this.cacheService.getWxConfig(roundMatch.getDdappid());
            if (wxConfig != null) {
                roundMatch.setAppName(wxConfig.getProductName());
            }
            //获取资源图片信息
            String ddRes = roundMatch.getDdres();
            if (StringUtils.isNotBlank(ddRes)) {
                String[] sz = ddRes.split("/");
                String pngName = sz[sz.length - 1];
                String gamePicture0;
                String gamePicture1;
                if (ddRes.endsWith(".zip")) {
                    gamePicture0 = url + pngName.substring(0, pngName.lastIndexOf(".")) + "/biaoti.png";
                    gamePicture1 = url + pngName.substring(0, pngName.lastIndexOf(".")) + "/bisai.png";
                } else {
                    gamePicture0 = url + pngName + "/biaoti.png";
                    gamePicture1 = url + pngName + "/bisai.png";
                }
                roundMatch.setGamePicture0(gamePicture0);
                roundMatch.setGamePicture1(gamePicture1);
            }
        }
        return roundMatchs;
    }

    /**
     * 新增小程序赛制配置
     *
     * @param record
     * @return
     */
    public int insert(RoundMatch record) {
        updateOrInsertRoundMatch(record, "insert");
        return roundMatchMapper.insert(record);
    }

    /**
     * 修改小程序赛制配置
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(RoundMatch record) {
        updateOrInsertRoundMatch(record, "update");
        return roundMatchMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 默认排序
     *
     * @param parameter
     */
    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null) {
            return;
        }
        parameter.setSort("ddtime");
        parameter.setOrder("desc");
    }

    @Override
    public Class<RoundMatch> getClassInfo() {
        return RoundMatch.class;
    }

    /**
     * 筛选
     *
     * @param recharge
     * @param searchData 查询内容
     * @return
     */
    @Override
    public boolean removeIf(RoundMatch recharge, JSONObject searchData) {
        if (existValueFalse(searchData.getString("productName"), recharge.getAppName())) {
            return true;
        }
        if (existValueFalse(searchData.getString("gameName"), recharge.getGameName())) {
            return true;
        }
        if (existValueFalse(searchData.getString("ddstate"), recharge.getDdstate().toString())) {
            return true;
        }
        return (existValueFalse(searchData.getString("roundName"), recharge.getRoundName()));
    }

    /**
     * 查询所有比赛赛制
     *
     * @return
     */
    public List<RoundExt> selectAllG() {
        List<RoundExt> roundExts = roundExtMapper.selectAllG();
        for (RoundExt roundExt : roundExts) {
            String ddCode = roundExt.getDdcode();
            String ddName = roundExt.getDdname();
            roundExt.setDdcode(ddCode + "-" + ddName);
        }
        return roundExts;
    }

    public List<RoundExt> selectAllRound() {
        List<RoundExt> roundExtS = roundExtMapper.selectAll();
        for (RoundExt roundExt : roundExtS) {
            String ddCode = roundExt.getDdcode();
            String ddName = roundExt.getDdname();
            roundExt.setDdcode(ddCode + "-" + ddName);
        }
        return roundExtS;
    }

    /**
     * 获取所有产品信息
     *
     * @return
     */
    public List<WxConfig> getAppName() {
        return wxConfigMapper.selectAll();
    }

    /**
     * 更新或者插入数据的时候统一处理数据（根据之前的方法提取的）
     *
     * @param record
     * @param type   类型判断是新增还是修改
     */
    private void updateOrInsertRoundMatch(RoundMatch record, String type) {
        String METHOD_TYPE = "update";
        String[] roundSplit = record.getRoundSelect().split("-");
        if (record.getDdgame() != null) {
            //获取游戏信息
            ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(record.getDdgame());
            if (arcadeGames.getDdispk() == 1) {
                if (roundSplit.length > 0) {
                    String ddCode = roundSplit[0];
                    record.setDdround(ddCode);
                    RoundExt roundExt = roundExtMapper.selectByddCodeG(ddCode);
                    //设置时间延迟
                    Long ddTime = roundExt.getDdtime();
                    Date ddStart = record.getDdstart();
                    Date endDate = new Date(ddStart.getTime() + ddTime * 1000 + 300 * 1000);
                    record.setDdend(endDate);
                }
            } else {
                if (roundSplit.length > 0) {
                    String ddCode = roundSplit[0];
                    record.setDdround(ddCode);
                    RoundExt roundExt = roundExtMapper.selectByddCodeG(ddCode);
                    //设置时间延迟
                    Long ddTime = roundExt.getDdtime();
                    Date ddStart = record.getDdstart();
                    Date endDate = new Date(ddStart.getTime() + ddTime * 1000);
                    record.setDdend(endDate);
                }
            }
        }
        record.setDdtime(new Timestamp(System.currentTimeMillis()));
        String ddRes = record.getDdres();
        if (StringUtils.isNotBlank(ddRes)) {
            if (!ddRes.startsWith("https")) {
                String resHost = baseConfig.getResHost();
                record.setDdres(resHost + ddRes + "/");
            }
        }
    }

    /**
     * 删除
     *
     * @param jsonObject
     * @return
     */
    public PostResult delete(JSONObject jsonObject) {
        PostResult result = new PostResult();
        String ddCode = jsonObject.getString("deleteIds");
        int delete = roundMatchMapper.deleteByPrimaryKey(Integer.parseInt(ddCode));
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员！");
        }
        return result;
    }
}
