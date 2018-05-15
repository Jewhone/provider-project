package com.zhp.jewhone.core.elasticsearch;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 索引文件配置类
 */
@Component
@ConfigurationProperties(prefix = "spring.data.elasticsearch.properties.index.name.config")
@Data
public class ESIndexNameConfig {

	private Db db;
	private Type type;
	private String index;

	public Db getDb() {
		return db;
	}

	@Data
	public static class Db {
		private String jewhone;
	}

	@Data
	public static class Type {
		private String diary;
		private String hotword;
		private String sysOperationLog;
		private String mobileLog;
	}

	/**
	 * 获取数据库前缀的索引
	 * 
	 * @param type
	 * @return
	 */
	public String getJewhoneIndex(String type) {
		return getDb().getJewhone() + "_" + type;
	}
}
