package com.fish.utils;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipFile;


@MapperScan("com.fish.manger.v5.mapper")
public class test
{

    @Autowired
    RedisData redisData;

    public static void main(String[] args) throws Exception
    {


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
//        paramMap.put("name","wx_config");
//        String res= HttpUtil.post("http://192.168.1.55:8080/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);


//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","deamon");//deamon , online   persieDeamon
//        String res= HttpUtil.post("https://logic.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","games");
//        String res= HttpUtil.post("http://192.168.1.55:8080/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);


//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","notice_system");
//        String res= HttpUtil.post("https://logic.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","notification");
//        String res= HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);


//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name", "games");
//        String res = HttpUtil.post("https://logic.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :" + res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name", "rounds");
//        String res = HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :" + res);




    }


    public static String minify(String jsonString)
    {
        boolean in_string = false;
        boolean in_multiline_comment = false;
        boolean in_singleline_comment = false;
        char string_opener = 'x'; // unused value, just something that makes compiler happy

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < jsonString.length(); i++)
        {
            // get next (c) and next-next character (cc)

            char c = jsonString.charAt(i);
            String cc = jsonString.substring(i, Math.min(i + 2, jsonString.length()));

            // big switch is by what mode we're in (in_string etc.)
            if (in_string)
            {
                if (c == string_opener)
                {
                    in_string = false;
                    out.append(c);
                } else if (c == '\\')
                { // no special treatment needed for \\u, it just works like this too
                    out.append(cc);
                    ++i;
                } else
                    out.append(c);
            } else if (in_singleline_comment)
            {
                if (c == '\r' || c == '\n')
                    in_singleline_comment = false;
            } else if (in_multiline_comment)
            {
                if (cc.equals("*/"))
                {
                    in_multiline_comment = false;
                    ++i;
                }
            } else
            {
                // we're outside of the special modes, so look for mode openers (comment start, string start)
                if (cc.equals("/*"))
                {
                    in_multiline_comment = true;
                    ++i;
                } else if (cc.equals("//"))
                {
                    in_singleline_comment = true;
                    ++i;
                } else if (c == '"' || c == '\'')
                {
                    in_string = true;
                    string_opener = c;
                    out.append(c);
                } else if (!Character.isWhitespace(c))
                    out.append(c);
            }
        }
        return out.toString();
    }

    /**
     * 根据 id 生成分表编号的方法
     */
    public static String generateTableCodeByKey(String key)
    {
        if (key.length() < 16)
        {
            return "0";
        }

        int index = key.length() - 4;
        int c0 = key.charAt(index);
        int c1 = key.charAt(index + 1);
        int c2 = key.charAt(index + 2);
        int c3 = key.charAt(index + 3);
        int value = (c0 % 10) * 1000 + (c1 % 10) * 100 + (c2 % 10) * 10 + (c3 % 10);
        if (value < 0)
        {
            value = -value;
        }

        return String.valueOf(value);
    }



}
