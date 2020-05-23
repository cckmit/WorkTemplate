package com.cc.manager.modules.sys.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.GetPageParam;
import com.cc.manager.modules.sys.entity.User;
import com.cc.manager.modules.sys.mapper.UserMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户操作逻辑
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:21
 */
@Service
public class UserService extends BaseCrudService<User, UserMapper> {

    /**
     * 用户登录，利用用户名查询用户信息，然后比较密码是否匹配
     *
     * @param username 用户名
     * @return 登录成功用户信息
     */
    public User login(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return this.getOne(queryWrapper);
    }

    @Override
    protected void updateGetPageWrapper(GetPageParam getPageParam, QueryWrapper<User> queryWrapper) {
        if (StringUtils.isNotBlank(getPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(getPageParam.getQueryData());
            String username = queryObject.getString("username");
            queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
            String nickName = queryObject.getString("nickName");
            queryWrapper.like(StringUtils.isNotBlank(nickName), "nick_name", nickName);
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param getPageParam 查询参数
     * @param entityList   查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(GetPageParam getPageParam, List<User> entityList) {
        entityList.forEach(entity -> entity.setRoleNames(entity.getRoleIds()));
    }

    @Override
    protected void updateInsertEntity(String requestParam, User entity) {
        // 对密码进行加密
        entity.setPassword(DigestUtil.md5Hex(entity.getUsername() + entity.getPassword()));
    }

    @Override
    protected boolean update(String requestParam, User entity, UpdateWrapper<User> updateWrapper) {
        // 对密码进行加密
        entity.setPassword(DigestUtil.md5Hex(entity.getUsername() + entity.getPassword()));
        // 更新数据
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<User> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = Lists.newArrayList(StringUtils.split(requestParam,","));
            // 删除时移除管理员账号ID
            idList.remove("1");
            return this.removeByIds(idList);
        }
        return false;
    }

}
