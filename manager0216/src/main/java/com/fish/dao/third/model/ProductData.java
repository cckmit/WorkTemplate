package com.fish.dao.third.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author CF
 * ProductData
 * fc数据对应展示实体类
 */
public class ProductData {
    private String wxAppid;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date wxDate;
    private Integer programType;

    /*** 产品数量 */
    private Integer productCount;
    /*** 总收入 */
    private BigDecimal revenueCount;
    private String wxRegJson;

    private String productName;
    private String ddAppPlatform;

    /*** 充值收入 */
    private BigDecimal recharge;

    /*** 广告收入 */
    private BigDecimal adRevenue;

    private BigDecimal activeUp;


    private MinitjWx minitjWx;

    private Integer wxNew;
    /*** 展示字段  新增单价  买量支出/广告新增人数*/
    private BigDecimal wxAdNewPrice;

    private Integer wxActive;

    private Integer wxVisit;

    private BigDecimal wxAvgLogin;

    private BigDecimal wxAvgOnline;

    private BigDecimal wxRemain2;

    private Integer wxVideoShow;

    private BigDecimal wxVideoClickrate;

    private BigDecimal wxVideoIncome;

    private BigDecimal videoECPM;

    private Integer wxBannerShow;

    private BigDecimal wxBannerClickrate;

    private BigDecimal wxBannerIncome;
    private BigDecimal bannerECPM;

    private Integer wxRegAd;

    private Integer wxRegSearch;

    private Integer wxRegOther;
    /*** 新增用户来源展示字段-- 任务栏-我的小程序 */
    private Integer wxRegTaskBarMySp;
    /*** 新增用户来源展示字段-- 发现-我的小程序 */
    private Integer wxRegFindMySp;
    /*** 新增用户来源展示字段-- 任务栏-最近使用 */
    private Integer wxRegTaskBarRecent;
    /*** 新增用户来源展示字段-- 其他小程序 */
    private Integer wxRegOtherSp;
    /*** 新增用户来源展示字段-- 其他小程序返回 */
    private Integer wxRegOtherReturn;

    private Integer wxRegJump;

    private Integer wxRegApp;

    private Integer wxRegCode;

    private Integer wxRegSession;

    private BigDecimal wxActiveWomen;

    private Integer wxShareUser;

    private Integer wxShareCount;

    private BigDecimal wxShareRate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insertTime;
    /*** 展示数据-买量支出 */
    private BigDecimal buyCost;
    /*** 展示数据-买量单价 */
    private BigDecimal buyClickPrice;

    /*** 展示数据--插屏收入 */
    private BigDecimal screenIncome;

    /*** 活跃用户来源--新增活跃来源  */
    private String wxActiveSource;
    /*** 活跃来源展示字段--广告 */
    private Integer wxActiveAd;
    /*** 活跃来源展示字段-- 任务栏-我的小程序 */
    private Integer wxActiveTaskBarMySp;
    /*** 活跃来源展示字段-- 发现-我的小程序 */
    private Integer wxActiveFindMySp;

    /*** 活跃来源展示字段-- 任务栏-最近使用 */
    private Integer wxActiveTaskBarRecent;
    /*** 活跃来源展示字段-- 搜索 */
    private Integer wxActiveSearch;
    /*** 活跃来源展示字段-- 其他 */
    private Integer wxActiveOther;
    /*** 活跃来源展示字段-- 其他小程序返回 */
    private Integer wxActiveOtherReturn;
    /*** 活跃来源展示字段-- 其他小程序 */
    private Integer wxActiveOtherSp;

    public BigDecimal getScreenIncome() {
        return screenIncome;
    }

