package com.zhp.jewhone.dao.diary;

import com.zhp.jewhone.model.diary.DiarySearchInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiaryMapper {

    /**
     * 获取搜索文章字段
     *
     * @param
     * @return
     */
    List<DiarySearchInfo> queryDiarySearchInfo(@Param("diaryId") Integer diaryId);

    List<DiarySearchInfo> queryDiarySearchInfoByLimit(@Param("startId") Integer startId, @Param("startNum") Integer startNum, @Param("limitNum") Integer limitNum);

    /**
     * 根据Id获取文章
     *
     * @param diaryId
     * @return
     */
    DiarySearchInfo queryDiarySearchInfoById(@Param("diaryId") String diaryId);

    /**
     * 根据id获取第三方分享文章内容
     *
     * @param diaryId
     * @return
     */
    String queryThirdPartyContent(@Param("diaryId") Integer diaryId);

    /**
     * 转发此文章的列表
     *
     * @param id
     * @return
     */
    List<Integer> queryRelatedDiary(@Param("id") Integer id);
}