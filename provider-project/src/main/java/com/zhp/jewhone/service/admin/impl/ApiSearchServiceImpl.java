package com.zhp.jewhone.service.admin.impl;

import com.zhp.jewhone.core.Result;
import com.zhp.jewhone.core.constant.enums.ResponseEnum;
import com.zhp.jewhone.core.elasticsearch.ESClient;
import com.zhp.jewhone.core.elasticsearch.ESIndexNameConfig;
import com.zhp.jewhone.core.util.page.PageUtils;
import com.zhp.jewhone.service.admin.ApiSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("apiSearchService")
@Transactional(readOnly = true)
// 只读（查询适用）
public class ApiSearchServiceImpl implements ApiSearchService {
	@Resource
	private ESClient elasticsearchClient;
	@Resource
	private ESIndexNameConfig esIndexNameConfig;

	@Override
	public Result addData(String type, String json) {
		if (StringUtil.isEmpty(type) || StringUtil.isEmpty(json)) {
			return Result.make(false, "添加数据错误,为NULL");
		}
		boolean flag;
		flag = elasticsearchClient.insertData(esIndexNameConfig.getJewhoneIndex(type), type, json);
		if (flag) {
			return Result.make(true);
		} else {
			log.error("apiSearchServiceImpl   添加数据错误 type={0} json={1}", type, json);
			return Result.make(false, "添加数据错误");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result selectListByPageBean(int pageNumber, int pageSize, Map<String, Object> mustMap, Map<String, Object> shouldMap) {
		log.info("进入search");
		SearchResponse searchResponse = elasticsearchClient.selectJsonList(pageNumber, pageSize, esIndexNameConfig.getType().getSysOperationLog(),
				esIndexNameConfig.getJewhoneIndex(esIndexNameConfig.getType().getSysOperationLog()), mustMap, shouldMap);
		SearchHits searchHits = searchResponse.getHits();
		long total = searchHits.totalHits();
		List<String> list = new ArrayList<>();
		searchHits.forEach(searchHit -> list.add(searchHit.sourceAsString()));
		PageUtils pb = new PageUtils(list,(int)total,pageSize,pageNumber);
		return new Result(ResponseEnum.SUCCESS, pb);
	}

}
