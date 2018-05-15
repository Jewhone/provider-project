package com.zhp.jewhone.service.diary.Impl;

import com.zhp.jewhone.dao.diary.DiaryMapper;
import com.zhp.jewhone.service.diary.DiaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DiaryServiceImpl implements DiaryService {
	@Resource
	private DiaryMapper diaryMapper;

	@Override
	public String queryThirdPartyContent(Integer diaryId) {
		return diaryMapper.queryThirdPartyContent(diaryId);
	}

}