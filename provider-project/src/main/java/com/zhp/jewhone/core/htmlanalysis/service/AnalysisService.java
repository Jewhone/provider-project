package com.zhp.jewhone.core.htmlanalysis.service;

import com.zhp.jewhone.core.constant.enums.SourceTypeEnum;
import com.zhp.jewhone.core.htmlanalysis.entity.H5ShareResult;

public interface AnalysisService {

	/**
	 * 后台重新解析今日头条视频地址变化
	 * 
	 * @param videoId
	 * @param sourceTypeEnum
	 * @param h5ShareResult
	 * @return
	 */

	H5ShareResult analysisToutiaoVideoUrl(String videoId,
										  SourceTypeEnum sourceTypeEnum, H5ShareResult h5ShareResult);

}
