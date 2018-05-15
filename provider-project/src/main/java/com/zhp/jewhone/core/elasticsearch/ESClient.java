package com.zhp.jewhone.core.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhp.jewhone.core.constant.Const;
import com.zhp.jewhone.core.exception.JewhoneException;
import com.zhp.jewhone.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class ESClient {
	@Resource
	private ESClusterConfig elasticsearchConfig;
	@Resource
	private Environment environment;

	@Resource
	ESIndexNameConfig esIndexNameConfig;
	public TransportClient transportClient;

	@PostConstruct
	public void init() {
		/**
		 * 这里的连接方式指的是没有安装x-pack插件,如果安装了x-pack则参考{@link ElasticsearchXPackClient}
		 * 1. java客户端的方式是以tcp协议在9300端口上进行通信 2. http客户端的方式是以http协议在9200端口上进行通信
		 */
		try {
			String springProfilesActive = environment.getProperty("spring.profiles.active");
			switch (springProfilesActive) {
			case Const.SPRING_PROFILES_ACTIVE_DEV:
				transportClient = new PreBuiltTransportClient(Settings.builder().put("cluster.name", elasticsearchConfig.getClusterName()) // 设置ES实例的名称
						// .put("client.transport.sniff", true)
						// 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
						.build()).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticsearchConfig.getClusterHost()),
						elasticsearchConfig.getClusterPort()));
				break;
			case Const.SPRING_PROFILES_ACTIVE_ZHP:
				transportClient = new PreBuiltXPackTransportClient(Settings.builder().put("cluster.name", elasticsearchConfig.getClusterName())
						.put("xpack.security.user", elasticsearchConfig.getEsUsername() + ":" + elasticsearchConfig.getEsPassword())
						.put("client.transport.sniff", false).build()).addTransportAddress(new InetSocketTransportAddress(InetAddress
						.getByName(elasticsearchConfig.getClusterHost()), elasticsearchConfig.getClusterPort()));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			throw new JewhoneException("ElasticsearchClient is error", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("ElasticsearchClient 连接成功");
		}
	}

	// 删除数据
	public boolean deleteDataById(String indexName, String typeName, String id) {
		try {
			DeleteResponse response = transportClient.prepareDelete().setIndex(indexName).setType(typeName).setId(id).execute().actionGet();
			if (response.status() != RestStatus.OK) {
				log.error("delete is fail,indexName:{},typeName:{},id:{}", indexName, typeName, id);
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new JewhoneException("delete is fail", e);
		}
	}

	/**
	 * 创建索引库
	 * 
	 * @param indexName
	 */
	public void createIndex(String indexName) {
		TransportClient client = transportClient;
		try {
			// 创建索引库
			if (!isIndexExists(indexName)) {
				CreateIndexRequest cIndexRequest = new CreateIndexRequest(indexName);
				CreateIndexResponse cIndexResponse = client.admin().indices().create(cIndexRequest).actionGet();
				if (!cIndexResponse.isAcknowledged()) {
					log.error("createIndex is fail:" + cIndexResponse.toString());
				}
			}

		} catch (Exception e) {
			throw new JewhoneException("createIndex is fail", e);
		}
	}

	/**
	 * 删除索引
	 * 
	 * @param indexName
	 * @return boolean
	 */
	public boolean deleteIndex(String indexName) {
		TransportClient client = transportClient;
		try {
			IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
			IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
			DeleteIndexResponse dResponse = null;
			if (inExistsResponse.isExists()) {
				// client.prepareDelete();
				// DeleteResponse response = client.prepareDelete("twitter",
				// "tweet", "1").setOperationThreaded(false).get();
				dResponse = client.admin().indices().prepareDelete(indexName).execute().actionGet();
			}
			if (dResponse != null && !dResponse.isAcknowledged()) {
				return true;
			}

		} catch (Exception e) {
			throw new JewhoneException("createIndex is fail", e);
		}
		return false;
	}

	/**
	 * 设置mapping
	 * 
	 * @param indexName
	 * @param typeName
	 */
	public void setMapping(String indexName, String typeName, XContentBuilder mapping) {
		TransportClient client = transportClient;
		try {
			client.admin().indices().putMapping(Requests.putMappingRequest(indexName).type(typeName).source(mapping)).actionGet();
		} catch (Exception e) {
			throw new JewhoneException("setMapping is fail", e);
		}
	}

	/**
	 * 判断索引是否存在 传入参数为索引库名称
	 * 
	 * @param indexName
	 * @return
	 */
	public boolean isIndexExists(String indexName) {
		boolean flag = false;
		TransportClient client = transportClient;
		try {

			IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);

			IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();

			if (inExistsResponse.isExists()) {
				flag = true;
			} else {
				flag = false;
			}

		} catch (Exception e) {
			throw new JewhoneException("isIndexExists is fail", e);
		}
		return flag;
	}

	/**
	 * 添加数据
	 * 
	 * @param indexName
	 * @param typeName
	 * @param id
	 * @param dataJson
	 * @return
	 */
	public boolean addData(String indexName, String typeName, String id, String dataJson) {
		TransportClient client = transportClient;
		try {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			IndexRequest request = client.prepareIndex(indexName, typeName, id).setSource(dataJson, XContentType.JSON).request();
			bulkRequest.add(request);
			BulkResponse br = bulkRequest.execute().actionGet();
			if (br.hasFailures()) {
				log.error(JsonUtil.toJson(br) + br.buildFailureMessage());
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new JewhoneException("addData dataJson operate is fail", e);
		}
	}

	public boolean addData(String indexName, String typeName, String id, Map<String, ?> paramsMap) {
		try {
			String dataJson = new ObjectMapper().writeValueAsString(paramsMap);
			return addData(indexName, typeName, id, dataJson);
		} catch (Exception e) {
			throw new JewhoneException("addData paramsMap operate is fail", e);
		}
	}

	/**
	 * 批量添加数据
	 * 
	 * @param moyouxIndex
	 * @param typeName
	 * @param idList
	 * @param dataJsonList
	 * @return
	 */
	public Boolean addBatchDataByIdList(String moyouxIndex, String typeName, List<?> idList, List<String> dataJsonList) {
		TransportClient client = transportClient;
		try {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			if (dataJsonList != null && dataJsonList.size() > 0) {
				for (int i = 0; i < dataJsonList.size(); i++) {
					IndexRequest request = client.prepareIndex(moyouxIndex, typeName, idList.get(i).toString())
							.setSource(dataJsonList.get(i), XContentType.JSON).request();
					bulkRequest.add(request);
				}
			}
			BulkResponse br = bulkRequest.execute().actionGet();
			if (br.hasFailures()) {
				log.error("=====批量导入错误=====");
				log.error(br.buildFailureMessage());
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new JewhoneException("addBatchDataIntegerIdList operate is fail", e);
		}
	}

	/**
	 * 修改数据
	 * 
	 * @param indexName
	 * @param typeName
	 * @param id
	 * @param dataJson
	 * @return
	 */
	public boolean updateData(String indexName, String typeName, String id, String dataJson) {
		return addData(indexName, typeName, id, dataJson);
	}

	/**
	 * 添加数据 自动生成 String.base64() 自动生成id
	 * 
	 * @param index
	 * @param type
	 * @param json
	 * @return
	 */
	public boolean insertData(String index, String type, String json) {
		TransportClient client = transportClient;
		try {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			IndexRequest request = client.prepareIndex(index, type).setSource(json, XContentType.JSON).request();
			bulkRequest.add(request);
			BulkResponse br = bulkRequest.execute().actionGet();
			if (br.hasFailures()) {
				log.error(JsonUtil.toJson(br) + br.buildFailureMessage());
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new JewhoneException("addData dataJson operate is fail", e);
		}
	}

	/**
	 * 根据条件 批量模糊查询
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param type
	 * @param index
	 * @param mustMap
	 *            必须包含的查询条件
	 * @param shouldMap
	 *            可以包含的查询条件
	 * @return
	 */
	public SearchResponse selectJsonList(int pageNumber, int pageSize, String type, String index, Map<String, Object> mustMap,
			Map<String, Object> shouldMap) {
		// Validate
		if (pageNumber <= 0 || pageSize <= 0) {
			log.error("参数错误  pageNumber <= 0 || pageSize <= 0");
			throw new JewhoneException("param erro   pageNumber <= 0 || pageSize <= 0");
		}
		if (StringUtil.isEmpty(index) || StringUtil.isEmpty(type)) {
			log.error("参数错误   StringUtil.isEmpty(index) || StringUtil.isEmpty(type)");
			throw new JewhoneException("param erro    StringUtil.isEmpty(index) || StringUtil.isEmpty(type)");
		}
		// 装饰查询条件
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (mustMap != null && mustMap.size() > 0) {
			for (String key : mustMap.keySet()) {
				boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.matchQuery(key, mustMap.get(key)));
			}
		}
		if (shouldMap != null && shouldMap.size() > 0) {
			for (String key : shouldMap.keySet()) {
				boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.matchQuery(key, shouldMap.get(key)));
			}
		}
		SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(index).setTypes(type)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(boolQueryBuilder).setFrom((pageNumber - 1) * pageSize).setSize(pageSize)
				.setExplain(true);
		log.info("GET _search");
		log.info((searchRequestBuilder.toString()));
		return searchRequestBuilder.get();
	}
}
