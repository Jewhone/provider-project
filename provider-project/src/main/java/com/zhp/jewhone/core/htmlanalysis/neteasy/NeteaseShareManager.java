package com.zhp.jewhone.core.htmlanalysis.neteasy;

import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;
import java.util.Map;

/**
 * @Title: 网易新闻页面分析
 */
public class NeteaseShareManager extends CrawlerHtml {

    @SuppressWarnings("all")
    @Override
    public AnalysisResult analysisByUrl(String url) {
        AnalysisResult analysisResult = new AnalysisResult();
        String xmlHtml = getHtmlLoadUrl(url);
        Map<String, Object> map = new HashMap<String, Object>();
        String videoUrl = "";
        String picUrl = "";
        if (StringUtils.isEmpty(xmlHtml)) {
            analysisResult
                    .setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
        }
        Document parse = Jsoup.parse(xmlHtml);
        String title = parse.title();
        Element body = parse.body();
        String htmlStr = "";
        String imgSrc = "";
        Elements articleMain = null;

        Elements figureEl = parse.select("figure.js-img.u-img-placeholder");// 带图片文章
        Elements videos = parse.select("div.video-holder");// 视频
        Elements onlyText = parse.select("article").select("main");// 纯文本
        Elements picAndText = body.select("script");// 滑动图片的文章
        if (figureEl != null && figureEl.size() > 0) {
            for (int n = 0; n < figureEl.size(); n++) {
                if (figureEl.get(n).attr("data-echo") != null
                        && figureEl.get(n).attr("data-echo").endsWith("=webp")) {
                    String regex = "=webp";
                    String newImgSrc = figureEl.get(n).attr("data-echo")
                            .replace(regex, "=jpg");
                    imgSrc = newImgSrc;
                } else {
                    imgSrc = figureEl.get(n).attr("data-echo");
                }
                figureEl.get(n).append("<img src='" + imgSrc + "'/>");// 把照片生成新格式
                figureEl.get(n).removeAttr("data-min");
                figureEl.get(n).removeAttr("data-retry");
                figureEl.get(n).removeAttr("data-param");
                figureEl.get(n).removeAttr("data-stat");
                figureEl.get(n).removeAttr("data-alt");

            }
            articleMain = body.select("div.js-article-inner");
            if (articleMain.size() != 0) {
                // 去掉不需要的元素

                articleMain.select("h1.g-title").remove();
                articleMain.select("div.g-subtitle").remove();
                articleMain.select("footer").remove();
                articleMain.select("div.u-tip").remove();
                articleMain.select("a").attr("href", "javascript:void(0)");
                htmlStr = articleMain.html().replaceAll("amp;", "");
                analysisResult
                        .setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
                String allContentText = articleMain.select("main").text();
                String contentDesc = "";
                int len = CONTENT_DESC_MAX_LENGTH;
                if (StringUtils.isNotEmpty(allContentText)) {
                    if (allContentText.length() < len) {
                        len = allContentText.length();
                    }
                    contentDesc = allContentText.substring(0, len);
                }
                analysisResult.setContentDesc(contentDesc);
                analysisResult
                        .setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
            }
        } else if (videos != null && videos.size() > 0) {
            // 去掉不需要的元素
            articleMain = videos.clone();
            articleMain.select("div.end").remove();
            articleMain.select("div.m-video-wifi").remove();
            articleMain.select("a.u-video-open-tip").remove();
            articleMain.select("div.video-subtitle").remove();
            articleMain.select("div.video-btn").remove();
            articleMain.select("div.btn").remove();
            articleMain.select("div.u-play-btn").remove();
            articleMain.select("div.video-desc-wrap").remove();
            articleMain.select("div.video-title").remove();
            articleMain.select("img[src~=(?i)\\.(png|jpe?g)]").remove();
            if (articleMain.select("video").hasAttr("src")) {
                videoUrl = articleMain.select("video").attr("src");
                articleMain.select("video").attr("src", "http:" + videoUrl);
            }
            htmlStr = articleMain.html();
            analysisResult.setContent(htmlStr);
            analysisResult
                    .setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String allContentText = title;
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(allContentText)) {
                if (allContentText.length() < len) {
                    len = allContentText.length();
                }
                contentDesc = allContentText.substring(0, len);
            }
            analysisResult.setContentDesc(contentDesc);
            map.put("video", "http:" + videoUrl);
            map.put("pic", picUrl);
            map.put("contentDesc", contentDesc);
            analysisResult.setVideoUrl(videoUrl);
            analysisResult.setExtendData(JsonUtil
                    .toJson(map));
            analysisResult
                    .setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            analysisResult
                    .setShareMedia(analysisResult.SHARE_MEDIA_VIDEO);
        } else if (onlyText != null && onlyText.size() > 0) {
            htmlStr = onlyText.html();
            analysisResult
                    .setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String allContentText = onlyText.select("main").text();
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(allContentText)) {
                if (allContentText.length() < len) {
                    len = allContentText.length();
                }
                contentDesc = allContentText.substring(0, len);
            }
            analysisResult.setContentDesc(contentDesc);
            analysisResult
                    .setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
        } else if (picAndText != null && picAndText.size() != 0) {
            Elements titleList = null;
            for (Element element : picAndText) {
                String script = element.data().toString();
                if (script.contains("window.PHOTOS_INFO")) {
                    String subScript = script.substring(
                            script.indexOf("window.PHOTOS_INFO = ")
                                    + "window.PHOTOS_INFO = ".length(),
                            script.indexOf("//]]"));
                    subScript = subScript.trim();
                    List<Map<String, Object>> listSubScript = JsonUtil
                            .toCollection(
                                    subScript,
                                    new TypeReference<List<Map<String, Object>>>() {
                                    });
                    picAndText = parse.select("body").html("");
                    for (int i = 0; i < listSubScript.size(); i++) {
                        picAndText.append("<p>"
                                + listSubScript.get(i).get("note") + "</p>");
                        picAndText.append("<img src='" + "http:"
                                + listSubScript.get(i).get("img")
                                + "' alt='图片'/>");
                    }
                    break;
                }
            }
            htmlStr = picAndText.html();
            analysisResult
                    .setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
            String allContentText = picAndText.text();
            String contentDesc = "";
            int len = CONTENT_DESC_MAX_LENGTH;
            if (StringUtils.isNotEmpty(allContentText)) {
                if (allContentText.length() < len) {
                    len = allContentText.length();
                }
                contentDesc = allContentText.substring(0, len);
            }
            analysisResult.setContentDesc(contentDesc);
        } else {
            analysisResult
                    .setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
            return analysisResult;
        }
        analysisResult.setContent(htmlStr);
        analysisResult.setTitle(title);
        analysisResult
                .setWordTotal(analysisResult.getContent()
                        .length());
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
            map.put("rawUrl", url);
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
