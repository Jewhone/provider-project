package com.zhp.jewhone.model.search;

import java.io.Serializable;

public class SearchParamType implements Serializable {
	private static final long serialVersionUID = -211917999172361957L;

	private Integer type;// 搜索类别(默认全局搜索)2说说3文章4转摘5供求6秘密
	private String keyword;// 搜索文本
	private Integer pageSize;// 每页页数
	private Integer pageNumber;// 页数
	private String data;//分页最后一条数据创建时间

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
