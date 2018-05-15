package com.zhp.jewhone.core.htmlanalysis.sina;

import com.zhp.jewhone.core.constant.enums.SourceTypeEnum;
import com.zhp.jewhone.core.htmlanalysis.CrawlerHtml;
import com.zhp.jewhone.core.htmlanalysis.entity.AnalysisResult;
import com.zhp.jewhone.core.util.JsonUtil;
import com.zhp.jewhone.core.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title:  新浪页面分析
 */
public class SinaShareManager extends CrawlerHtml {

	@SuppressWarnings("all")
	@Override
	public AnalysisResult analysisByUrl(String url) {
		AnalysisResult analysisResult = new AnalysisResult();
		Document parse;
		try {
			parse = Jsoup.connect(url).get();
			Map<String, Object> map = new HashMap<String, Object>();
			String picUrl = "";
			if (StringUtils.isEmpty(parse)) {
				analysisResult
						.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
			}
			String title = parse.select("head").select("title").text();
			if (StringUtils.isNotEmpty(title)) {
				title = title.replace("_手机新浪网", "").trim();
			}
			Element body = parse.body();
			String htmlStr = "";
	        Elements yuleMain = body.select("article.art_box").select("section.art_pic_card.art_content");
	        Elements slidePic = body.select("article.s_card");
			Elements articleMain = body.select("article.art_box");// 带图片文章
			if (yuleMain.size() != 0) {
				yuleMain.select("a").attr("href", "javascript:void(0)");
				htmlStr = yuleMain.html();
				analysisResult
						.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
				String allContentText = parse.select("article.art_box").select("section.art_pic_card.art_content").select("p").text();
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
			else if (slidePic.size() != 0) {
				title = title.replace("_新浪图片", "").trim();
				slidePic.select("h1.hd_h1").remove();
				slidePic.select("div.hd_h2").remove();
				slidePic.select("a").attr("href", "javascript:void(0)");
				Elements figureEl=slidePic.select("img");
				if(figureEl!=null && figureEl.size()>0){
					for (int i = 0; i < figureEl.size(); i++) {
						if(!figureEl.get(i).attr("src").toString().startsWith("http")){
							figureEl.get(i).attr("src", "http:"+figureEl.get(i).attr("src").toString());
						}
						if(figureEl.get(i).hasAttr("data-src")){						
							String imgSrc=figureEl.get(i).attr("data-src");
							figureEl.get(i).attr("src", "http:"+imgSrc);
							figureEl.get(i).removeAttr("data-src");
						}
						if(figureEl.get(i).hasAttr("alt")){
							String text=figureEl.get(i).attr("alt");
							figureEl.append("<p>" + text + "</p>");
						}
					}
				}
				htmlStr = slidePic.html();
				analysisResult
						.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
				String allContentText = parse.select("meta[name=description]").attr("content").toString();
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
			else if (articleMain.size() != 0) {
				articleMain.select("h1.art_tit_h1").remove();
				articleMain.select("section").remove();			
				articleMain.select("a").attr("href", "javascript:void(0)");
				Elements figureEl=articleMain.select("img");
				if(figureEl!=null && figureEl.size()>0){
					for (int i = 0; i < figureEl.size(); i++) {
						if(!figureEl.get(i).attr("src").toString().startsWith("http")){
							figureEl.get(i).attr("src", "http:"+figureEl.get(i).attr("src").toString());
						}
						if(figureEl.get(i).hasAttr("data-src")){						
							String imgSrc=figureEl.get(i).attr("data-src");
							figureEl.get(i).attr("src", "http:"+imgSrc);
						}
					}
				}
				articleMain.select("img.sharePic.hide").remove();
				htmlStr = articleMain.html();
				analysisResult
						.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
				String allContentText = parse.select("meta[name=description]").attr("content").toString();
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
			} else {
				analysisResult
						.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
				return analysisResult;
			}
			analysisResult.setContent(htmlStr);
			analysisResult.setTitle(title);
			analysisResult.setWordTotal(analysisResult.getContent().length());
			analysisResult.setImgList(getImgListByHtml(body, LOAD_IMG_NUMBER,
					SourceTypeEnum.FROM_NETEASE));
			if (analysisResult.getImgList() != null
					&& analysisResult.getImgList().size() > 0) {
				map.put("pic", analysisResult.getImgList().get(0).getImgUrl());
			}
			analysisResult.setExtendData(JsonUtil.toJson(map));
			return analysisResult;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return analysisResult;

	}
}
