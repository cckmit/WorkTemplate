package com.fish.service;

import com.fish.dao.second.mapper.GoodsValueMapper;
import com.fish.dao.second.mapper.OrdersMapper;
import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.*;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrdersService implements BaseService<ShowOrders> {

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    GoodsValueMapper goodsValueMapper;

    @Autowired
    WxConfigMapper wxConfigMapper;

    @Override
    //查询展示所有wxconfig信息
    public List<ShowOrders> selectAll(GetParameter parameter) {
        ArrayList<ShowOrders> shows = new ArrayList<>();
        List<Orders> orders = ordersMapper.selectAll();
        for (Orders order : orders) {
            ShowOrders showOrders = new ShowOrders();
            String appId = order.getDdappid();
            Integer goodId = order.getDdgid();
            String dduId = order.getDduid();
            GoodsValue goodsValue = goodsValueMapper.selectByPrimaryKey(goodId);
            String goodnName = goodsValue.getDdname();
            String dddesc = goodsValue.getDddesc();
            showOrders.setDdDesc(dddesc);
            UserInfo userInfo = userInfoMapper.selectByDdUid(dduId);
            String userName = userInfo.getDdname();
            WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(appId);
            if (wxConfig != null) {
                String productName = wxConfig.getProductName();
                String originName = wxConfig.getOriginName();

                if (productName != null) {
                    showOrders.setProductName(productName);
                }
                if (originName != null) {
                    showOrders.setOriginName(originName);
                }

            }
            if (goodnName != null) {
                showOrders.setGoodsName(goodnName);
            }
            if (userName != null) {
                showOrders.setUserName(userName);
            }
            showOrders.setOrders(order);
            shows.add(showOrders);
        }

        return shows;
    }
    public ShowOrders singleOrder(String record) {

        //新增产品信息
        Orders order = ordersMapper.selectByPrimaryKey(record);

        ShowOrders showOrders = new ShowOrders();
        String appId = order.getDdappid();
        Integer goodId = order.getDdgid();
        String dduId = order.getDduid();
        GoodsValue goodsValue = goodsValueMapper.selectByPrimaryKey(goodId);
        String goodnName = goodsValue.getDdname();
        String dddesc = goodsValue.getDddesc();
        showOrders.setDdDesc(dddesc);
        UserInfo userInfo = userInfoMapper.selectByDdUid(dduId);
        String userName = userInfo.getDdname();
        WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(appId);
        if (wxConfig != null) {
            String productName = wxConfig.getProductName();
            String originName = wxConfig.getOriginName();

            if (productName != null) {
                showOrders.setProductName(productName);
            }
            if (originName != null) {
                showOrders.setOriginName(originName);
            }

        }
        if (goodnName != null) {
            showOrders.setGoodsName(goodnName);

        }
        if (userName != null) {
            showOrders.setUserName(userName);
        }
        showOrders.setOrders(order);

        if(showOrders != null){
            return showOrders;
        }
        return null;
    }
    //新增展示所有产品信息
    public int insert(Orders record) {


        int insert1 = 0;
        //新增产品信息
        insert1 = ordersMapper.insert(record);

        return insert1;
    }


    //更新产品信息
    public int updateByPrimaryKeySelective(Orders record) {

        return ordersMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("productName");
        parameter.setOrder("desc");
    }

    @Override
    public Class<ShowOrders> getClassInfo() {
        return ShowOrders.class;
    }

    @Override
    public boolean removeIf(ShowOrders orders, Map<String, String> searchData) {

        if (existValueFalse(searchData.get("productName"), orders.getProductName())) {
            return true;
        }
        if (existValueFalse(searchData.get("tradeNumber"), orders.getOrders().getDdorder())) {
            return true;
        }
        if (existValueFalse(searchData.get("uid"), orders.getProductName())) {
            return true;
        }
        if (existValueFalse(searchData.get("userName"), orders.getUserName())) {
            return true;
        }
        if (existValueFalse(searchData.get("payState"), orders.getOrders().getDdstate())) {
            return true;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dt = simpleDateFormat.format(orders.getOrders().getDdtime());
        if (existValueFalse(searchData.get("times"),  dt)) {
            return true;
        }
//
//        if (existValueFalse(searchData.get("clear"), wxConfig.getClearCompany())) {
//            return true;
//        }
        return existValueFalse(searchData.get("openID"), orders.getOrders().getDdoid());
    }
}
