package com.zhp.jewhone.core.htmlanalysis.zhihu;

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

/**
 * @Title:  知乎页面分析
 */
public class ZhihuShareManager extends CrawlerHtml {

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
			title = title.replace("- 知乎", "").trim();
		}
		Element body = parse.body();
		String htmlStr = "";
		Elements articleMain = body.select("div.RichContent-inner");// 带图片文章
		Elements zhuanLanArticle = body.select("div.RichText.PostIndex-content.av-paddingSide.av-card");
			if (articleMain.size() != 0) {			   //问答
				Elements img = articleMain.select("img");
				if (img != null && img.size() > 0) {
					for (int n = 0; n < img.size(); n++) {
						String imgList = img.get(n).attr("data-original");
						if(imgList.indexOf("https")>=0){
							String imgSrc = imgList.substring(6);
							img.get(n).removeAttr("src");
							img.get(n).removeAttr("width");
							img.get(n).removeAttr("data-actualsrc");
							img.get(n).removeAttr("data-original");
							img.get(n).attr("src", "http:"+imgSrc);							
						}else{
							img.get(n).removeAttr("src");
							img.get(n).removeAttr("width");
							img.get(n).removeAttr("data-actualsrc");
							img.get(n).removeAttr("data-original");
							img.get(n).attr("src", imgList);
						}			
					}
				}
				articleMain.select("a").attr("href", "javascript:void(0)");
				articleMain.prepend("<meta name='referrer' content='no-referrer' />");
				htmlStr = articleMain.html();
				analysisResult
						.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
				String allContentText = Const.ZHIHU_SHARE_CONTENT;
				String contentDesc = "";
				int maxLength = CONTENT_DESC_MAX_LENGTH;
				if (StringUtils.isNotEmpty(htmlStr)) {
					String content = StringUtils.htmlDocument(htmlStr);
					contentDesc=content.length()>maxLength ?content.substring(0, 200):content;
				}
				analysisResult.setContentDesc(contentDesc);
				analysisResult
						.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
			}else if (zhuanLanArticle != null && zhuanLanArticle.size() != 0) {     //专栏
				Elements img = zhuanLanArticle.select("img");
				if (img != null && img.size() > 0) {
					for (int n = 0; n < img.size(); n++) {
						String imgList = img.get(n).attr("data-actualsrc");
						if(imgList.indexOf("https")>=0){
							String imgSrc = imgList.substring(6);
							img.get(n).removeAttr("src");
							img.get(n).removeAttr("width");
							img.get(n).removeAttr("data-actualsrc");
							img.get(n).removeAttr("data-original");
							img.get(n).attr("src", "http:"+imgSrc);							
						}else{
							img.get(n).removeAttr("src");
							img.get(n).removeAttr("width");
							img.get(n).removeAttr("data-actualsrc");
							img.get(n).removeAttr("data-original");
							img.get(n).attr("src", imgList);
						}			
					}
				}
				zhuanLanArticle.select("a").attr("href", "javascript:void(0)");
				zhuanLanArticle.select("span").select("div").remove();
				zhuanLanArticle.prepend("<meta name='referrer' content='no-referrer' />");
					title=title.replace("- 知乎专栏", "").trim();
					htmlStr = zhuanLanArticle.html();
					analysisResult
							.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
					String allContentText = Const.ZHIHU_SHARE_CONTENT;
					String contentDesc = "";
					int maxLength = CONTENT_DESC_MAX_LENGTH;
					if (StringUtils.isNotEmpty(htmlStr)) {
						String content = StringUtils.htmlDocument(htmlStr);
						contentDesc=content.length()>maxLength ?content.substring(0, 200):content;
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
				SourceTypeEnum.FROM_ZHIHU));
		if (analysisResult.getImgList() != null
				&& analysisResult.getImgList().size() > 0) {
			map.put("pic", analysisResult.getImgList().get(0).getImgUrl());
		}
		analysisResult.setExtendData(JsonUtil.toJson(map));
		return analysisResult;
	}
}