    public void setScreenIncome(BigDecimal screenIncome) {
        this.screenIncome = screenIncome;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 查询开始和结束时间
     */
    private String beginTime;
    private String endTime;


    public BigDecimal getWxAdNewPrice() {
        return wxAdNewPrice;
    }

    public void setWxAdNewPrice(BigDecimal wxAdNewPrice) {
        this.wxAdNewPrice = wxAdNewPrice;
    }

    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public Date getWxDate() {
        return wxDate;
    }

    public void setWxDate(Date wxDate) {
        this.wxDate = wxDate;
    }

    public Integer getProgramType() {
        return programType;
    }

    public void setProgramType(Integer programType) {
        this.programType = programType;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getRevenueCount() {
        return revenueCount;
    }

    public void setRevenueCount(BigDecimal revenueCount) {
        this.revenueCount = revenueCount;
    }

    public String getWxRegJson() {
        return wxRegJson;
    }

    public void setWxRegJson(String wxRegJson) {
        this.wxRegJson = wxRegJson;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    public BigDecimal getAdRevenue() {
        return adRevenue;
    }

    public void setAdRevenue(BigDecimal adRevenue) {
        this.adRevenue = adRevenue;
    }

    public BigDecimal getActiveUp() {
        return activeUp;
    }

    public void setActiveUp(BigDecimal activeUp) {
        this.activeUp = activeUp;
    }

    public MinitjWx getMinitjWx() {
        return minitjWx;
    }

    public void setMinitjWx(MinitjWx minitjWx) {
        this.minitjWx = minitjWx;
    }

    public Integer getWxNew() {
        return wxNew;
    }

    public void setWxNew(Integer wxNew) {
        this.wxNew = wxNew;
    }

    public Integer getWxActive() {
        return wxActive;
    }

    public void setWxActive(Integer wxActive) {
        this.wxActive = wxActive;
    }

    public Integer getWxVisit() {
        return wxVisit;
    }

    public void setWxVisit(Integer wxVisit) {
        this.wxVisit = wxVisit;
    }

    public BigDecimal getWxAvgLogin() {
        return wxAvgLogin;
    }

    public void setWxAvgLogin(BigDecimal wxAvgLogin) {
        this.wxAvgLogin = wxAvgLogin;
    }

    public BigDecimal getWxAvgOnline() {
        return wxAvgOnline;
    }

    public void setWxAvgOnline(BigDecimal wxAvgOnline) {
        this.wxAvgOnline = wxAvgOnline;
    }

    public BigDecimal getWxRemain2() {
        return wxRemain2;
    }

    public void setWxRemain2(BigDecimal wxRemain2) {
        this.wxRemain2 = wxRemain2;
    }

    public Integer getWxVideoShow() {
        return wxVideoShow;
    }

    public void setWxVideoShow(Integer wxVideoShow) {
        this.wxVideoShow = wxVideoShow;
    }

    public BigDecimal getWxVideoClickrate() {
        return wxVideoClickrate;
    }

    public void setWxVideoClickrate(BigDecimal wxVideoClickrate) {
        this.wxVideoClickrate = wxVideoClickrate;
    }

    public BigDecimal getWxVideoIncome() {
        return wxVideoIncome;
    }

    public void setWxVideoIncome(BigDecimal wxVideoIncome) {
        this.wxVideoIncome = wxVideoIncome;
    }

    public BigDecimal getVideoECPM() {
        return videoECPM;
    }

    public void setVideoECPM(BigDecimal videoECPM) {
        this.videoECPM = videoECPM;
    }

    public Integer getWxBannerShow() {
        return wxBannerShow;
    }

    public void setWxBannerShow(Integer wxBannerShow) {
        this.wxBannerShow = wxBannerShow;
    }

    public BigDecimal getWxBannerClickrate() {
        return wxBannerClickrate;
    }

    public void setWxBannerClickrate(BigDecimal wxBannerClickrate) {
        this.wxBannerClickrate = wxBannerClickrate;
    }

    public BigDecimal getWxBannerIncome() {
        return wxBannerIncome;
    }

    public void setWxBannerIncome(BigDecimal wxBannerIncome) {
        this.wxBannerIncome = wxBannerIncome;
    }

    public BigDecimal getBannerECPM() {
        return bannerECPM;
    }

    public void setBannerECPM(BigDecimal bannerECPM) {
        this.bannerECPM = bannerECPM;
    }

    public Integer getWxRegAd() {
        return wxRegAd;
    }

    public void setWxRegAd(Integer wxRegAd) {
        this.wxRegAd = wxRegAd;
    }

    public Integer getWxRegJump() {
        return wxRegJump;
    }

    public void setWxRegJump(Integer wxRegJump) {
        this.wxRegJump = wxRegJump;
    }

    public Integer getWxRegSearch() {
        return wxRegSearch;
    }

    public void setWxRegSearch(Integer wxRegSearch) {
        this.wxRegSearch = wxRegSearch;
    }

    public Integer getWxRegApp() {
        return wxRegApp;
    }

    public void setWxRegApp(Integer wxRegApp) {
        this.wxRegApp = wxRegApp;
    }

    public Integer getWxRegCode() {
        return wxRegCode;
    }

    public void setWxRegCode(Integer wxRegCode) {
        this.wxRegCode = wxRegCode;
    }

    public Integer getWxRegSession() {
        return wxRegSession;
    }

    public void setWxRegSession(Integer wxRegSession) {
        this.wxRegSession = wxRegSession;
    }

    public Integer getWxRegOther() {
        return wxRegOther;
    }

    public void setWxRegOther(Integer wxRegOther) {
        this.wxRegOther = wxRegOther;
    }

    public BigDecimal getWxActiveWomen() {
        return wxActiveWomen;
    }

    public void setWxActiveWomen(BigDecimal wxActiveWomen) {
        this.wxActiveWomen = wxActiveWomen;
    }

    public Integer getWxShareUser() {
        return wxShareUser;
    }

    public void setWxShareUser(Integer wxShareUser) {
        this.wxShareUser = wxShareUser;
    }

    public Integer getWxShareCount() {
        return wxShareCount;
    }

    public void setWxShareCount(Integer wxShareCount) {
        this.wxShareCount = wxShareCount;
    }

    public BigDecimal getWxShareRate() {
        return wxShareRate;
    }

    public void setWxShareRate(BigDecimal wxShareRate) {
        this.wxShareRate = wxShareRate;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public BigDecimal getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(BigDecimal buyCost) {
        this.buyCost = buyCost;
    }

    public BigDecimal getBuyClickPrice() {
        return buyClickPrice;
    }

    public void setBuyClickPrice(BigDecimal buyClickPrice) {
        this.buyClickPrice = buyClickPrice;
    }

    public String getWxActiveSource() {
        return wxActiveSource;
    }

    public void setWxActiveSource(String wxActiveSource) {
        this.wxActiveSource = wxActiveSource;
    }

    public Integer getWxActiveAd() {
        return wxActiveAd;
    }

    public void setWxActiveAd(Integer wxActiveAd) {
        this.wxActiveAd = wxActiveAd;
    }

    public Integer getWxActiveTaskBarMySp() {
        return wxActiveTaskBarMySp;
    }

    public void setWxActiveTaskBarMySp(Integer wxActiveTaskBarMySp) {
        this.wxActiveTaskBarMySp = wxActiveTaskBarMySp;
    }

    public Integer getWxActiveFindMySp() {
        return wxActiveFindMySp;
    }

    public void setWxActiveFindMySp(Integer wxActiveFindMySp) {
        this.wxActiveFindMySp = wxActiveFindMySp;
    }

    public Integer getWxActiveTaskBarRecent() {
        return wxActiveTaskBarRecent;
    }

    public void setWxActiveTaskBarRecent(Integer wxActiveTaskBarRecent) {
        this.wxActiveTaskBarRecent = wxActiveTaskBarRecent;
    }

    public Integer getWxActiveSearch() {
        return wxActiveSearch;
    }

    public void setWxActiveSearch(Integer wxActiveSearch) {
        this.wxActiveSearch = wxActiveSearch;
    }

    public Integer getWxActiveOther() {
        return wxActiveOther;
    }

    public void setWxActiveOther(Integer wxActiveOther) {
        this.wxActiveOther = wxActiveOther;
    }

    public Integer getWxActiveOtherReturn() {
        return wxActiveOtherReturn;
    }

    public void setWxActiveOtherReturn(Integer wxActiveOtherReturn) {
        this.wxActiveOtherReturn = wxActiveOtherReturn;
    }

    public Integer getWxActiveOtherSp() {
        return wxActiveOtherSp;
    }

    public void setWxActiveOtherSp(Integer wxActiveOtherSp) {
        this.wxActiveOtherSp = wxActiveOtherSp;
    }


    public Integer getWxRegTaskBarMySp() {
        return wxRegTaskBarMySp;
    }

    public void setWxRegTaskBarMySp(Integer wxRegTaskBarMySp) {
        this.wxRegTaskBarMySp = wxRegTaskBarMySp;
    }

    public Integer getWxRegFindMySp() {
        return wxRegFindMySp;
    }

    public void setWxRegFindMySp(Integer wxRegFindMySp) {
        this.wxRegFindMySp = wxRegFindMySp;
    }

    public Integer getWxRegTaskBarRecent() {
        return wxRegTaskBarRecent;
    }

    public void setWxRegTaskBarRecent(Integer wxRegTaskBarRecent) {
        this.wxRegTaskBarRecent = wxRegTaskBarRecent;
    }

    public Integer getWxRegOtherSp() {
        return wxRegOtherSp;
    }

    public void setWxRegOtherSp(Integer wxRegOtherSp) {
        this.wxRegOtherSp = wxRegOtherSp;
    }

    public Integer getWxRegOtherReturn() {
        return wxRegOtherReturn;
    }

    public void setWxRegOtherReturn(Integer wxRegOtherReturn) {
        this.wxRegOtherReturn = wxRegOtherReturn;
    }

    public String getDdAppPlatform() {
        return ddAppPlatform;
    }

    public void setDdAppPlatform(String ddAppPlatform) {
        this.ddAppPlatform = ddAppPlatform;
    }
}
