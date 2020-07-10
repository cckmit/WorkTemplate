package com.cc.manager.modules.sys.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.sys.entity.Menu;
import com.cc.manager.modules.sys.entity.Role;
import com.cc.manager.modules.sys.entity.User;
import com.cc.manager.modules.sys.mapper.MenuMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单操作逻辑
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:21
 */
@Service
public class MenuService extends BaseCrudService<Menu, MenuMapper> {

    private UserService userService;
    private RoleService roleService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Menu> queryWrapper) {
    }

    @Override
    protected void updateInsertEntity(String requestParam, Menu entity) {
        updateOrderNum(entity);
    }

    @Override
    protected boolean update(String requestParam, Menu entity, UpdateWrapper<Menu> updateWrapper) {
        updateOrderNum(entity);
        return super.update(requestParam, entity, updateWrapper);
    }

    @Override
    public List<Menu> list() {
        // 先查询全部菜单列表，按照父菜单ID和排序顺序查询，这里排序非常重要，影响整个树构建的逻辑
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("parent_id", "order_num");
        return this.list(queryWrapper);
    }

    /**
     * 如果当前传入的排序数值小于等于0，将菜单添加到最后面
     *
     * @param entity 菜单数据
     */
    private void updateOrderNum(Menu entity) {
        if (entity.getOrderNum() <= 0) {
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", entity.getParentId());
            queryWrapper.orderByDesc("order_num");
            queryWrapper.last("LIMIT 1");
            int existOrderNum = 0;
            Menu existMenu = this.getOne(queryWrapper);
            if (existMenu != null) {
                existOrderNum = existMenu.getOrderNum();
            }
            entity.setOrderNum(existOrderNum + 1);
        }
    }

    /**
     * 递归删除当前菜单及子菜单
     *
     * @param requestParam  请求的参数
     * @param deleteWrapper 删除Wrapper
     * @return 删除结果
     */
    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Menu> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<Integer> idList = JSONObject.parseArray(requestParam, Integer.class);
            // 根菜单不允许删除
            idList.remove(Integer.valueOf(-1));
            if (!idList.isEmpty()) {
                // 递归查询所有子菜单
                List<Integer> collectIdList = Lists.newCopyOnWriteArrayList(idList);
                this.queryChildIdList(idList, collectIdList);
                return this.removeByIds(collectIdList);
            }
        }
        return false;
    }

    /**
     * 递归查询所有子菜单
     *
     * @param parentIdList  父ID
     * @param collectIdList 存储全部ID列表
     */
    private void queryChildIdList(List<Integer> parentIdList, List<Integer> collectIdList) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("parent_id", parentIdList);
        List<Menu> menuList = this.list(queryWrapper);
        if (menuList != null) {
            parentIdList.clear();
            for (Menu menu : menuList) {
                parentIdList.add(menu.getId());
                collectIdList.add(menu.getId());
                queryChildIdList(parentIdList, collectIdList);
            }
        }
        System.out.println(collectIdList);
    }

    /**
     * 查询树形菜单，编辑和新增子菜单使用
     *
     * @param clazz        查询下拉框数据类
     * @param requestParam 过滤参数
     * @return layui-xm-select树形下拉框
     */
    @Override
    public JSONObject getSelectArray(Class<Menu> clazz, String requestParam) {
        JSONObject requestObject = JSON.parseObject(requestParam);
        String idType = requestObject.getString("idType");
        int selectedId = requestObject.getInteger("id");
        // 如果是新增子菜单，则当前传入的即为选中的父菜单
        if (StringUtils.equals("id", idType)) {
            Menu menu = this.getById(requestObject.getInteger("id"));
            selectedId = menu.getParentId();
        }

        JSONObject resultObject = new JSONObject();
        List<Menu> menuList = this.list();

        // 将菜单属性处理成Json对象
        Map<Integer, JSONObject> menuMap = new HashMap<>();
        for (Menu menu : menuList) {
            JSONObject menuObject = new JSONObject();
            menuObject.put("value", menu.getId());
            menuObject.put("name", menu.getName());
            menuObject.put("selected", menu.getId() == selectedId);
            menuMap.put(menu.getId(), menuObject);
            // 非根菜单放入父菜单，由于查询时已经做好了排序，所以一定会有父菜单
            if (menu.getId() != 0) {
                JSONObject parentObject = menuMap.get(menu.getParentId());
                if (parentObject != null) {
                    JSONArray childrenArray = parentObject.getJSONArray("children");
                    if (childrenArray == null) {
                        childrenArray = new JSONArray();
                    }
                    childrenArray.add(menuObject);
                    parentObject.put("children", childrenArray);
                }
            }
        }

        // 添加根菜单
        JSONArray rootArray = new JSONArray();
        rootArray.add(menuMap.get(0));
        resultObject.put("data", rootArray);
        resultObject.put("code", 1);
        return resultObject;
    }

    /**
     * 获取角色的菜单树
     *
     * @param menuIds 角色菜单列表
     * @return 角色菜单树
     */
    public JSONObject getRoleMenuTree(String menuIds) {
        List<String> menuIdList = new ArrayList<>();
        // #表示管理员权限，不允许修改
        if (StringUtils.isNotBlank(menuIds)) {
            menuIdList = JSONObject.parseArray(menuIds, String.class);
        }

        // 先查询全部菜单列表，按照父菜单ID和排序顺序查询，这里排序非常重要，影响整个树构建的逻辑
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("parent_id", "order_num");
        List<Menu> menuList = this.list(queryWrapper);

        // 将菜单属性处理成Json对象
        Map<Integer, JSONObject> menuMap = new HashMap<>();

        for (Menu menu : menuList) {
            JSONObject menuObject = new JSONObject();
            menuObject.put("id", menu.getId());
            menuObject.put("title", menu.getName());
            menuObject.put("spread", true);
            // -1表示管理员默认全部权限
            menuObject.put("checked", menuIdList.contains(String.valueOf(menu.getId())) || menuIdList.contains("-1"));
            addChildren(menuMap, menu, menuObject);
        }
        System.out.println(menuMap.get(0).toJSONString());
        // 返回根菜单
        return menuMap.get(0);
    }

    /**
     * 菜单追加处理
     *
     * @param menuMap    菜单map
     * @param menu       菜单对象
     * @param menuObject 菜单对象Json
     */
    private void addChildren(Map<Integer, JSONObject> menuMap, Menu menu, JSONObject menuObject) {
        menuMap.put(menu.getId(), menuObject);
        // 非根菜单放入父菜单，由于查询时已经做好了排序，所以一定会有父菜单
        if (menu.getId() != 0) {
            JSONObject parentObject = menuMap.get(menu.getParentId());
            if (parentObject != null) {
                JSONArray childrenArray = parentObject.getJSONArray("children");
                if (childrenArray == null) {
                    childrenArray = new JSONArray();
                }
                childrenArray.add(menuObject);
                // 这里是layui树组件渲染的bug，如果父节点有选中，渲染时会选中所有子节点，所以如果子节点的话，父节点checked全部设置为false
                parentObject.put("checked", false);
                parentObject.put("children", childrenArray);
            }
        }
    }

    /**
     * 行内编辑方法，只修改部分参数
     *
     * @param requestParam 请求参数
     * @return 修改结果
     */
    public PostResult tableEdit(String requestParam) {
        PostResult postResult = new PostResult();
        if (StringUtils.isNotBlank(requestParam)) {
            JSONObject requestObject = JSON.parseObject(requestParam);
            UpdateWrapper<Menu> updateWrapper = new UpdateWrapper<>();
            String orderNum = requestObject.getString("orderNum");
            String href = requestObject.getString("href");
            String perms = requestObject.getString("perms");
            updateWrapper.set(StringUtils.isNotBlank(orderNum), "order_num", orderNum);
            updateWrapper.set(StringUtils.isNotBlank(href), "href", href);
            updateWrapper.set(StringUtils.isNotBlank(perms), "perms", perms);
            updateWrapper.eq("id", requestObject.getInteger("id"));
            if (!this.update(updateWrapper)) {
                postResult.setCode(2);
                postResult.setMsg("更新数据失败！");
            }
        }
        return postResult;
    }

    /**
     * 通过登录用户，获取用户权限菜单
     *
     * @param userId 用户ID
     * @return 授权菜单
     */
    public JSONObject getMenuJson(int userId) {
        JSONObject authenticatedObject = new JSONObject();
        User user = this.userService.getById(userId);
        if (StringUtils.isNotBlank(user.getRoleIds())) {
            List<String> roleIdList = JSONObject.parseArray(user.getRoleIds(), String.class);
            List<Role> roleList = this.roleService.listByIds(roleIdList);
            Set<String> menuIdSet = new HashSet<>();
            roleList.forEach(role -> {
                if (StringUtils.isNotBlank(role.getMenuIds())) {
                    menuIdSet.addAll(JSONArray.parseArray(role.getMenuIds(), String.class));
                }
            });

            // 查询全部菜单
            List<Menu> menuList = this.list();
            // 将菜单属性处理成Json对象

            Map<Integer, JSONObject> menuMap = new HashMap<>();
            menuList.forEach(menu -> {
                // 判断方式：有授权或者是管理员，同时不是按钮
                boolean haveAuthenticated = (menuIdSet.contains(String.valueOf(menu.getId())) || roleIdList.contains(
                        "1")) && menu.getType() == 0;
                if (haveAuthenticated) {
                    JSONObject menuObject = new JSONObject();
                    menuObject.put("title", menu.getName());
                    menuObject.put("href", menu.getHref());
                    menuObject.put("icon", StringUtils.isNotBlank(menu.getIcon()) ? "fa " + menu.getIcon() : "");
                    menuObject.put("target", "_self");

                    menuMap.put(menu.getId(), menuObject);
                    // 非根菜单放入父菜单，由于查询时已经做好了排序，所以一定会有父菜单
                    if (menu.getId() != 0) {
                        JSONObject parentObject = menuMap.get(menu.getParentId());
                        if (parentObject != null) {
                            JSONArray childrenArray = parentObject.getJSONArray("child");
                            if (childrenArray == null) {
                                childrenArray = new JSONArray();
                            }
                            childrenArray.add(menuObject);
                            parentObject.put("child", childrenArray);
                        }
                    }
                }
            });
            authenticatedObject.put("menuInfo", menuMap.get(0).getJSONArray("child"));
        }
        // 返回根菜单
        JSONObject homeInfo = new JSONObject();
        homeInfo.put("title", "首页");
        homeInfo.put("href", "page/home.html?t=1");
        authenticatedObject.put("homeInfo", homeInfo);

        JSONObject logoInfo = new JSONObject();
        logoInfo.put("title", "街机管理后台");
        logoInfo.put("image", "images/logo.png");
        logoInfo.put("href", "");
        authenticatedObject.put("logoInfo", logoInfo);

        authenticatedObject.put("code", 1);
        return authenticatedObject;
    }

    /**
     * 获取菜单配置表
     *
     * @return 菜单配置页面数据Json
     */
    public JSONObject getMenuTable() {
        JSONObject resultObject = new JSONObject();
        List<Menu> menuList = this.list();
        Map<Integer, JSONObject> menuMap = new HashMap<>();
        for (Menu menu : menuList) {
            JSONObject menuObject = new JSONObject();
            menuObject.put("id", menu.getId());
            menuObject.put("name", menu.getName());
            menuObject.put("type", menu.getType());
            menuObject.put("orderNum", menu.getOrderNum());
            menuObject.put("href", menu.getHref());
            menuObject.put("icon", menu.getIcon());
            menuObject.put("perms", menu.getPerms());
            addChildren(menuMap, menu, menuObject);
        }
        resultObject.put("code", 1);
        resultObject.put("data", menuMap.get(0).getJSONArray("children"));
        return resultObject;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


}
