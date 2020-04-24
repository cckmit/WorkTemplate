package com.code.ui.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.code.dao.MiniGamebackDao;
import com.code.dao.MiniPersieValueDao;
import com.code.entity.Minitj_daily;
import com.code.entity.Persie_value;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;

import javax.servlet.annotation.WebServlet;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

@WebServlet(urlPatterns = {"/minitj_daily", "/pages/minitj_daily"})
public class MinitjdailyServlet extends UIMoudleServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 新增数据
     *
     * @param <T>
     * @param t
     */
    protected void newData(Object t) {
        try {
            Minitj_daily model = (Minitj_daily) t;
            MiniGamebackDao.instance.saveOrUpdate(model);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 修改数据
     *
     * @param <T>
     * @param t
     */
    protected void editData(Object t) {
        try {
            Minitj_daily model = (Minitj_daily) t;
            MiniGamebackDao.instance.saveOrUpdate(model);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 删除数据
     *
     * @param <T>
     * @param parameter
     */
    protected void deleteData(Object t) {
        Minitj_daily model = (Minitj_daily) t;
        String deleteSQL = "delete from minitj_daily where daily_date="
                + model.daily_date;
        MiniGamebackDao.instance.execSQLCMDInfo(deleteSQL);
    }

    @Override
    public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (String key : keySet) {
            if ("daily_date_s".equals(key)) {
                sb.append(" AND daily_date>='" + map.get(key) + "' ");
            } else if ("daily_date_d".equals(key)) {
                sb.append(" AND daily_date<='" + map.get(key) + "' ");
            } else {
                sb.append(" AND " + key + " LIKE '%" + map.get(key) + "%' ");
            }
        }
        return sb.toString();
    }

    @Override
    public String getOrderSql() {
        return " ORDER BY daily_date DESC ";
    }

    public Class<?> getClassInfo() {
        return Minitj_daily.class;
    }

    protected Type getTypeof() {
        // TODO Auto-generated method stub
        return new TypeToken<Vector<Minitj_daily>>() {
        }.getType();
    }

    @SuppressWarnings("unchecked")
    public Vector<Minitj_daily> findData() {
        Vector<Minitj_daily> list = (Vector<Minitj_daily>) MiniGamebackDao.instance
                .findBySQL(getSQL(), Minitj_daily.class);
        //查询插屏数据
        Vector<Persie_value> screenList = MiniPersieValueDao.instance.findBySQL("SELECT appId AS wx_appid,SUM(income) AS wx_screen_income,SUM(exposureCount) AS wx_screen_show,SUM(clickCount) AS wx_screen_click, DATE AS  wx_date FROM persie_value.ad_value_wx_adunit WHERE  slotId='3030046789020061'  GROUP BY DATE ", Persie_value.class);
        Map<String, Persie_value> screenMap = new HashMap<>(16);
        for (Persie_value persieValue : screenList) {
            screenMap.put(persieValue.wx_date, persieValue);
        }
        //拼接插屏日收入数据
        dataDeal(list, screenMap);
        Minitj_daily.addSumLine(list);
        return list;
    }

    /**
     * 拼接插屏收入
     *
     * @param list      数据集合
     * @param screenMap 插屏数据集合
     */
    private void dataDeal(Vector<Minitj_daily> list, Map<String, Persie_value> screenMap) {
        for (Minitj_daily general : list) {
            Persie_value persieValue = screenMap.get(general.daily_date);
            //数据赋值
            if (persieValue != null) {
                general.daily_income_screen = persieValue.wx_screen_income;
                general.daily_income_ad = general.daily_income_ad.add(general.daily_income_screen);
            }
        }
    }

}
