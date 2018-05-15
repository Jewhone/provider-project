package com.zhp.jewhone.service.common.impl;

import com.zhp.jewhone.core.Result;
import com.zhp.jewhone.core.constant.enums.ESIndexTypeEnum;
import com.zhp.jewhone.core.elasticsearch.ESClient;
import com.zhp.jewhone.core.elasticsearch.ESIndexNameConfig;
import com.zhp.jewhone.core.util.DateTimeUtil;
import com.zhp.jewhone.core.util.JsonUtil;
import com.zhp.jewhone.core.util.MD5Util;
import com.zhp.jewhone.core.util.StringUtils;
import com.zhp.jewhone.dao.diary.DiaryMapper;
import com.zhp.jewhone.dao.diary.DiaryResourceMapper;
import com.zhp.jewhone.dao.hotword.HotwordMapper;
import com.zhp.jewhone.model.diary.ContentInDetail;
import com.zhp.jewhone.model.diary.DiarySearchInfo;
import com.zhp.jewhone.model.hotword.HotwordSearchInfo;
import com.zhp.jewhone.model.search.GlobalSearchCount;
import com.zhp.jewhone.model.search.SearchData;
import com.zhp.jewhone.model.search.SearchParamType;
import com.zhp.jewhone.service.common.FullSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
// 只读（查询适用）
public class FullSearchServiceImpl implements FullSearchService {
    @Resource
    private DiaryMapper diaryMapper;
    @Resource
    private ESClient elasticsearchClient;
   // @Resource
    //private StringRedisTemplate redisTemplate;
    @Resource
    private ESIndexNameConfig esIndexNameConfig;
    @Resource
    private DiaryResourceMapper diaryResourceMapper;
    @Resource
    private HotwordMapper hotwordMapper;

    private ExecutorService updateMOUOSearchDiaryexecutor = null;

    @Override
    public Result updateMOUOSearchDiary(int batchNum) {
        if (batchNum <= 0)
            batchNum = 100;
        int threadNum = batchNum / 2;// 声明线程池数量
        log.info("********故事导入开始********");
        if (updateMOUOSearchDiaryexecutor == null || updateMOUOSearchDiaryexecutor.isTerminated()) {
            updateMOUOSearchDiaryexecutor = Executors.newFixedThreadPool(Math.min(threadNum, 1000));
        }
        addDiary(batchNum, updateMOUOSearchDiaryexecutor);
        updateMOUOSearchDiaryexecutor.shutdown();
        String msg = "<=======故事导入结束========>";
        log.info(msg);
        return Result.make(true, msg);
    }

    private ExecutorService updateMOUOSearchHotwordexecutor = null;

    public Result updateMOUOSearchHotword(int batchNum) {
        if (batchNum <= 0)
            batchNum = 100;
        int threadNum = batchNum / 2;// 声明线程池数量
        log.info("********热词数据导入开始********");
        if (updateMOUOSearchHotwordexecutor == null || updateMOUOSearchHotwordexecutor.isTerminated()) {
            updateMOUOSearchHotwordexecutor = Executors.newFixedThreadPool(Math.min(threadNum, 1000));
        }
        addHotword(batchNum, updateMOUOSearchHotwordexecutor);
        updateMOUOSearchHotwordexecutor.shutdown();
        String msg = "<=======热词数据导入结束========>";
        log.info(msg);
        return Result.make(true, msg);
    }


    private void addDiary(int limitNum, ExecutorService executor) {
        //redisTemplate.opsForValue().get(Const.DIARY_ELASTICSEARCH_INDEX_ID)
        int startId = StringUtils.getInt(0);// 上次导入截止的数据id，这次由上次的id开始导入
        boolean isGoOn = true;
        List<DiarySearchInfo> diaryInfoList = diaryMapper.queryDiarySearchInfoByLimit(startId, 0, limitNum);
        if (diaryInfoList == null || diaryInfoList.size() == 0) {
            return;
        }
        if (diaryInfoList.size() < limitNum) {
            log.info("导入到 id：" + startId + " end:" + diaryInfoList.size());
            isGoOn = false;
        }
        log.info("load resource:" + DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "diaryInfoList.size()=" + diaryInfoList.size());
        List<Integer> diaryIdList;
        List<String> diaryData;
        diaryIdList = diaryInfoList.stream().map(DiarySearchInfo::getId).collect(Collectors.toList());

        List<CompletableFuture<String>> jsonList = diaryInfoList.stream()
                .map(diary -> CompletableFuture.supplyAsync(() -> getDiaryJson(diary), executor)).collect(Collectors.toList());
        diaryData = jsonList.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList());

