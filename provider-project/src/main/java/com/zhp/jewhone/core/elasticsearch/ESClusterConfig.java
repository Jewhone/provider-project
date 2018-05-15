package com.zhp.jewhone.core.elasticsearch;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 客户端连接配置文件
 */
@Component
@ConfigurationProperties(prefix = "elasticsearch")
@Data
public class ESClusterConfig {

	private String clusterName;
	private String esUsername;
	private String esPassword;
	private String clusterHost;
	private int clusterPort;
	
}
