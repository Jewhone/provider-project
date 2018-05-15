package com.zhp.jewhone.model.diary;

import com.zhp.jewhone.core.constant.enums.ESIndexTypeEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DiarySearchInfo implements Serializable {
	private static final long serialVersionUID = -3345792588622698419L;

	private Integer id;// 文章id
	private Integer creatorId;// 创建者id
	private String title;// 文章标题
	private String city;// 城市
	private String category;// 分类
	private String content;// 文章内容
	private Date createTime;// 创建时间
	private Integer mediaType;// 媒体类型1.音频2.视频3.图片
	private List<ContentInDetail> contents;// 文章图片
	private Integer type = ESIndexTypeEnum.DIARY.getValue();// 1故事2说说3秘密4供求
	private Integer sourceType;// 0App格式发布1后台html发布2第三方分享'
	private String activityId;// 活动标签id

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<ContentInDetail> getContents() {
		return contents;
	}

	public void setContents(List<ContentInDetail> contents) {
		this.contents = contents;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getMediaType() {
		return mediaType;
	}

	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

}
