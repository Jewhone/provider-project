package com.zhp.jewhone.dao.diary;

import com.zhp.jewhone.model.diary.ContentInDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiaryResourceMapper {
	/**
	 * 获取文章资源文件
	 * @param diaryId
	 * @return
	 */
	List<ContentInDetail> selectDiaryResourceById(@Param("diaryId") Integer diaryId);
}