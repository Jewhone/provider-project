package com.zhp.jewhone.model.search;

import java.io.Serializable;

public class GlobalSearchCount implements Serializable {
	private static final long serialVersionUID = -1622690723187692737L;

	private Integer userCount;// 用户数量
	private Integer talkCount;// 视界数量
	private Integer diaryCount;// 文章数量
	private Integer thirdPartyCount;// 转摘数量
	private Integer plazaCount;// 发布数量
	private Integer secretCount;// 秘密数量

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public Integer getTalkCount() {
		return talkCount;
	}

	public void setTalkCount(Integer talkCount) {
		this.talkCount = talkCount;
	}

	public Integer getDiaryCount() {
		return diaryCount;
	}

	public void setDiaryCount(Integer diaryCount) {
		this.diaryCount = diaryCount;
	}

	public Integer getThirdPartyCount() {
		return thirdPartyCount;
	}

	public void setThirdPartyCount(Integer thirdPartyCount) {
		this.thirdPartyCount = thirdPartyCount;
	}

	public Integer getPlazaCount() {
		return plazaCount;
	}

	public void setPlazaCount(Integer plazaCount) {
		this.plazaCount = plazaCount;
	}

	public Integer getSecretCount() {
		return secretCount;
	}

	public void setSecretCount(Integer secretCount) {
		this.secretCount = secretCount;
	}

}
