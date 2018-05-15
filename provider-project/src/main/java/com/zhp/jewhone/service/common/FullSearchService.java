package com.zhp.jewhone.service.common;

import com.zhp.jewhone.core.Result;
import com.zhp.jewhone.model.search.GlobalSearchCount;
import com.zhp.jewhone.model.search.SearchData;
import com.zhp.jewhone.model.search.SearchParamType;

import java.util.List;

public interface FullSearchService {

	/**
	 * 更新故事搜索引擎数据
	 * 
	 * @param batchNum
	 *            批次数量
	 * @return
	 */
	Result updateMOUOSearchDiary(int batchNum);

	/**
	 * 更新热词搜索引擎数据
	 * 
	 * @param batchNum
	 *            批次数量
	 * @return
	 */
	Result updateMOUOSearchHotword(int batchNum);

	/**
	 * 删除根据条件删除单条数据
	 * 
	 * @return
	 */
	public Result deleteDataByTypeAndId(String index, String type, String id);

	/**
	 * 添加数据
	 * 
	 * @param type
	 *            1 故事
	 * @param
	 * @return
	 */

	Result addData(int type, String id);

	/**
	 * 添加数据
	 * 
	 * @param type
	 *            1 故事
	 * @param
	 * @return
	 */

	Result addData(int type, SearchData searchData);

	/**
	 * 根据输入搜索内容
	 * 
	 * @param searchInfo
	 *            type 搜索分类 3文章
	 * @return
	 */
	List<SearchData> queryDataByKeyWord(SearchParamType searchInfo);

	/**
	 * 同城搜索
	 * 
	 * @return
	 */
	List<SearchData> queryLocalData(SearchParamType searchInfo);

	/**
	 * 最新搜索
	 * 
	 * @return
	 */
	List<SearchData> queryNewestData(SearchParamType searchInfo);

	/**
	 * 删除根据条件删除单条数据
	 * 
	 * @return
	 */
	public Result deleteDataByType(Integer type, Integer id);

	/**
	 * 单条插入热词
	 * 
	 * @param text
	 * @return
	 */
	Result addHotwordData(String text);

	/**
	 * 单条删除热词
	 * 
	 * @param text
	 * @return
	 */
	Result deleteHotwordData(String text);

	/**
	 * 搜索数量
	 * 
	 * @param keyword
	 * @return
	 */
	GlobalSearchCount queryCountByKeyWord(String keyword);

	/**
	 * 活动搜索
	 * 
	 * @return
	 */
	List<SearchData> queryActivityData(SearchParamType searchInfo);
	
	/**
	 * 初始化城市数据
	 * @param keyword
	 * @return
	 */
	Integer queryCityTalk(String keyword);
}
