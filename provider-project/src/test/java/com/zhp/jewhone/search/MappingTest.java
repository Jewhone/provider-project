package com.zhp.jewhone.search;

import com.zhp.jewhone.core.elasticsearch.ESClient;
import com.zhp.jewhone.core.elasticsearch.ESIndexNameConfig;
import com.zhp.jewhone.service.common.FullSearchService;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MappingTest {

    @Resource
    private ESClient elasticsearchClient;
    @Resource
    private ESIndexNameConfig esIndexNameConfig;
    @Resource
    private FullSearchService fullSearchService;

    @Test
    public void addAdd() {
        fullSearchService.updateMOUOSearchDiary(1000);
        fullSearchService.updateMOUOSearchHotword(1000);
    }

    @Test
    public void setDiaryMapping() {
        String diaryType = esIndexNameConfig.getType().getDiary();
        String diaryIndex = esIndexNameConfig.getJewhoneIndex(diaryType);
        XContentBuilder mapping;
        try (TransportClient client = elasticsearchClient.transportClient) {
            IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(diaryIndex);
            IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
            DeleteIndexResponse dResponse = null;
            if (inExistsResponse.isExists()) {
                dResponse = client.admin().indices().prepareDelete(diaryIndex).execute().actionGet();
            }
            if (dResponse != null && !dResponse.isAcknowledged()) {
                return;
            }
            createIndex(diaryIndex);
            // 创建映射
            mapping = XContentFactory.jsonBuilder().startObject().startObject("properties").startObject("id").field("type", "integer").endObject()
                    .startObject("title").field("type", "text").field("analyzer", "ik_max_word").endObject().startObject("content")
                    .field("type", "text").field("analyzer", "ik_max_word").endObject().startObject("creatorId").field("type", "integer").endObject()
                    .startObject("createTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject().startObject("city")
                    .field("type", "keyword").field("index", "not_analyzed").endObject().startObject("category").field("type", "keyword").field("index", "not_analyzed").endObject()
                    .startObject("contents").field("type", "object").endObject().startObject("mediaType").field("type", "integer").endObject()
                    .startObject("type").field("type", "integer").endObject().startObject("sourceType").field("type", "integer").endObject()
                    .startObject("activityId").field("type", "text").endObject().endObject().endObject();

            client.admin().indices().putMapping(Requests.putMappingRequest(diaryIndex).type(diaryType).source(mapping)).actionGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setHotwordMapping() {
        String hotwordType = esIndexNameConfig.getType().getHotword();
        String hotwordInedx = esIndexNameConfig.getJewhoneIndex(hotwordType);
        XContentBuilder mapping;
        try (TransportClient client = elasticsearchClient.transportClient) {
            IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(hotwordInedx);
            IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
            DeleteIndexResponse dResponse = null;
            if (inExistsResponse.isExists()) {
                dResponse = client.admin().indices().prepareDelete(hotwordInedx).execute().actionGet();
            }
            if (dResponse != null && !dResponse.isAcknowledged()) {
                return;
            }
            createIndex(hotwordInedx);
            // 创建映射
            mapping = XContentFactory.jsonBuilder().startObject().startObject("properties").startObject("id").field("type", "text").endObject()
                    .startObject("content").field("type", "text").field("analyzer", "ik_max_word").endObject().startObject("createTime")
                    .field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject().endObject().endObject();

            client.admin().indices().putMapping(Requests.putMappingRequest(hotwordInedx).type(hotwordType).source(mapping)).actionGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 创建索引库
    private void createIndex(String indexName) {
        TransportClient client = elasticsearchClient.transportClient;
        try {
            // 创建索引库
            if (isIndexExists(indexName)) {
                System.out.println("Index  " + indexName + " already exits!");
            } else {
                CreateIndexRequest cIndexRequest = new CreateIndexRequest(indexName);
                CreateIndexResponse cIndexResponse = client.admin().indices().create(cIndexRequest).actionGet();
                if (cIndexResponse.isAcknowledged()) {
                    System.out.println("create index successfully！");
                } else {
                    System.out.println("Fail to create index!");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 判断索引是否存在 传入参数为索引库名称
    private boolean isIndexExists(String indexName) {
        boolean flag = false;
        TransportClient client = elasticsearchClient.transportClient;
        try {

            IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);

            IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();

            flag = inExistsResponse.isExists();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }
}
