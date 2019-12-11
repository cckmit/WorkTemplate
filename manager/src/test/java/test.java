import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.RankingMapper;
import com.fish.dao.primary.model.Ranking;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.fish.manger.v5.mapper")
public class test {

    public static void main(String[] args) throws Exception {


        //https://sgame.qinyougames.com/persieService/flush/logic
        //http://192.168.1.55:8080/persieService/flush/logic
        //persieDeamon   persieService    public
//        LocalTime localTime = LocalTime.now();
//        LocalDate localDate = LocalDate.now();
//        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
//        ZoneId zone = ZoneId.systemDefault();
//        Instant instant = localDateTime.atZone(zone).toInstant();
//        Date date = Date.from(instant);
//        System.out.println(date);   //sgame.qinyougames.com

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","notification");
//        String res= HttpUtil.post("http://192.168.1.55:8080/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);


//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","rounds");
//        String res= HttpUtil.post("http://192.168.1.55:8080/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);


//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","games");
//        String res= HttpUtil.post("https://sgame.qinyougames.com/persieDeamon/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","notification");
//        String res= HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","notification");
//        String res= HttpUtil.post("http://192.168.1.55:8080/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name", "wx_config");
//        String res = HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :" + res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name", "app_config");
//        String res = HttpUtil.post("https://sgame.qinyougames.com/public/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :" + res);



//     String aa = HexToStringUtil.getStringFromHex("b3a3b9e6b1c8c8fcc8d5");
//        System.out.println(aa);

//        b6f6c0c7b4abcbb5a2f2
       // String s = HexToStringUtil.utf8Togb2312("饿狼传说Ⅱ");
//        String bbb = HexToStringUtil.getStringToHex("饿狼传说Ⅱ");
//        System.out.println(bbb);
//        String aaa=new String(aa.getBytes(),"GBK");
//        String newName=new String(aa.getBytes(), "gb2312");
//        System.out.println(aaa);
//        System.out.println(newName);

//        String srcString =  getStringFromHex("c8adbbca3937");
//        byte[] GbkBytes = srcString.getBytes("GBK");
//        System.out.println("GbkBytes.length:" + GbkBytes.length);
//        byte[] UtfBytes = srcString.getBytes("UTF-8");
//        System.out.println("UtfBytes.length:" + UtfBytes.length);
//        String s;
//        for (int i = 0; i < srcString.length(); i++) {
//            s = Character.valueOf(srcString.charAt(i)).toString();
//            System.out.println(s + ":" + s.getBytes().length);
//        }

//        String aa = getStringFromHex("cbf9d3d0d3cecfb7bacfbcaf");
//        System.out.println(aa);

//        String s = generateTableCodeByKey("b6f6c0c7b4abcbb5cfb5c1d02bcbabbdd8c1fab8f1b6b72bbdd6cdb7b0d4cdf532bacfbcaf");
//        System.out.println(s);

     /*   Date now = new Date();
        System.out.println(now);
// java.util.Date -> java.time.LocalDate
        LocalDate localDate=now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
// java.time.LocalDate -> java.sql.Date
        Date newDate=java.sql.Date.valueOf(localDate);
        System.out.println(newDate);
        localDate = localDate.minusDays(1);
        Date tomorrowDate=java.sql.Date.valueOf(localDate);
        System.out.println(tomorrowDate);
*/

//        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        System.out.println("localDateTime :"+today_start);
//        LocalDateTime localDateTime = today_start.minusHours(24);
//        System.out.println("localDateTime :"+localDateTime);
    }
    /**
     * 根据 id 生成分表编号的方法
     */
    public static String generateTableCodeByKey(String key) {
        if (key.length() < 16) {
            return "0";
        }

        int index = key.length() - 4;
        int c0 = (int) key.charAt(index);
        int c1 = (int) key.charAt(index + 1);
        int c2 = (int) key.charAt(index + 2);
        int c3 = (int) key.charAt(index + 3);
        int value = (c0 % 10) * 1000 + (c1 % 10) * 100 + (c2 % 10) * 10 + (c3 % 10);
        if (value < 0) {
            value = -value;
        }

        return String.valueOf(value);
    }

    /**
     * 从二进制到字符串的方法
     *
     * @param hexString 二进制信息
     * @return 得到的字符串信息
     */
    public static String getStringFromHex(String hexString) throws UnsupportedEncodingException {
        byte[] bytes = new byte[hexString.length() / 2];

        for (int i = 0, j = 0; i < hexString.length(); i += 2, j++)
        {
            String value = "0x" + hexString.substring(i, i + 2);

            bytes[j] = Integer.decode(value).byteValue();
        }

        return new String(bytes,"gb2312");
    }

    /**
     * 从字符串到二进制流的方法
     *
     * @param content 源字串信息
     * @return 得到的二进制流的文本描述
     */
    public static String getStringToHex(String content)
    {
        byte[] bytes = content.getBytes();
        StringBuilder sb = new StringBuilder();
        String temp = null;

        for (int i = 0; i < bytes.length; i++)
        {
            temp = Integer.toHexString(bytes[i] & 0xff);
            if (temp.length() == 1)
            {
                sb.append("0");
            }
            sb.append(temp);
        }

        return sb.toString();
    }

    @Test
    public void testConn() throws SQLException {
        String time = "1567197702450";
        String timeStamp2Date = timeStampDate(time);
        Date date = new Date();
        System.out.println(date);

    }
    public static String timeStampDate(String time) {
        Long timeLong = Long.parseLong(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(timeLong));
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
