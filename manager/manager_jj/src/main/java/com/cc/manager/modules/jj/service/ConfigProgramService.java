package com.cc.manager.modules.jj.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.ConfigProgram;
import com.cc.manager.modules.jj.entity.GameSet;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.ConfigProgramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class ConfigProgramService extends BaseCrudService<ConfigProgram, ConfigProgramMapper> {
    private WxConfigService wxConfigService;
    private GameSetService gameSetService;
    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigProgram> queryWrapper) {
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<ConfigProgram> entityList) {

        for (ConfigProgram configProgram : entityList) {
            String ddAppId = configProgram.getDdAppId();
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, ddAppId);
            if (wxConfig != null) {
                configProgram.setProductName(wxConfig.getProductName());
                configProgram.setProgramType(wxConfig.getProgramType());
            }
            GameSet gameSet = this.gameSetService.getCacheEntity(GameSet.class, configProgram.getDdCode().toString());
            if (gameSet != null) {
                configProgram.setCodeName(gameSet.getDdName());
            }
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, ConfigProgram entity) {

    }

    @Override
    protected boolean update(String requestParam, ConfigProgram entity, UpdateWrapper<ConfigProgram> updateWrapper) {
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigProgram> deleteWrapper) {
        return false;
    }
    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }
    @Autowired
    public void setGameSetService(GameSetService gameSetService) {
        this.gameSetService = gameSetService;
    }
}
