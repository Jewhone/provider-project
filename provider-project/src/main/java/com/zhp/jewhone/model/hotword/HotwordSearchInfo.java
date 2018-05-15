package com.zhp.jewhone.model.hotword;

import java.io.Serializable;
import java.util.Date;

public class HotwordSearchInfo implements Serializable {
	private static final long serialVersionUID = 519359434398987947L;

	private Object id;// 供求id
	private String content;// 供求内容
	private Date createTime;// 创建时间

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
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

}
