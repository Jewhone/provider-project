package com.zhp.jewhone.dao.hotword;

import com.zhp.jewhone.model.hotword.HotwordSearchInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HotwordMapper{
	
	/**
	 * 获取热词
	 * @param startId
	 * @param startNum
	 * @param limitNum
	 * @return
	 */
	public List<HotwordSearchInfo> queryHotwordByLimit(@Param("startId") int startId, @Param("startNum") int startNum, @Param("limitNum") int limitNum);
	
}
