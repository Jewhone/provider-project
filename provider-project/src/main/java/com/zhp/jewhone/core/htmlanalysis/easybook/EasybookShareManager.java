package com.zhp.jewhone.core.htmlanalysis.easybook;

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
 * @Title:  简书页面分析
 */
public class EasybookShareManager extends CrawlerHtml {

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
				analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
			}
			String title = parse.select("head").select("meta").select("title").text();
			if (StringUtils.isNotEmpty(title)) {
				title = title.replace("- 简书", "").trim();
			}
			 
			Element body = parse.body();
			String titles =body.select("h1.title").html();
			String htmlStr = "";

			Elements articleMain = parse.select("div.show-content");// 带图片文章
			if (articleMain.size() != 0) {
				Elements imgSrc = articleMain.select("img");
				Elements other = articleMain.select("div.image-container");
					for (int n = 0; n < imgSrc.size(); n++) {
						String src = imgSrc.get(n).attr("data-original-src");
						if(src != null && src.length()>0 && StringUtils.isNotEmpty(other)){
							imgSrc.get(n).attr("src", src);
							imgSrc.get(n).removeAttr("data-original-src");
							imgSrc.get(n).removeAttr("data-original-width");
							imgSrc.get(n).removeAttr("data-original-height");
							imgSrc.get(n).removeAttr("data-original-format");
							imgSrc.get(n).removeAttr("data-original-filesize");
							imgSrc.get(n).removeAttr("style");
							articleMain.select("div.image-container").removeAttr("style");
							articleMain.select("div.image-view").removeAttr("data-width");
							articleMain.select("div.image-view").removeAttr("data-height");
							articleMain.select("div.image-container-fill").removeAttr("style");
						}
					}
				articleMain.select("div.image-caption").remove();
				articleMain.select("a").attr("href", "javascript:void(0)");
				
				htmlStr = articleMain.html();
				analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
				String allContentText = articleMain.select("div.show-content").text();
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
			analysisResult.setTitle(titles);
			analysisResult.setWordTotal(analysisResult.getContent().length());
			analysisResult.setImgList(getImgListByHtml(body, LOAD_IMG_NUMBER,
					SourceTypeEnum.FROM_EASYBOOK));
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

}}
