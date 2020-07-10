package com.cc.manager.modules.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.sys.entity.Role;
import com.cc.manager.modules.sys.mapper.RoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 菜单 Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 23:17
 */
@Service
public class RoleService extends BaseCrudService<Role, RoleMapper> {

    private MenuService menuService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Role> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String roleName = queryObject.getString("roleName");
            queryWrapper.like(StringUtils.isNotBlank(roleName), "role_name", roleName);
        }
    }

    /**
     * 实现角色的菜单树
     *
     * @param entity 查询结果数据对象
     */
    @Override
    protected void rebuildSelectedEntity(Role entity) {
        if (entity.getId() != 1) {
            entity.setMenuTreeJson(this.menuService.getRoleMenuTree(entity.getMenuIds()));
        }
    }

    /**
     * 查询列表的时候，就不实现菜单树了
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<Role> entityList) {

    }

    /**
     * 更新角色的菜单权限
     *
     * @param requestParam 更新参数
     * @return 更新结果
     */
    public PostResult updateMenuIds(String requestParam) {
        PostResult postResult = new PostResult();
        if (StringUtils.isNotBlank(requestParam)) {
            JSONObject requestObject = JSONObject.parseObject(requestParam);
            if (Objects.nonNull(requestObject)) {
                int id = requestObject.getInteger("id");
                // 管理员权限不允许修改
                if (id == 1) {
                    postResult.setCode(2);
                    postResult.setMsg("更新数据失败，管理员角色权限不许修改！");
                } else {
                    UpdateWrapper<Role> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.set("menu_ids", requestObject.getString("menuIds"));
                    updateWrapper.eq("id", id);
                    if (!this.update(updateWrapper)) {
                        postResult.setCode(2);
                        postResult.setMsg("更新数据失败，更新数据库失败，请联系技术人员！");
                    }
                }
            } else {
                postResult.setCode(2);
                postResult.setMsg("更新数据失败，提交参数解析异常，请联系技术人员！");
            }
        }
        return postResult;
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Role> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = JSONObject.parseArray(requestParam, String.class);
            // 删除时移除管理员账号ID
            idList.remove("1");
            if (!idList.isEmpty()) {
                return this.removeByIds(idList);
            }
        }
        return false;
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

}
