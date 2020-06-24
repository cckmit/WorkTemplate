package com.cc.manager;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.utils.RedisUtil;
import com.cc.manager.modules.jj.mapper.UserAppMapper;
import com.cc.manager.modules.jj.service.AllCostService;
import com.cc.manager.modules.jj.service.RechargeService;
import com.cc.manager.modules.jj.service.RoundExtService;
import com.cc.manager.modules.jj.service.UserInfoService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ManagerJjApplicationTests {

    @Autowired
    UserAppMapper userAppMapper;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    RechargeService RechargeService;
    @Autowired
    AllCostService allCostService;
    @Autowired
    RoundExtService roundExtService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PersieServerUtils persieServerUtils;

    @Test
    void contextLoads() {
    }

    @Test
    public void getString() {
//        QueryWrapper<UserApp> userAppQueryWrapper = new QueryWrapper<>();
//        userAppQueryWrapper.eq("ddUid", "6F8BE96F678D2BCAA5653BA58E20EB96").eq("ddAppId", 1110381534);
//        UserApp userApp = userAppMapper.selectOne(userAppQueryWrapper);
//        System.out.println(userApp.toString());
        //    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //    LocalDateTime parse = LocalDateTime.parse("2020-02-15 19:40:32", df);
        //  AllCost allCost = allCostService.selectRemainAmount("oSn_Lw88gelVi7jYCijJUAArmMIs", parse);
//        BigDecimal i = RechargeService.selectUserCashOut("oSn_Lw88gelVi7jYCijJUAArmMIs","2020-02-05");
        //  System.out.println(allCost.toString());
        // postResult = this.persieServerUtils.refreshTable("app_config");
        //   System.out.println(postResult.toString());
        FileReader fileReader = new FileReader("TableFakeUser.txt");
        System.out.println("我是文本内容"+fileReader.readString());
        JSONObject jsonObject = JSONObject.parseObject(fileReader.readString());
        JSONArray list = jsonObject.getJSONArray("list");
        for (Object o : list) {
            String head = JSONObject.parseObject(o.toString()).getString("head");
            String url = "https://tennishead.gamesmvp.com/head/"+head+".png";
            long size = HttpUtil.downloadFile(url, FileUtil.file("e:/picture/"));
            System.out.println("Download size: " + size);
        }
//        String url = "https://tennishead.gamesmvp.com/head/r1101000415.png";
//        //将文件下载后保存在E盘，返回结果为下载文件大小
//        long size = HttpUtil.downloadFile(url, FileUtil.file("e:/picture/"));
//        System.out.println("Download size: " + size);
    }

    public void sort(Integer[] array, int n) {
        if (n <= 1) {
            return;
        }
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - 1; j++) {
                if (array[j] > array[j + 1]) {
                    Integer temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }


    @Test
    public void getUserInfo() throws Exception {


//      Integer coin = (Integer) redisUtil.hashGet("user-oxDM75DNVEcHIT0eHnL-QYCMv8sY", "coin");
//      System.out.println(coin.toString());

//      UserInfo userInfo = this.userInfoService.getById("oSn_Lw5rxqPXgC3b_M0heLW5wzbA");
//      System.out.println(userInfo.toString()

    }

    @Test
    public void getRecharge() {
//        List<Recharge> recharges = rechargeMapper.selectAllCharge(null,null);
//        for (Recharge recharge : recharges) {
//            System.out.println(recharge.toString());
//        }
        //SELECT  COUNT(*) FROM persie_deamon.round_ext  WHERE ddGroup = TRUE

        // int i = this.roundExtService.selectSMaxId();
        //  System.out.println(i);
        Integer aa = 123;
        System.out.println(String.valueOf(aa));
    }

}
