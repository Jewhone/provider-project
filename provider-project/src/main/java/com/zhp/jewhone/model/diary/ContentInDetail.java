package com.zhp.jewhone.model.diary;

import java.io.Serializable;

public class ContentInDetail implements Serializable{
	private static final long serialVersionUID = 8556362336623428412L;
	
	private String textOrPicture;// 文字内容或者图片 URL
	private Integer sorting;// 文字和图片的排序 从 1 开始
	private Integer contentType;// 0表示文字 1表示图片
	private String extendData;

	public String getTextOrPicture() {
		return textOrPicture;
	}

	public void setTextOrPicture(String textOrPicture) {
		this.textOrPicture = textOrPicture;
	}

	public Integer getSorting() {
		return sorting;
	}

	public void setSorting(Integer sorting) {
		this.sorting = sorting;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public String getExtendData() {
		return extendData;
	}

	public void setExtendData(String extendData) {
		this.extendData = extendData;
	}
}
