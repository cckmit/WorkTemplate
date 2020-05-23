package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.utils.ReduceJsonUtil;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.entity.RoundExt;
import com.cc.manager.modules.jj.entity.RoundMatch;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.RoundMatchMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cf
 * @since 2020-05-08
 */
@Service
public class RoundMatchService extends BaseCrudService<RoundMatch, RoundMatchMapper> {

    private WxConfigService wxConfigService;
    private GamesService gamesService;
    private RoundExtService roundExtService;
    @Autowired
    BaseConfig baseConfig;
    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<RoundMatch> entityList) {
        String url = baseConfig.getResHost();
        for (RoundMatch roundMatch : entityList) {
            // 更新游戏信息
            Games games = this.gamesService.getCacheEntity(Games.class, roundMatch.getDdGame().toString());
            if (games == null) {
                continue;
            }
            roundMatch.setGameName(games.getDdName());
            // 判断资源分享JSON字串
            if (StringUtils.isNotBlank(games.getDdShareRes())) {
                JSONArray shareLists = JSONObject.parseArray(games.getDdShareRes());
                for (int i = 0; i < shareLists.size(); i++) {
                    JSONObject jsonObject = JSONObject.parseObject(shareLists.getString(i));
                    if (jsonObject.getInteger("position") == 4) {
                        String icon = ReduceJsonUtil.concatUrl(this.baseConfig.getResHost(),
                                jsonObject.getString("url"), "g" + games.getDdCode(), "share");
                        if (StringUtils.isNotBlank(icon)) {
                            roundMatch.setJumpDirect(icon);
                        }
                    }
                }
            }
            // 更新赛场信息
            RoundExt roundExt = this.roundExtService.getCacheEntity(RoundExt.class, roundMatch.getDdRound());
            if (roundExt != null) {
               // roundMatch.setRoundSelect(roundMatch.getDdRound() + "-" + roundExt.getDdName());
                roundMatch.setRoundName(roundExt.getDdName());
                roundMatch.setDdReward(roundExt.getDdReward());
                roundMatch.setRoundLength(roundExt.getTip());
            }
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, roundMatch.getDdAppId());
            if (wxConfig != null) {
                roundMatch.setAppName(wxConfig.getProductName());
            }
            //获取资源图片信息
            String ddRes = roundMatch.getDdRes();
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
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<RoundMatch> queryWrapper) {
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<RoundMatch> deleteWrapper) {
        return false;
    }


    @Override
    protected void updateInsertEntity(String requestParam, RoundMatch entity) {

    }
    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }
    @Autowired
    public void setGamesService(GamesService gamesService) {
        this.gamesService = gamesService;
    }
    @Autowired
    public void setRoundExtService(RoundExtService roundExtService) {
        this.roundExtService = roundExtService;
    }
}
