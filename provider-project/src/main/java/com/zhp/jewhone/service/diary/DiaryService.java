package com.zhp.jewhone.service.diary;

import org.apache.ibatis.annotations.Param;

public interface DiaryService {

    /**
     * 根据id获取第三方分享文章
     *
     * @param
     * @return
     */
    String queryThirdPartyContent(@Param("diaryId") Integer diaryId);
}