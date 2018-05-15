package com.zhp.jewhone.core.util.page;

import com.zhp.jewhone.core.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 分页工具类
 */
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	//总记录数
	private int totalCount;
	//每页记录数
	private int pageSize;
	//总页数
	private int totalPage;
	//当前页数
	private int currPage;
	//列表数据
	private List<?> list;
	
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param currPage    当前页数
	 */
	public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}


	public static void main(String[] args) {
		String input = "aavzcadfdsfsdhshgwasdfasdfdddaaa";
		char[] chars = input.toCharArray();
		List<String> list = new ArrayList<>();
		for (char aChar : chars) {
			list.add(String.valueOf(aChar));
		}
		Map<String,Integer> map = new LinkedHashMap<>();
		for (String str: list) {
			if (StringUtils.getInt(map.get(str)) != 0){
				map.put(str,map.get(str)+1);
			}else {
				map.put(str,1);
			}
		}
		List<Map.Entry<String,Integer>> list1 = new LinkedList<>(map.entrySet());
		list1.sort(Comparator.comparing(e -> (e.getValue())));
		Map<String,Integer> result = new LinkedHashMap<>();
		for (Map.Entry<String,Integer> entry : list1) {
			result.put(entry.getKey(), entry.getValue());
		}
		System.out.println(result);
	}
}