        // 批量导入
        startId = diaryIdList.stream().max(Integer::compareTo).get();
        try {
            log.info("导入到 id：" + startId + "");
            String diaryType = esIndexNameConfig.getType().getDiary();
            Boolean flag = elasticsearchClient.addBatchDataByIdList(esIndexNameConfig.getJewhoneIndex(diaryType), diaryType, diaryIdList, diaryData);
            if (!flag) {
                log.error("批量导入diary数据异常: " + startId);
                isGoOn = false;
            } else {
                //redisTemplate.opsForValue().set(Const.DIARY_ELASTICSEARCH_INDEX_ID, startId + "");
                log.info("load resource" + DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("批量导入diary数据异常: " + e.getMessage(), e);
            isGoOn = false;
        }
        if (isGoOn) {
            addDiary(limitNum, executor);
        }
    }

    private String getDiaryJson(DiarySearchInfo diaryInfo) {
        List<ContentInDetail> contents = diaryResourceMapper.selectDiaryResourceById(diaryInfo.getId());
        if (contents != null && contents.size() != 0) {
            String diaryContents = String.join(" ",
                    contents.stream().filter(c -> c.getContentType() == 0).map(ContentInDetail::getTextOrPicture).collect(Collectors.toList()));
            if (diaryInfo.getSourceType() != 0) {// 转载存searchContent
                String searchContent = diaryMapper.queryThirdPartyContent(diaryInfo.getId());
                String content = StringUtils.htmlDocument(searchContent);
                diaryInfo.setContent(content);
            } else {
                diaryInfo.setContent(StringUtils.htmlDocument(diaryContents));
            }
            diaryInfo.setContents(contents);
        }
        return JsonUtil.toJson(diaryInfo);
    }


    /**
     * 批量导入热词
     *
     * @param limitNum executor
     */
    private void addHotword(int limitNum, ExecutorService executor) {
        boolean isGoOn = true;
        MD5Util md5 = MD5Util.getInstance();
        List<HotwordSearchInfo> hotwordInfoList = hotwordMapper.queryHotwordByLimit(0, 0, limitNum);
        if (CollectionUtils.isEmpty(hotwordInfoList)) {
            return;
        }
        if (hotwordInfoList.size() < limitNum) {
            log.info("导入热词数据");
            isGoOn = false;
        }
        log.info("load resource:" + DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "hotwordInfoList.size()=" + hotwordInfoList.size());
        List<String> hotwordIdList;
        List<String> hotwordData;
        hotwordIdList = hotwordInfoList.stream().map(hotword -> md5.getMD5Code(hotword.getContent())).collect(Collectors.toList());

        List<CompletableFuture<String>> jsonList = hotwordInfoList.stream()
                .map(hotword -> CompletableFuture.supplyAsync(() -> getHotwordJson(hotword), executor)).collect(Collectors.toList());
        hotwordData = jsonList.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList());
        try {
            String hotwordType = esIndexNameConfig.getType().getHotword();
            Boolean flag = elasticsearchClient.addBatchDataByIdList(esIndexNameConfig.getJewhoneIndex(hotwordType), hotwordType, hotwordIdList,
                    hotwordData);
            if (!flag) {
                log.error("批量导入hotword数据异常");
                isGoOn = false;
            } else {
                log.info("load resource" + DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("批量导入hotword数据异常: " + e.getMessage(), e);
            isGoOn = false;
        }
        if (isGoOn) {
            addHotword(limitNum, executor);
        }
    }

    private String getHotwordJson(HotwordSearchInfo hotwordInfo) {
        String id = MD5Util.getInstance().getMD5Code(hotwordInfo.getContent());
        hotwordInfo.setId(id);
        return JsonUtil.toJson(hotwordInfo);
    }

    @Override
    public Result deleteDataByTypeAndId(String index, String type, String id) {
        boolean isSuccess = elasticsearchClient.deleteDataById(index, type, id);
        return Result.make(isSuccess);
    }

    @Override
    public Result addData(int type, String id) {
        boolean isSuccess = false;
        if (type == ESIndexTypeEnum.DIARY.getValue()) {
            String diaryType = esIndexNameConfig.getType().getDiary();
            String diaryIndex = esIndexNameConfig.getJewhoneIndex(diaryType);
            DiarySearchInfo diaryInfo = diaryMapper.queryDiarySearchInfoById(id);
            List<ContentInDetail> contents = diaryResourceMapper.selectDiaryResourceById(diaryInfo.getId());
            if (contents != null && contents.size() != 0) {
                StringBuffer diaryContents = new StringBuffer("");
                contents.forEach(diaryContent -> {
                    if (diaryContent.getContentType() == 0) {// 文字
                        diaryContents.append(diaryContent.getTextOrPicture());
                    }
                });
                if (diaryInfo.getSourceType() != 0) {
                    String searchContent = diaryMapper.queryThirdPartyContent(diaryInfo.getId());
                    String content = StringUtils.htmlDocument(searchContent);
                    diaryInfo.setContent(content);
                } else {
                    diaryInfo.setContent(StringUtils.htmlDocument(diaryContents.toString()));
                }
                diaryInfo.setContents(contents);
            }
            isSuccess = elasticsearchClient.addData(diaryIndex, diaryType, id, JsonUtil.toJson(diaryInfo));
        }
        return Result.make(isSuccess);
    }

    @Override
    public List<SearchData> queryDataByKeyWord(SearchParamType searchInfo) {
        List<SearchData> result = new ArrayList<>();
        // 搜索目标
        String diaryType = esIndexNameConfig.getType().getDiary();
        String diaryIndex = esIndexNameConfig.getJewhoneIndex(diaryType);
        // 搜索条件
        QueryBuilder s1 = QueryBuilders.matchQuery("title", searchInfo.getKeyword());// 内容搜索
        QueryBuilder s2 = QueryBuilders.matchQuery("content", searchInfo.getKeyword());// 内容搜索

        SearchRequestBuilder srb;
        QueryBuilder queryBuilder;
        List<String> Indices = new ArrayList<>();
        List<String> types = new ArrayList<>();
        queryBuilder = QueryBuilders.boolQuery().should(s1).should(s2);
        Indices.add(diaryIndex);
        types.add(diaryType);
        srb = searchOrderByScore(Indices, types, queryBuilder, searchInfo.getPageNumber(), searchInfo.getPageSize());
        SearchResponse response = srb.get();
        SearchHits resultHits = response.getHits();
        Map<String, Class<?>> classMap = new HashMap<>();
        getResult(result, resultHits, classMap);
        return result;
    }

    private void getResult(List<SearchData> result, SearchHits resultHits, Map<String, Class<?>> classMap) {
        for (int i = 0; i < resultHits.getHits().length; i++){
            classMap.put("contents", ContentInDetail.class);
            String json = resultHits.getHits()[i].getSourceAsString();
            SearchData data = JsonUtil.toBean(json, SearchData.class, classMap);
            result.add(data);
        }
    }

    private SearchRequestBuilder searchOrderByScore(List<String> searchIndex, List<String> searchType, QueryBuilder queryBuilder, Integer pageNumber,
                                                    Integer pageSize) {
        String[] indexs = new String[searchIndex.size()];
        String[] types = new String[searchType.size()];
        return elasticsearchClient.transportClient.prepareSearch(searchIndex.toArray(indexs)).setTypes(searchType.toArray(types))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(queryBuilder).setFrom((pageNumber - 1) * pageSize).setSize(pageSize)
                .setExplain(true);
    }

    private SearchRequestBuilder searchOrderByTime(List<String> searchIndex, List<String> searchType, QueryBuilder queryBuilder, Integer pageNumber,
                                                   Integer pageSize) {
        String[] indexs = new String[searchIndex.size()];
        String[] types = new String[searchType.size()];
        return elasticsearchClient.transportClient.prepareSearch(searchIndex.toArray(indexs)).setTypes(searchType.toArray(types))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(queryBuilder).addSort("createTime", SortOrder.DESC)
                .setFrom((pageNumber - 1) * pageSize).setSize(pageSize).setExplain(true);
    }

    @Override
    public Result addData(int type, SearchData searchData) {
        boolean flag;
        List<ContentInDetail> contents;
        String diaryType = esIndexNameConfig.getType().getDiary();
        String diaryIndex = esIndexNameConfig.getJewhoneIndex(diaryType);
        DiarySearchInfo diaryInfo = new DiarySearchInfo();
        BeanUtils.copyProperties(searchData, diaryInfo);
        diaryInfo.setType(ESIndexTypeEnum.DIARY.getValue());
        contents = searchData.getContents();
        diaryInfo.setContent(searchData.getContent());
        if (!CollectionUtils.isEmpty(contents)) {
            diaryInfo.setContents(contents);
        }
        flag = elasticsearchClient.addData(diaryIndex, diaryType, searchData.getId().toString(), JsonUtil.toJson(diaryInfo));
        return Result.make(flag);
    }

    @Override
    public List<SearchData> queryLocalData(SearchParamType searchInfo) {
        List<SearchData> result = new ArrayList<>();
        // 搜索目标
        String diaryType = esIndexNameConfig.getType().getDiary();
        String diaryIndex = esIndexNameConfig.getJewhoneIndex(diaryType);

        List<String> Indices = new ArrayList<>();
        Indices.add(diaryIndex);
        List<String> types = new ArrayList<>();
        types.add(diaryType);
        // 搜索条件
        QueryBuilder s1 = QueryBuilders.termQuery("city", searchInfo.getKeyword());// 按城市排序
        QueryBuilder s2 = QueryBuilders.rangeQuery("createTime").lt(searchInfo.getData());// 按时间排序
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(s1).must(s2);
        SearchRequestBuilder srb = searchOrderByTime(Indices, types, queryBuilder, searchInfo.getPageNumber(), searchInfo.getPageSize());

        SearchResponse response = srb.get();
        SearchHits resultHits = response.getHits();
        Map<String, Class<?>> classMap = new HashMap<>();
        getResult(result, resultHits, classMap);
        return result;
    }

    @Override
    public List<SearchData> queryNewestData(SearchParamType searchInfo) {
        List<SearchData> result = new ArrayList<>();
        // 搜索目标
        String diaryType = esIndexNameConfig.getType().getDiary();
        String diaryIndex = esIndexNameConfig.getJewhoneIndex(diaryType);

        List<String> Indices = new ArrayList<>();
        Indices.add(diaryIndex);
        List<String> types = new ArrayList<>();
        types.add(diaryType);
        // 搜索条件
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("createTime").lt(searchInfo.getData());// 防重复数据
        queryBuilder = QueryBuilders.boolQuery().must(queryBuilder);
        SearchRequestBuilder srb = searchOrderByTime(Indices, types, queryBuilder, searchInfo.getPageNumber(), searchInfo.getPageSize());

        SearchResponse response = srb.get();
        SearchHits resultHits = response.getHits();
        Map<String, Class<?>> classMap = new HashMap<>();
        getResult(result, resultHits, classMap);
        return result;
    }

    @Override
    public Result deleteDataByType(Integer type, Integer id) {
        List<Integer> diaryIds = diaryMapper.queryRelatedDiary(id);
        diaryIds.add(id);
        diaryIds.forEach(diaryId -> {
            String diaryType = esIndexNameConfig.getType().getDiary();
            String diaryIndex = esIndexNameConfig.getJewhoneIndex(diaryType);
            elasticsearchClient.deleteDataById(diaryIndex, diaryType, diaryId.toString());
        });
        return Result.make(true);
    }

    @Override
    public Result addHotwordData(String text) {
        String hotwordType = esIndexNameConfig.getType().getHotword();
        String hotwordIndex = esIndexNameConfig.getJewhoneIndex(hotwordType);
        HotwordSearchInfo hotwordInfo = new HotwordSearchInfo();
        String id = MD5Util.getInstance().getMD5Code(text);
        hotwordInfo.setId(id);
        hotwordInfo.setContent(text);
        hotwordInfo.setCreateTime(new Date());
        boolean flag = elasticsearchClient.addData(hotwordIndex, hotwordType, id, JsonUtil.toJson(hotwordInfo));
        return Result.make(flag);
    }

    @Override
    public Result deleteHotwordData(String text) {
        String hotwordType = esIndexNameConfig.getType().getHotword();
        String hotwordIndex = esIndexNameConfig.getJewhoneIndex(hotwordType);
        Boolean flag = elasticsearchClient.deleteDataById(hotwordIndex, hotwordType, MD5Util.getInstance().getMD5Code(text));
        return Result.make(flag);
    }

    @Override
    public GlobalSearchCount queryCountByKeyWord(String keyword) {
        TransportClient client = elasticsearchClient.transportClient;

        String diaryType = esIndexNameConfig.getType().getDiary();
        String diaryIndex = esIndexNameConfig.getJewhoneIndex(diaryType);

        // 搜索条件
        QueryBuilder s1 = QueryBuilders.matchQuery("title", keyword);// 内容搜索
        QueryBuilder s2 = QueryBuilders.matchQuery("content", keyword);// 内容搜索
        QueryBuilder s3 = QueryBuilders.matchQuery("sourceType", 0);// 原创


        SearchRequestBuilder srb2 = client.prepareSearch(diaryIndex).setTypes(diaryType).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.boolQuery().should(s1).should(s2).must(s3).minimumShouldMatch(1)).setExplain(true);

        SearchRequestBuilder srb3 = client.prepareSearch(diaryIndex).setTypes(diaryType).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.boolQuery().should(s1).should(s2).mustNot(s3).minimumShouldMatch(1)).setExplain(true);

        long diaryCount = srb2.get().getHits().getTotalHits();
        long thirdCount = srb3.get().getHits().getTotalHits();

        GlobalSearchCount globalSearchCount = new GlobalSearchCount();
        globalSearchCount.setDiaryCount((int) diaryCount);
        globalSearchCount.setThirdPartyCount((int) thirdCount);
        return globalSearchCount;
    }

