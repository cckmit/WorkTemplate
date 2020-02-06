package db;

import com.alibaba.fastjson.JSONObject;
import servlet.CmServletListener;
import tool.CmTool;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * 权重分配配置逻辑
 */
@Entity
public class PeDbWeight
{
    //最小阀值
    public BigDecimal minLimit = new BigDecimal(0.2);
    //最大阀值
    public BigDecimal maxLimit = new BigDecimal(0.8);
    //默认群组
    public int tag = 30;
    //地区方位
    public JSONObject ares = new JSONObject();

    private static PeDbWeight instance;

    private static SendMessageNotice notice = null;

    //报警通知{10}:群组,{1}:标题,{2}:高亮文字,{3}:正常文字,{4}:低灰文字
    private String noticeUrl = "https://sms2.down10ad.com/smsv2.php?tag={0}&t1={1}&h1={2}&h2={3}&h3={4}";

    /**
     * 唯一实体类
     *
     * @return
     */
    public static PeDbWeight instance()
    {
        return instance;
    }

    /**
     * 发送通知
     *
     * @param title     标题
     * @param highTip   高亮文字
     * @param normalTip 正常文字
     * @param grayTip   灰色文字
     */
    public void sendNotice(String title, String highTip, String normalTip, String grayTip)
    {
        String url = MessageFormat.format(noticeUrl, tag, title, highTip, normalTip, grayTip);

        //      CmTool.makeHttpConnect(url);

    }

    /**
     * 30s进行执行一次
     */
    public static class SendMessageNotice implements Runnable
    {
        public Vector<String> messageUrl = new Vector<>();

        @Override
        public void run()
        {
            if (!messageUrl.isEmpty())
            {
                String message = messageUrl.remove(0);
                System.out.println(message);
                CmTool.makeHttpConnect(message);
            }
        }
    }

    public static void main(String[] arg)
    {
        PeDbWeight weight = new PeDbWeight();
        weight.sendNotice("扩容测试", "扩容地区{全}", "阀值预警，房间使用高于80%", "低于20%进行缩容,高于80%进行缩容。");
    }

    public static void init()
    {
        instance = new PeDbWeight();
        if (notice == null)
        {
            notice = new SendMessageNotice();
            //30s執行一次
            CmServletListener.scheduler.scheduleWithFixedDelay(notice, 0, 30, TimeUnit.SECONDS);
        }
    }

    private PeDbWeight()
    {
        ares.put("0", "华东-上海");
        ares.put("1", "华北-北京");
        ares.put("2", "华南-广州");
        ares.put("3", "西南-成都");
    }
}
