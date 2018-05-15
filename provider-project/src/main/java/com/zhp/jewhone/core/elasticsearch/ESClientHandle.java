package com.zhp.jewhone.core.elasticsearch;

/**
 * es线程相关的数据源处理类
 */
public class ESClientHandle {
	// 数据源名称线程池
	private static final ThreadLocal<ESClient> holder = new ThreadLocal<ESClient>();
	
	/**
	 * 设置数据源
	 * 
	 * @param esClient
	 *            数据源名称
	 */
	public static void setDataSource(ESClient esClient) {
		holder.set(esClient);
	}

	/**
	 * 获取数据源
	 * 
	 * @return 数据源名称
	 */
	public static ESClient get() {
		return holder.get();
	}

	/**
	 * 清空数据源
	 */
	public static void clearDataSource() {
	/*	ESClient esClient = holder.get();
		if(esClient != null) {
			esClient.transportClient.close();
		}*/
		holder.remove();
	}
}
