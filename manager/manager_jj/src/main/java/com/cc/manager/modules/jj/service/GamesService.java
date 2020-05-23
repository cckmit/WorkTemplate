package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.mapper.GamesMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author cf
 * @since 2020-05-08
 */
@Service
@DS("jj")
public class GamesService extends BaseCrudService<Games, GamesMapper> {

    private JjConfig jjConfig;

    /**
     * 读入一个文本的方法
     *
     * @param path 路径信息
     * @return 得到的文本信息
     */
    private static String readFileString(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            return readInputStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取流数据
     */
    private static String readInputStream(InputStream in) {
        try {
            int length = in.available();
            byte[] bytes = new byte[length];
            in.read(bytes);
            in.close();
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Games> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String gameId = queryObject.getString("gameId");
            queryWrapper.eq(StringUtils.isNotBlank(gameId), "ddCode", gameId);
            String gameName = queryObject.getString("gameName");
            queryWrapper.like(StringUtils.isNotBlank(gameName), "ddName", gameName);
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, Games entity) {

    }

    @Override
    protected boolean update(String requestParam, Games entity, UpdateWrapper<Games> updateWrapper) {
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Games> deleteWrapper) {
        return false;
    }

    /**
     * 刷新游戏配置资源
     *
     * @param parameter parameter
     * @return PostResult
     */
    public PostResult flushGamesResources(JSONArray parameter) {
        PostResult postResult = new PostResult();
        for (int i = 0; i < parameter.size(); i++) {
            int gameCode = parameter.getInteger(i);
            Games game = this.getById(gameCode);
            try {
                if (game != null) {
                    String resPath = this.jjConfig.getReadRes();
                    String share = readFileString(resPath.concat("g" + gameCode).concat("/share/readme.json"));
                    if (share != null) {
                        game.setDdShareRes(share);
                        this.updateById(game);
                    }
                }
            } catch (Exception e) {
                LOGGER.error(ExceptionUtils.getStackTrace(e));
                postResult.setCode(2);
            }
        }
        return postResult;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

}
