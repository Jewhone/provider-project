package com.zhp.jewhone.model.diary;

import java.util.Date;

public class Diary {
    private Integer id;
    private String diaryTitle;//日记标题
    private String city;//城市定位
    private Object searchContent;//文字内容用于检索
    private Integer creatorId;//创建用户 id
    private Integer praiseTotal;//点赞数统计
    private Integer commentTotal;//评论数统计
    private Integer rewardTotal;//打赏数
    private Integer picTotal;//图片数量统计
    private Integer state;//0删除 1正常
    private Date createTime;
    private Integer readTotal;//阅读量
    private Integer relateDiaryId;//转发数据id
    private Integer relateState;//源文章的状态 0正常 1删除
    private String shareAppName;//来源描述
    private Integer shareAppType;//故事来源类型0陌友1后台2网易3头条4简书5知乎6百度
    private Integer sourceType;//0App格式发布1后台html发布2第三方分享
    private String h5Url;//h5地址，默认空
    private Integer urlAnalysisSuccess;//url解析0不解析1成功2解析失败
    private String rawUrl;//未加工url
    private Integer shareMedia;//0文章1视频
    private String extendData;//扩展字段
    private String videoUrl;//视频的播放地址当分享内容是视频的时候使用
    private Integer wordTotal;//文章字数统计
    private Integer dataHot;//0不是热门1是热门2热门推荐
    private Date dataHotTime;//热门时间
    private Integer applyHot;//申请热门0默认未申请或未通过1申请2通过
    private String category;//分类名
    public Diary() {
        super();
    }
    public Diary(Integer id,String diaryTitle,String city,Object searchContent,Integer creatorId,Integer praiseTotal,Integer commentTotal,Integer rewardTotal,Integer picTotal,Integer state,Date createTime,Integer readTotal,Integer relateDiaryId,Integer relateState,String shareAppName,Integer shareAppType,Integer sourceType,String h5Url,Integer urlAnalysisSuccess,String rawUrl,Integer shareMedia,String extendData,String videoUrl,Integer wordTotal,Integer dataHot,Date dataHotTime,Integer applyHot,String category) {
        super();
        this.id = id;
        this.diaryTitle = diaryTitle;
        this.city = city;
        this.searchContent = searchContent;
        this.creatorId = creatorId;
        this.praiseTotal = praiseTotal;
        this.commentTotal = commentTotal;
        this.rewardTotal = rewardTotal;
        this.picTotal = picTotal;
        this.state = state;
        this.createTime = createTime;
        this.readTotal = readTotal;
        this.relateDiaryId = relateDiaryId;
        this.relateState = relateState;
        this.shareAppName = shareAppName;
        this.shareAppType = shareAppType;
        this.sourceType = sourceType;
        this.h5Url = h5Url;
        this.urlAnalysisSuccess = urlAnalysisSuccess;
        this.rawUrl = rawUrl;
        this.shareMedia = shareMedia;
        this.extendData = extendData;
        this.videoUrl = videoUrl;
        this.wordTotal = wordTotal;
        this.dataHot = dataHot;
        this.dataHotTime = dataHotTime;
        this.applyHot = applyHot;
        this.category = category;
    }
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiaryTitle() {
        return this.diaryTitle;
    }

    public void setDiaryTitle(String diaryTitle) {
        this.diaryTitle = diaryTitle;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object getSearchContent() {
        return this.searchContent;
    }

    public void setSearchContent(Object searchContent) {
        this.searchContent = searchContent;
    }

    public Integer getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getPraiseTotal() {
        return this.praiseTotal;
    }

    public void setPraiseTotal(Integer praiseTotal) {
        this.praiseTotal = praiseTotal;
    }

    public Integer getCommentTotal() {
        return this.commentTotal;
    }

    public void setCommentTotal(Integer commentTotal) {
        this.commentTotal = commentTotal;
    }

    public Integer getRewardTotal() {
        return this.rewardTotal;
    }

    public void setRewardTotal(Integer rewardTotal) {
        this.rewardTotal = rewardTotal;
    }

    public Integer getPicTotal() {
        return this.picTotal;
    }

    public void setPicTotal(Integer picTotal) {
        this.picTotal = picTotal;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getReadTotal() {
        return this.readTotal;
    }

    public void setReadTotal(Integer readTotal) {
        this.readTotal = readTotal;
    }

    public Integer getRelateDiaryId() {
        return this.relateDiaryId;
    }

    public void setRelateDiaryId(Integer relateDiaryId) {
        this.relateDiaryId = relateDiaryId;
    }

    public Integer getRelateState() {
        return this.relateState;
    }

    public void setRelateState(Integer relateState) {
        this.relateState = relateState;
    }

    public String getShareAppName() {
        return this.shareAppName;
    }

    public void setShareAppName(String shareAppName) {
        this.shareAppName = shareAppName;
    }

    public Integer getShareAppType() {
        return this.shareAppType;
    }

    public void setShareAppType(Integer shareAppType) {
        this.shareAppType = shareAppType;
    }

    public Integer getSourceType() {
        return this.sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getH5Url() {
        return this.h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public Integer getUrlAnalysisSuccess() {
        return this.urlAnalysisSuccess;
    }

    public void setUrlAnalysisSuccess(Integer urlAnalysisSuccess) {
        this.urlAnalysisSuccess = urlAnalysisSuccess;
    }

    public String getRawUrl() {
        return this.rawUrl;
    }

    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    public Integer getShareMedia() {
        return this.shareMedia;
    }

    public void setShareMedia(Integer shareMedia) {
        this.shareMedia = shareMedia;
    }

    public String getExtendData() {
        return this.extendData;
    }

    public void setExtendData(String extendData) {
        this.extendData = extendData;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getWordTotal() {
        return this.wordTotal;
    }

    public void setWordTotal(Integer wordTotal) {
        this.wordTotal = wordTotal;
    }

    public Integer getDataHot() {
        return this.dataHot;
    }

    public void setDataHot(Integer dataHot) {
        this.dataHot = dataHot;
    }

    public Date getDataHotTime() {
        return this.dataHotTime;
    }

    public void setDataHotTime(Date dataHotTime) {
        this.dataHotTime = dataHotTime;
    }

    public Integer getApplyHot() {
        return this.applyHot;
    }

    public void setApplyHot(Integer applyHot) {
        this.applyHot = applyHot;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
