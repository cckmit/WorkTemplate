package com.code.entity;

import com.annotation.Comments;
import com.annotation.Page;
import com.annotation.PrimaryKey;
import com.code.dao.MiniGamebackDao;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * mini_game 实体类
 * 2018-12-06 xuweihua
 */

@Entity
@Page
public class Mini_game {
    @PrimaryKey
    @Column(name = "game_id")
    @Comments(name = "主键id")
    public int game_id;
    @Column(name = "game_name")
    @Comments(name = "产品名称")
    public String game_name;
    @Column(name = "game_file")
    @Comments(name = "文件名称")
    public String game_file;
    @Column(name = "game_spread")
    @Comments(name = "推广名称")
    public String game_spread;
    @Column(name = "game_appid")
    @Comments(name = "Appid")
    public String game_appid;
    @Column(name = "game_plattype")
    @Comments(name = "平台 1-微信 2-百度 3-头条 4-OPPO 5-Facebook")
    public Integer game_plattype;
    @Column(name = "game_appsecret")
    @Comments(name = "AppSecret")
    public String game_appsecret;
    @Column(name = "game_initid")
    @Comments(name = "初始id")
    public String game_initid;
    @Column(name = "game_alakey")
    @Comments(name = "阿拉丁key")
    public String game_alakey;
    @Column(name = "game_swbelong")
    @Comments(name = "软著所属 1-个人 2-野火 3-奔游")
    public Integer game_swbelong;
    @Column(name = "game_softwork")
    @Comments(name = "软著")
    public String game_softwork;
    @Column(name = "game_username")
    @Comments(name = "账号")
    public String game_username;
    @Column(name = "game_password")
    @Comments(name = "密码")
    public String game_password;
    @Column(name = "game_admin")
    @Comments(name = "主体")
    public String game_admin;
    @Column(name = "game_adminwx")
    @Comments(name = "运营者微信")
    public String game_adminwx;
    @Column(name = "game_cellphone")
    @Comments(name = "手机号")
    public String game_cellphone;
    @Column(name = "game_iconfile")
    @Comments(name = "IconZip文件")
    public String game_iconfile;
    @Column(name = "game_cheattype")
    @Comments(name = "作弊类型")
    public int game_cheattype;
    @Column(name = "game_md5")
    @Comments(name = "MD5")
    public String game_md5;


    @Column(name = "plat_recommend")
    @Comments(name = "0-双平台 1-仅ios 2-仅安卓")
    public int plat_recommend;

    @Column(name = "game_ispublish")
    @Comments(name = "是否发布 1-是 2-否")
    public int game_ispublish;
    @Column(name = "game_iswheel")
    @Comments(name = "是否打开大转盘 1-是 2-否")
    public int game_iswheel;
    @Column(name = "game_wheel_count")
    @Comments(name = "大转盘次数")
    public int game_wheel_count;
    @Column(name = "game_sorttype_rec")
    @Comments(name = "推荐位排序类型 A B C")
    public String game_sorttype_rec;
    @Column(name = "game_sorttype_shop")
    @Comments(name = "商店排序类型 A B C")
    public String game_sorttype_shop;
    @Column(name = "game_emailpwd")
    @Comments(name = "邮箱密码")
    public String game_emailpwd;
    @Column(name = "game_bindappid")
    @Comments(name = "绑定APPID")
    public String game_bindappid;

    @Column(name = "game_jiecompany")
    @Comments(name = "结算公司")
    public Integer game_jiecompany;

    //数据库增加相应字段后删除@Column注释
    //@Column(name = "game_jiecompany_email")
    @Comments(name = "结算公司邮箱")
    public String game_jiecompanyemail;
    @Column(name = "show_shop_id")
    @Comments(name = "商品id")
    public Integer show_shop_id;


    @Column(name = "game_pos")
    @Comments(name = "排序位置")
    public Integer game_pos;

//	@ReadOnly
//	@Column(name="game_pos")
//	@Comments(name="排序位置")
//	public int game_pos;
//	@ReadOnly
//	@Column(name="game_adstate")
//	@Comments(name="广告模式状态")
//	public int game_adstate;
//	@ReadOnly
//	@Column(name="game_playrate")
//	@Comments(name="视频播放占比")
//	public Double game_playrate;
//	@ReadOnly
//	@Column(name="game_pullappids")
//	@Comments(name="拉起appid")
//	public String game_pullappids;

    /**
     * Excel导入计费点信息
     *
     * @return map
     */


    public static Map<String, Mini_game> getAppid2Game() {
        Map<String, Mini_game> map = new HashMap<String, Mini_game>();
        Vector<Mini_game> list = (Vector<Mini_game>) MiniGamebackDao.instance
                .findBySQL("select * from mini_game", Mini_game.class);
        for (Mini_game mini_game : list) {
            map.put(mini_game.game_appid, mini_game);
        }
        return map;
    }

    public static Map<Integer, Mini_game> getGameId2Game() {
        Map<Integer, Mini_game> map = new HashMap<Integer, Mini_game>();
        Vector<Mini_game> list = (Vector<Mini_game>) MiniGamebackDao.instance
                .findBySQL("select * from mini_game", Mini_game.class);
        for (Mini_game mini_game : list) {
            map.put(mini_game.game_id, mini_game);
        }
        return map;
    }
}
