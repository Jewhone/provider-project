package com.zhp.jewhone.model.search;

import com.zhp.jewhone.model.diary.ContentInDetail;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SearchData implements Serializable {
	private static final long serialVersionUID = 5281842664167508304L;

	private Integer id;// 说说/故事、秘密 id
	private Integer creatorId;// 创建者id
	private String title;// 标题
	private String content;// 内容
	private List<ContentInDetail> contents;// 全部内容
	private String city;// 城市
	private String category;// 标签
	private Integer mediaType;// 媒体类型
	private Map<String, Object> location;// 地理位置 -->"location":
											// {"lon":110.8474948505407,"lat":35.42587934580798}
	private Date createTime;// 创建时间
	private Integer type;// 1故事2说说3秘密4供求
	private Integer sourceType;// 0App格式发布1后台html发布2第三方分享'
	private String activityId;// 活动标签
	private String picUrls;// 图片字符串

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
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

	public List<ContentInDetail> getContents() {
		return contents;
	}

	public void setContents(List<ContentInDetail> contents) {
		this.contents = contents;
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

	public Integer getMediaType() {
		return mediaType;
	}

	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Map<String, Object> getLocation() {
		return location;
	}

	public void setLocation(Map<String, Object> location) {
		this.location = location;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getPicUrls() {
		return picUrls;
	}

	public void setPicUrls(String picUrls) {
		this.picUrls = picUrls;
	}

}
