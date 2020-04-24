package com.code.ui.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.code.dao.MiniGamebackDao;
import com.code.dao.MiniPersieValueDao;
import com.code.entity.Minitj_general;
import com.code.entity.Persie_value;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
import com.tools.XwhTool;

import javax.servlet.annotation.WebServlet;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

@WebServlet(urlPatterns = {"/minitj_general", "/pages/minitj_general"})
public class MinitjgeneralServlet extends UIMoudleServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 新增数据
     *
     * @param <T>
     * @param t
     */
    protected void newData(Object t) {
        try {
            Minitj_general model = (Minitj_general) t;
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
            Minitj_general model = (Minitj_general) t;
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
            date = " wx_date='" + XwhTool.getPriDateWithChar() + "'";
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
            } else if ("game_spread".equals(key)) {
                sb.append("AND wx_appid in (SELECT game_appid FROM mini_game WHERE game_spread='" + map.get(key) + "')");
            } else {
                sb.append(" AND " + key + " LIKE '%" + map.get(key) + "%' ");
            }
        }
        return sb.toString();
    }

    @Override
    protected String getSelectData() {
        String sql =
                "SELECT a.*,b.`game_name`,c.manual_outgo FROM `minitj_wx` a " +
                        "LEFT JOIN `mini_game` b ON a.`wx_appid`=b.`game_appid` " +
                        "LEFT JOIN `minitj_manual` c ON b.`game_id`=c.`manual_gameid` AND a.wx_date=c.manual_date ";
        return sql;
    }

    public Class<?> getClassInfo() {
        return MiniGamebackDao.class;
    }

    protected Type getTypeof() {
        // TODO Auto-generated method stub
        return new TypeToken<Vector<Minitj_general>>() {
        }.getType();
    }

    @SuppressWarnings("unchecked")
    public Vector<Minitj_general> findData() {
        //查询时间处理
        String wxDateS = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        String wxDateE = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        JSONObject jsonDate = JSON.parseObject(get("search-data"));
        if (jsonDate != null) {
            wxDateS = (String) jsonDate.get("wx_date_s");
            wxDateE = (String) jsonDate.get("wx_date_e");
        }
        Vector<Minitj_general> list = (Vector<Minitj_general>) MiniGamebackDao.instance
                .findBySQL(getSQL(), Minitj_general.class);

        //查询插屏数据
        Vector<Persie_value> screenList = MiniPersieValueDao.instance.findBySQL("SELECT appId as wx_appid,SUM(income) AS wx_screen_income,SUM(exposureCount) AS wx_screen_show,SUM(clickCount) as wx_screen_click, DATE as  wx_date FROM persie_value.ad_value_wx_adunit WHERE DATE(DATE) BETWEEN '" + wxDateS + "' and '" + wxDateE + "' AND slotId='3030046789020061'  GROUP BY DATE ,appId", Persie_value.class);
        Map<String, Persie_value> screenMap = new HashMap<>(16);
        for (Persie_value persieValue : screenList) {
            screenMap.put(persieValue.wx_appid + "-" + persieValue.wx_date, persieValue);
        }
        //拼接插屏收入数据
        dataDeal(list, screenMap);
        for (Minitj_general general : list) {
		/*	if(general.wx_banner_income.compareTo(BigDecimal.ZERO)!=0)
			{*/
            general.totalIncome = general.wx_video_income.add(general.wx_banner_income);
            //	}
            if (general.wx_screen_income != null) {
                general.totalIncome = general.totalIncome.add(general.wx_screen_income);
            }
            if (general.wx_active != 0) {
                if (general.totalIncome == null) {
                    general.totalIncome = new BigDecimal(0);
                }
                general.activeUp = general.totalIncome.divide(new BigDecimal(general.wx_active), 3, BigDecimal.ROUND_HALF_UP);
            }
            if (general.wx_reg_ad != 0) {
                if (general.manual_outgo == null) {
                    general.manual_outgo = new BigDecimal("0.00");
                }
                general.unit_price = general.manual_outgo.divide(new BigDecimal(general.wx_reg_ad), 2, BigDecimal.ROUND_HALF_UP);
            }
        }
        Minitj_general.addSumLine(list);
        return list;
    }

    /**
     * 拼接插屏收入
     *
     * @param list      数据集合
     * @param screenMap 插屏数据集合
     */
    private void dataDeal(Vector<Minitj_general> list, Map<String, Persie_value> screenMap) {
        for (Minitj_general general : list) {
            Persie_value persieValue = screenMap.get(general.wx_appid + "-" + general.wx_date);
            //数据赋值
            if (persieValue != null) {
                general.wx_screen_income = persieValue.wx_screen_income;
            }
        }
    }

}
