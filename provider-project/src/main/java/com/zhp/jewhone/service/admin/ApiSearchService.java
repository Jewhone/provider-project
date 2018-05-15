package com.zhp.jewhone.service.admin;

import com.zhp.jewhone.core.Result;

import java.util.Map;

public interface ApiSearchService {
	/**
	 * 添加数据 自动生成 String.base64() UUID
	 * 
	 * @param type
	 * @param json
	 * @return
	 */
	Result addData(String type, String json);

	/**
	 * * 初始化查询所有登录日志
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param mustMap
	 *            必须包含的查询条件
	 * @param shouldMap
	 *            可以包含的查询条件
	 * @return
	 */
	Result selectListByPageBean(int pageNumber, int pageSize, Map<String, Object> mustMap, Map<String, Object> shouldMap);
}
