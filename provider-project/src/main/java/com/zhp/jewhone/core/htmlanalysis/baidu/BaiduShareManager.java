package com.zhp.jewhone.core.htmlanalysis.baidu;

import com.zhp.jewhone.core.constant.Const;
import com.zhp.jewhone.core.constant.enums.SourceTypeEnum;
import com.zhp.jewhone.core.htmlanalysis.CrawlerHtml;
import com.zhp.jewhone.core.htmlanalysis.entity.AnalysisResult;
import com.zhp.jewhone.core.util.JsonUtil;
import com.zhp.jewhone.core.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class BaiduShareManager extends
        CrawlerHtml {

    @SuppressWarnings("all")
    @Override
    public AnalysisResult analysisByUrl(String url) {
        AnalysisResult analysisResult = new AnalysisResult();
        String xmlHtml = getHtmlLoadUrl(url);
        Map<String, Object> map = new HashMap<String, Object>();
        String picUrl = "";
        if (StringUtils.isEmpty(xmlHtml)) {
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
        }
        Document parse = Jsoup.parse(xmlHtml);//解析页面
        String title = parse.title();//标题
        if (StringUtils.isNotEmpty(title)) {
            title = title.replace("- 百度新闻", "").trim();
        }
        Element body = parse.body();
        String htmlStr = "";
        System.out.println(body.toString());
        Elements articleMain = parse.select("div.mainContent");// 百家号
        Elements wenKu = parse.select("div.ie-fix");// 文库
        Elements wenKuTxt = parse.select("div.doc-content-txt");// 文库Txt
        Elements wenKuMain = parse.select("div#reader-pageNo-1");// 百度文库
        Elements jingYan = parse.select("div#format-exp");// 经验
        Elements baiKe = body.select("div.content-wrapper");// 百度百科
        Elements baiKeMain = body.select("div.J-lemma");// 百科人物
        Elements tieBa = parse.select("div.left_section").select("cc");// 贴吧
        Elements tieBaMain = body.select("div.root");// 贴吧单个内容
        String allContentText = Const.BAIDU_SHARE_CONTENT;
        if (articleMain.size() != 0) {
            articleMain.select("div").removeAttr("style");
            articleMain.select("a").attr("href", "javascript:void(0)");
            htmlStr = articleMain.html();
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            allContentText = articleMain.select("div.mainContent")
                    .text();
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(htmlStr)) {
                String content = StringUtils.htmlDocument(htmlStr);
                contentDesc = content.length() > len ? content
                        .substring(0, 200) : content;
            }
            analysisResult.setContent(htmlStr);
            analysisResult.setContentDesc(contentDesc);
            analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
        } else if (wenKu.size() != 0) {
            title = title.replace("_百度文库", "").trim();
            wenKu.select("a").attr("href", "javascript:void(0)");
            wenKu.select("p").removeAttr("style");
            parse.select("img").remove();
            htmlStr = wenKu.html().replaceAll("\n", "");
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(htmlStr)) {
                String content = StringUtils.htmlDocument(htmlStr);
                contentDesc = content.length() > len ? content
                        .substring(0, 200) : content;
            }
            analysisResult.setContent(htmlStr);
            analysisResult.setContentDesc(contentDesc);
            analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
        } else if (wenKuTxt.size() != 0) {
            title = parse.select("div.doc-title").html();
            wenKuTxt.select("a").attr("href", "javascript:void(0)");
            parse.select("img").remove();
            htmlStr = wenKuTxt.html();
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(htmlStr)) {
                String content = StringUtils.htmlDocument(htmlStr);
                contentDesc = content.length() > len ? content
                        .substring(0, 200) : content;
            }
            analysisResult.setContent(htmlStr);
            analysisResult.setContentDesc(contentDesc);
            analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
        } else if (wenKuMain.size() != 0) {
            title = title.replace("_百度文库", "").trim();
            wenKuMain.select("a").attr("href", "javascript:void(0)");
            wenKuMain.select("p").removeAttr("style");
            parse.select("img").remove();
            htmlStr = wenKuMain.html();
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(htmlStr)) {
                String content = StringUtils.htmlDocument(htmlStr);
                contentDesc = content.length() > len ? content
                        .substring(0, 200) : content;
            }
            analysisResult.setContent(htmlStr);
            analysisResult.setContentDesc(contentDesc);
            analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
        } else if (baiKe.size() != 0) {
            title = title.replace("_百度百科", "").trim();
            baiKe.select("a").attr("href", "javascript:void(0)");
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(allContentText)) {
                if (allContentText.length() < len) {
                    len = allContentText.length();
                }
                contentDesc = allContentText.substring(0, len);
            }
            analysisResult.setContent(url);
            analysisResult.setContentDesc(contentDesc);
            analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
        } else if (baiKeMain.size() != 0) {
            title = title.replace("_百度百科", "").trim();
            baiKeMain.select("a").attr("href", "javascript:void(0)");
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(allContentText)) {
                if (allContentText.length() < len) {
                    len = allContentText.length();
                }
                contentDesc = allContentText.substring(0, len);
            }
            analysisResult.setContent(url);
            analysisResult.setContentDesc(contentDesc);
            analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
        } else if (jingYan.size() != 0) {
            title = title.replace("_百度经验", "").trim();
            jingYan.select("a").attr("href", "javascript:void(0)");
            parse.select("img").remove();
            jingYan.select("span").remove();
            jingYan.select("div.content-listblock-media").remove();
            htmlStr = jingYan.html();
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(htmlStr)) {
                String content = StringUtils.htmlDocument(htmlStr);
                contentDesc = content.length() > len ? content
                        .substring(0, 200) : content;
            }
            analysisResult.setContent(htmlStr);
            analysisResult.setContentDesc(contentDesc);
            analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
        } else if (tieBa.size() != 0) {
            title = parse.select("div.left_section").select("h1").html();
            tieBa.select("a").attr("href", "javascript:void(0)");
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(allContentText)) {
                if (allContentText.length() < len) {
                    len = allContentText.length();
                }
                contentDesc = allContentText.substring(0, len);
            }
            analysisResult.setContent(url);
            analysisResult.setContentDesc(contentDesc);
            analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
        } else if (tieBaMain.size() != 0) {
            tieBaMain.select("a").attr("href", "javascript:void(0)");
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(allContentText)) {
                if (allContentText.length() < len) {
                    len = allContentText.length();
                }
                contentDesc = allContentText.substring(0, len);
            }
            analysisResult.setContent(url);
            analysisResult.setContentDesc(contentDesc);
            analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
        } else {
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
            return analysisResult;
        }
        analysisResult.setTitle(title);
        analysisResult.setWordTotal(analysisResult.getContent().length());
        analysisResult.setImgList(getImgListByHtml(body,
                LOAD_IMG_NUMBER, SourceTypeEnum.FROM_NETEASE));
        if (analysisResult.getImgList() != null
                && analysisResult.getImgList().size() > 0
                && analysisResult.getImgList().get(0) != null
                && analysisResult.getImgList().get(0)
                .getImgUrl() != null) {
            map.put("pic",
                    analysisResult.getImgList().get(0)
                            .getImgUrl());
        } else {
            try {
                analysisResult.setImgList(getImgListByHtml(
                        body, LOAD_IMG_NUMBER, SourceTypeEnum.FROM_NETEASE));
                map.put("pic",
                        analysisResult.getImgList().get(0)
                                .getImgUrl());
                map.put("rawUrl", url);
            } catch (Exception e) {
                System.err.println("图片源有误！[" + analysisResult.getImgList().get(0)
                        .getImgUrl() + "]");
            }
        }
        analysisResult.setExtendData(JsonUtil.toJson(map));
        return analysisResult;
    }
}
