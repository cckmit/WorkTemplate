package com.cc.manager.modules.sys.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.GetPageParam;
import com.cc.manager.modules.sys.mapper.MenuMapper;
import com.cc.manager.modules.sys.entity.Menu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单操作逻辑
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:21
 */
@Service
public class MenuService extends BaseCrudService<Menu, MenuMapper> {

    @Override
    protected void updateGetPageWrapper(GetPageParam getPageParam, QueryWrapper<Menu> queryWrapper) {

    }

    @Override
    protected void updateInsertEntity(String requestParam, Menu entity) {

    }

    @Override
    protected boolean update(String requestParam, Menu entity, UpdateWrapper<Menu> updateWrapper) {
        return this.saveOrUpdate(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Menu> deleteWrapper) {
        return false;
    }

    /**
     * 获取菜单TreeTable结构Json
     *
     * @return
     */
    public JSONObject getMenuTreeTableJson() {
        JSONObject menuTreeTableJson = new JSONObject();
        menuTreeTableJson.put("code", 0);
        menuTreeTableJson.put("msg", "");

        // 查询全部菜单列表
        List<Menu> allMenuList = this.mapper.selectList(new QueryWrapper<>());
        JSONArray dataArray = new JSONArray();
        allMenuList.forEach(menu -> {
            JSONObject dataObject = JSONObject.parseObject(JSONObject.toJSONString(menu));
            dataArray.add(dataObject);
        });
        menuTreeTableJson.put("data", dataArray);
        return menuTreeTableJson;
    }


    /**
     * 获取用户已授权菜单
     *
     * @param roleIds 角色列表
     * @return 已授权菜单数据
     */
    public JSONObject getUserInitInfo(String roleIds) {
        JSONObject initObject = new JSONObject();
        if (StringUtils.isNotBlank(roleIds)) {
            // 首页信息
            JSONObject homeInfo = new JSONObject();
            homeInfo.put("title", "首页");
            homeInfo.put("href", "page/welcome.html");
            initObject.put("homeInfo", homeInfo);

            // logo信息
            JSONObject logoInfo = new JSONObject();
            homeInfo.put("title", "街机/FC/校花管理后台");
            homeInfo.put("image", "images/logo.png");
            initObject.put("href", "");
            initObject.put("homeInfo", homeInfo);

            // TODO 菜单系统，目前只支持3级菜单：1,2,3，顶部为-1，菜单系统完善中……
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
            // 如果包含#表示系统管理员，授权全部页面
            if (!roleIds.contains("#")) {
                // 否则按照逗号分隔查询
                queryWrapper.in("id", (Object) StringUtils.split(roleIds, ","));
            }
            // 按照父ID升序和自身
            queryWrapper.orderByAsc("pid").orderByAsc("id");

        }
        return initObject;
    }


}
