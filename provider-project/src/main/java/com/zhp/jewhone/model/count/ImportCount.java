package com.zhp.jewhone.model.count;

import java.io.Serializable;

public class ImportCount implements Serializable {
	private static final long serialVersionUID = -1622690723187692737L;

	private Integer talkCount;// 视界数量
	private Integer diaryCount;// 文章数量
	private Integer secretCount;// 秘密数量

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

	public Integer getSecretCount() {
		return secretCount;
	}

	public void setSecretCount(Integer secretCount) {
		this.secretCount = secretCount;
	}

}
