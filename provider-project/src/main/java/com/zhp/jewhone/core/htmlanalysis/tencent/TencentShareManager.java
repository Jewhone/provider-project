package com.zhp.jewhone.core.htmlanalysis.tencent;

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

/**
 * @Title:  腾讯页面分析
 */
public class TencentShareManager extends CrawlerHtml {

	@SuppressWarnings("all")
	@Override
	public AnalysisResult analysisByUrl(String url) {
		AnalysisResult analysisResult = new AnalysisResult();
		String xmlHtml = getHtmlLoadUrl(url);
		Map<String, Object> map = new HashMap<String, Object>();
		String picUrl = "";
		if (StringUtils.isEmpty(xmlHtml)) {
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
		}
		Document parse = Jsoup.parse(xmlHtml);
		String title = parse.title();
		if (StringUtils.isNotEmpty(title)) {
			title = title.replace("-腾讯新闻", "").trim();
		}
		Element body = parse.body();
		String htmlStr = "";

		Elements articleMain = parse.select("div#root");// 带图片文章
		if (articleMain.size() != 0) {
			articleMain.select("a").attr("href", "javascript:void(0)");
			
			htmlStr = articleMain.html();
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = articleMain.select("div.article-content").select("p").text();
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
				SourceTypeEnum.FROM_TENCENT));
		if (analysisResult.getImgList() != null
				&& analysisResult.getImgList().size() > 0) {
			map.put("pic", analysisResult.getImgList().get(0).getImgUrl());
		}
		analysisResult.setExtendData(JsonUtil.toJson(map));
		return analysisResult;
	}
}
