package com.zhp.jewhone.core.htmlanalysis.wechat;

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
 * @Title:  微信页面分析
 */
public class WeChatShareManager extends CrawlerHtml {

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
		Element body = parse.body();
		String htmlStr = "";
		Elements articleMain = body.select("div.rich_media_content");// 带图片文章
		if (articleMain.size() != 0) {
			articleMain.select("a").attr("href", "javascript:void(0)");
			articleMain.select("mpvoice").remove();
			Elements figureEl=articleMain.select("img");
			if(figureEl!=null && figureEl.size()>0){
				for (int i = 0; i < figureEl.size(); i++) {
					if(figureEl.get(i).hasAttr("data-src")){
							String imgSrc=figureEl.get(i).attr("data-src");
							if(!imgSrc.startsWith("http")){
								imgSrc ="http:"+imgSrc;
							}
							if(imgSrc.lastIndexOf("=")>0){
								imgSrc=imgSrc.substring(0,imgSrc.lastIndexOf("="));
								imgSrc=imgSrc+"=jpg";
							}
							figureEl.get(i).attr("src", imgSrc);
							figureEl.get(i).removeAttr("style");
							figureEl.get(i).removeAttr("data-src");
					}
				}
			}
			articleMain.prepend("<meta name='referrer' content='no-referrer' />");
			htmlStr = articleMain.html();
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = Const.WECHAT_SHARE_CONTENT;
			String contentDesc = "";
			int len = CONTENT_DESC_MAX_LENGTH;
			if (StringUtils.isNotEmpty(htmlStr)) {
				String content = StringUtils.htmlDocument(htmlStr);
				contentDesc=content.length()>len ?content.substring(0, 200):content;

				contentDesc =StringUtils.htmlDocument(htmlStr).substring(0, 200);
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
	}
}
