package com.cc.manager.modules.fc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author cf
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(schema = "minigameback", value = "mini_game")
public class MiniGame implements BaseCrudEntity<MiniGame> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "game_id", type = IdType.AUTO)
    private Integer gameId;

    /**
     * 产品名称
     */
    private String gameName;

    /**
     * 产品平台 weixin-微信，q-QQ,tt-头条,quick-快应用
     */
    @TableField("game_appPlatform")
    private String gameAppplatform;

    /**
     * 文件名称
     */
    private String gameFile;

    private String gameSpread;

    /**
     * Appid
     */
    private String gameAppid;

    /**
     * 平台 1-微信 2-百度 3-头条 4-OPPO 5-Facebook
     */
    private Integer gamePlattype;

    /**
     * oppo-AppKey
     */
    private String gameAppKey;

    /**
     * AppSecret
     */
    private String gameAppsecret;

    /**
     * 初始id
     */
    private String gameInitid;

    /**
     * 阿拉丁key
     */
    private String gameAlakey;

    /**
     * 软著所属 1-个人 2-野火 3-奔游
     */
    private Integer gameSwbelong;

    /**
     * 软著
     */
    private String gameSoftwork;

    /**
     * 账号
     */
    private String gameUsername;

    /**
     * 密码
     */
    private String gamePassword;

    /**
     * 管理员
     */
    private String gameAdmin;

    /**
     * 管理员微信
     */
    private String gameAdminwx;

    /**
     * 手机号
     */
    private String gameCellphone;

    /**
     * IconZip文件
     */
    private String gameIconfile;

    /**
     * 作弊类型 1-不开启 2-开启
     */
    private Integer gameCheattype;

    /**
     * MD5
     */
    private String gameMd5;

    /**
     * 是否发布 1-是 2-否
     */
    private Integer gameIspublish;

    /**
     * 是否打开大转盘 1-是 2-否
     */
    private Integer gameIswheel;

    /**
     * 大转盘次数
     */
    private Integer gameWheelCount;

    /**
     * 推荐位排序类型 A B C
     */
    private String gameSorttypeRec;

    /**
     * 商店排序类型 A B C
     */
    private String gameSorttypeShop;

    /**
     * 商店策略 0/1
     */
    private Integer gameShopstrategy;

    /**
     * 排序位置
     */
    private Integer gamePos;

    /**
     * 广告模式状态
     */
    private Integer gameAdstate;

    /**
     * 视频播放占比
     */
    private BigDecimal gamePlayrate;

    /**
     * 拉起appid
     */
    private String gamePullappids;

    /**
     * 邮箱密码
     */
    private String gameEmailpwd;

    /**
     * 展示商店id
     */
    private Integer showShopId;

    /**
     * 绑定APPID
     */
    private String gameBindappid;

    /**
     * 结算公司
     */
    private Integer gameJiecompany;

    /**
     * 结算公司邮箱
     */
    private String gameJiecompanyEmail;

    /**
     * 0-双平台 1-仅ios 2-仅安卓
     */
    private Integer platRecommend;

    @Override
    public String getCacheKey() {
        return this.gameAppid;
    }

    @Override
    public String getCacheValue() {
        return this.gameAppid + "-" + this.gameName;
    }

}
