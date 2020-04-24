package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.utils.HexToStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 合集配置  Service
 *
 * @author
 * @date
 */
@Service
public class GamesSetService implements BaseService<ArcadeGameSet> {
    @Autowired
    com.fish.dao.primary.mapper.ArcadeGameSetMapper arcadeGameSetMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    WxConfigService wxConfigService;


    /**
     * 查询
     *
     * @param parameter
     * @return
     */
    @Override
    public List<ArcadeGameSet> selectAll(GetParameter parameter) {
        List<ArcadeGameSet> arcadeGameSets;
        ConcurrentHashMap<String, WxConfig> wxConfigMap = wxConfigService.getAll(WxConfig.class);
        //手动设值升序降序
        if ("asc".equals(parameter.getOrder())) {
            arcadeGameSets = arcadeGameSetMapper.selectAllByAsc();
        } else if ("desc".equals(parameter.getOrder())) {
            arcadeGameSets = arcadeGameSetMapper.selectAllByDesc();
        } else {
            arcadeGameSets = arcadeGameSetMapper.selectAll();
        }
        for (ArcadeGameSet arcadeGameSet : arcadeGameSets) {
            String content = arcadeGameSet.getDdcontent512a();
            StringBuilder ddContents = new StringBuilder();
            List<String> gameBox = new ArrayList<>();
            try {
                //处理展示游戏合集内容,把#转化为,
                String[] split = content.split("#");
                for (int i = 0; i < split.length; i++) {
                    String ddCode = split[i];
                    if (StringUtils.isNotBlank(ddCode)) {
                        gameBox.add(ddCode);
                        ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(Integer.valueOf(ddCode));
                        String ddName = StringUtils.isBlank(arcadeGames.getDdname()) ? "" : arcadeGames.getDdname();
                        if (ddContents.length() > 0) {
                            ddContents.append(",");
                        }
                        ddContents.append(ddName);
                    }
                    //此处过滤只有一个游戏集合防止传入数据[""]
                    if (split.length == 1) {
                        arcadeGameSet.setSelect(gameBox.toString().substring(1, gameBox.toString().length() - 1));
                    } else {
                        arcadeGameSet.setSelect(gameBox.toString());
                    }
                }
            } catch (Exception e) {
                LOGGER.error("查询GamesSetService失败" + ", 详细信息:{}", e.getMessage());
            }
            arcadeGameSet.setDdcontent512a(ddContents.toString());
            if (StringUtils.isNotBlank(arcadeGameSet.getDdappid())) {
                WxConfig wxConfigName = wxConfigMap.get(arcadeGameSet.getDdappid());
                if (wxConfigName != null) {
                    if (StringUtils.isNotBlank(wxConfigName.getProductName())) {
                        arcadeGameSet.setDdappid(arcadeGameSet.getDdappid() + "-" + wxConfigName.getProductName());
                    } else {
                        arcadeGameSet.setDdappid(arcadeGameSet.getDdappid());
                    }
                }
            }
        }
        return arcadeGameSets;
    }

    /**
     * 新增
     *
     * @param record
     * @return
     */
    public int insert(ArcadeGameSet record) {
        //新增判断是否游戏编号重复
        ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(record.getDdcode());
        if (!org.springframework.util.StringUtils.isEmpty(arcadeGameSet)) {
            return 5;
        }
        //获取提交的合集下拉框数据
        String select = record.getSelect();
        String[] split = select.split(",");
        List<String> gameBox = new ArrayList<>(Arrays.asList(split));
        StringBuilder ddContents = new StringBuilder();
        StringBuilder ddName = new StringBuilder();
        if (gameBox.size() >= 1) {
            for (String box : gameBox) {
                if (!"".equals(box)) {
                    ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(Integer.valueOf(box));
                    String ddname = arcadeGames.getDdname();
                    if (ddName.length() > 0) {
                        ddName.append(",");
                    }
                    ddName.append(ddname);
                    if (ddContents.length() > 0) {
                        ddContents.append("#");
                    }
                    ddContents.append(box);
                }
            }
        }
        record.setDdcontent512a(ddContents.toString());
        record.setDddesc512u(HexToStringUtil.getStringToHex(ddName.toString()));
        record.setDddesc(ddName.toString());
        return arcadeGameSetMapper.insert(record);
    }

    /**
     * 修改
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(ArcadeGameSet record) {
        List<ArcadeGameSet> arcadeGameSets = arcadeGameSetMapper.selectAll();
        List<ArcadeGameSet> arcadeGameSetOthers = new ArrayList<>();
        for (ArcadeGameSet arcadeGameSetSingle : arcadeGameSets) {
            if (!record.getId().equals(arcadeGameSetSingle.getId())) {
                arcadeGameSetOthers.add(arcadeGameSetSingle);
            }
        }
        for (ArcadeGameSet arcadeGameSetOther : arcadeGameSetOthers) {
            if (record.getDdcode().equals(arcadeGameSetOther.getDdcode())) {
                return 5;
            }
        }
        String select = record.getSelect();
        String[] split = select.split(",");
        List<String> gameBox = new ArrayList<>(Arrays.asList(split));
        StringBuilder ddContents = new StringBuilder();
        StringBuilder ddName = new StringBuilder();
        if (gameBox.size() >= 1) {
            for (String box : gameBox) {
                if (!"".equals(box)) {
                    ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(Integer.valueOf(box));
                    String ddname = arcadeGames.getDdname();
                    if (ddName.length() > 0) {
                        ddName.append(",");
                    }
                    ddName.append(ddname);
                    if (ddContents.length() > 0) {
                        ddContents.append("#");
                    }
                    ddContents.append(box);
                }
            }
        }
        record.setDdcontent512a(ddContents.toString());
        record.setDddesc(ddName.toString());
        return arcadeGameSetMapper.updateByPrimaryKeySelective(record);
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
    public Class<ArcadeGameSet> getClassInfo() {
        return ArcadeGameSet.class;
    }

    @Override
    public boolean removeIf(ArcadeGameSet arcadeGameSet, JSONObject searchData) {
        if (existValueFalse(searchData.getString("gameId"), arcadeGameSet.getDdcode())) {
            return true;
        }
        return (existValueFalse(searchData.getString("gameName"), arcadeGameSet.getDdname()));
    }

}