    @Override
    public List<SearchData> queryActivityData(SearchParamType searchInfo) {
        List<SearchData> result = new ArrayList<>();
        // 搜索目标
        String diaryType = esIndexNameConfig.getType().getDiary();
        String diaryIndex = esIndexNameConfig.getJewhoneIndex(diaryType);

        List<String> Indices = new ArrayList<>();
        Indices.add(diaryIndex);
        List<String> types = new ArrayList<>();
        types.add(diaryType);
        // 搜索条件
        QueryBuilder s1 = QueryBuilders.matchQuery("activityId", searchInfo.getKeyword());// 按活动名称排序
        QueryBuilder s2 = QueryBuilders.rangeQuery("createTime").lt(searchInfo.getData());// 按时间排序
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(s1).must(s2);
        SearchRequestBuilder srb = searchOrderByTime(Indices, types, queryBuilder, searchInfo.getPageNumber(), searchInfo.getPageSize());

        SearchResponse response = srb.get();
        SearchHits resultHits = response.getHits();
        Map<String, Class<?>> classMap = new HashMap<>();
        getResult(result, resultHits, classMap);
        return result;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            List<String> list = new ArrayList<>();
            list.add("1");
            list.add("1");
            list.add("1");
            list.add("1");
            ExecutorService updateMOUOSearchDiaryexecutor = Executors.newFixedThreadPool(Math.min(1000, 1000));

            List<CompletableFuture<String>> jsonList = list.stream()
                    .map(plaza -> CompletableFuture.supplyAsync(() -> getJson(plaza), updateMOUOSearchDiaryexecutor)).collect(Collectors.toList());

            List<String> talkData = jsonList.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList());
            updateMOUOSearchDiaryexecutor.shutdown();
            System.out.println(updateMOUOSearchDiaryexecutor.isTerminated());
            System.out.println(i + ":" + JsonUtil.toJson(talkData));
        }
    }

    private static String getJson(String s) {
        return s + "2";
    }

    @Override
    public Integer queryCityTalk(String keyword) {
        // 搜索目标
        String diaryType = esIndexNameConfig.getType().getDiary();
        String diaryIndex = esIndexNameConfig.getJewhoneIndex(diaryType);

        List<String> Indices = new ArrayList<>();
        Indices.add(diaryIndex);
        List<String> types = new ArrayList<>();
        types.add(diaryType);
        // 搜索条件
        QueryBuilder s1 = QueryBuilders.termQuery("city", keyword);// 按城市排序
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(s1);
        SearchRequestBuilder srb = searchOrderByTime(Indices, types, queryBuilder, 1, 1);
        SearchResponse response = srb.get();
        SearchHits resultHits = response.getHits();
        return (int) resultHits.getTotalHits();
    }
}
