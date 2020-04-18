package com.code.ui.servlet;

import com.code.dao.MiniGamebackDao;
import com.code.dao.MiniPersieValueDao;
import com.code.entity.Minitj_income;
import com.code.entity.Persie_value;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.XwhTool;

import javax.servlet.annotation.WebServlet;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(urlPatterns = {"/minitj_income", "/pages/minitj_income"})
public class MinitjincomeServlet extends UIMoudleServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 新增数据
     *
     * @param <T>
     * @param t
     */
    protected void newData(Object t) {
        try {
            Minitj_income model = (Minitj_income) t;
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
            Minitj_income model = (Minitj_income) t;
            MiniGamebackDao.instance.saveOrUpdate(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getInitWhere() {
        String searchdata = get("search-data");
        String date = " 1=1 ";
        if (searchdata == null || "".equals(searchdata)) {
            date = "  wx_date='" + XwhTool.getPriDateWithChar() + "'";
        }
        return date;
    }

    @Override
    public String getGivenSearch(Set<String> keySet, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (String key : keySet) {
            if ("gameappid".equals(key)) {
                sb.append(" AND wx_appid= (SELECT game_appid FROM mini_game WHERE game_appid='" + map.get(key) + "' limit 0,1)");
            } else if ("wx_date_s".equals(key)) {
                sb.append(" AND wx_date>='" + map.get(key) + "'");
            } else if ("wx_date_e".equals(key)) {
                sb.append(" AND wx_date<='" + map.get(key) + "'");
            } else {
                sb.append(" AND " + key + " LIKE '%" + map.get(key) + "%' ");
            }
        }

        return sb.toString();
    }

    @Override
    protected String getSelectData() {
        String sql =
                "SELECT a.*,b.`game_name`,c.runtime_videoreq FROM `minitj_wx` a " +
                        "LEFT JOIN `mini_game` b ON a.`wx_appid`=b.`game_appid` " +
                        "LEFT JOIN `minitj_runtime` c ON b.`game_id`=c.`runtime_gameid` AND a.wx_date=c.runtime_date ";
        return sql;
    }

    public Class<?> getClassInfo() {
        return MiniGamebackDao.class;
    }

    protected Type getTypeof() {
        // TODO Auto-generated method stub
        return new TypeToken<Vector<Minitj_income>>() {
        }.getType();
    }

    @SuppressWarnings("unchecked")
    public Vector<Minitj_income> findData() {
        Date nowtime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(nowtime);
        Vector<Minitj_income> list = (Vector<Minitj_income>) MiniGamebackDao.instance
                .findBySQL(getSQL(), Minitj_income.class);

        Vector<Persie_value> screenList = (Vector<Persie_value>) MiniPersieValueDao.instance.findBySQL("SELECT appId as wx_appid,SUM(income) AS wx_screen_income,SUM(exposureCount) AS wx_screen_show,SUM(clickCount) as wx_screen_click, DATE as  wx_date FROM persie_value.ad_value_wx_adunit WHERE DATE(DATE) BETWEEN '2020-04-16' and '2020-04-16' AND slotId='3030046789020061'  GROUP BY DATE ,appId", Persie_value.class);
        Map<String, Persie_value> screenMap = new HashMap<>(16);
        Persie_value.addSum(screenList);
        for (Persie_value persieValue : screenList) {
            screenMap.put(persieValue.wx_appid + "-" + persieValue.wx_date, persieValue);
        }

        Minitj_income.addSumLine(list);
        for (Minitj_income general : list) {
            Persie_value persieValue = screenMap.get(general.wx_appid + "-" + general.wx_date);
            if("wx0f42a37e32eb59b5".equals(general.wx_appid)){
                System.out.println(general.wx_date);
            }
            if (persieValue != null) {
                general.wx_screen_show = persieValue.wx_screen_show;
                general.wx_screen_clickrate = persieValue.wx_screen_clickrate;
                general.wx_screen_income = persieValue.wx_screen_income;
                general.screenEcpm = persieValue.screenEcpm;
            }
			/*if(general.wx_banner_income.compareTo(BigDecimal.ZERO)!=0)
			{*/
            general.totalIncome = general.wx_video_income.add(general.wx_banner_income);
            //	}
            if (general.wx_video_show != 0) {
                general.videoEcpm = general.wx_video_income.multiply(new BigDecimal(1000)).setScale(2, BigDecimal.ROUND_HALF_UP)
                        .divide(new BigDecimal(general.wx_video_show), 2, BigDecimal.ROUND_HALF_UP);
            }
            if (general.wx_banner_show != 0) {
                general.bannerEcpm = general.wx_banner_income.multiply(new BigDecimal(1000)).setScale(2, BigDecimal.ROUND_HALF_UP)
                        .divide(new BigDecimal(general.wx_banner_show), 2, BigDecimal.ROUND_HALF_UP);
            }
        }

        return list;
    }

}
