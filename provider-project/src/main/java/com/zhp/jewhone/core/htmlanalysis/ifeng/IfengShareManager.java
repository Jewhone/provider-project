package com.zhp.jewhone.core.htmlanalysis.ifeng;

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
 * 凤凰网页面解析
 */
public class IfengShareManager extends CrawlerHtml {
	
	public AnalysisResult analysisByUrl(String url){
		AnalysisResult analysisResult = new AnalysisResult();
		String xmlHtml = getHtmlLoadUrl(url);
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isEmpty(xmlHtml)) {
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
		}
		Document parse = Jsoup.parse(xmlHtml);
		String title = parse.title();
		if (StringUtils.isNotEmpty(title)) {
			title = title.replace("-凤凰网", "").trim();
		}
		Element body = parse.body();
		String htmlStr;
		Elements articleMain= body.select("div.n-words");
		Elements PicAndText = body.select("div.pic_word");
		if(articleMain != null && articleMain.size() > 0 && PicAndText.size() ==0){
			articleMain.select("a").attr("href", "javascript:void(0)");
			Elements pic = articleMain.select("img");
			updateAttr(pic);
			articleMain.select("img.showbar_shadow").remove();
			articleMain.select("img#showbar").remove();
			articleMain.select("div.n-video").remove();
			htmlStr = articleMain.html();
			analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = Const.IFENG_SHARE_CONTENT;
			String contentDesc ="";
			StringUtils.isEmpty(contentDesc);
			analysisResult.setContentDesc(allContentText);
			analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
		}else if(PicAndText != null && PicAndText.size() > 0){
			PicAndText.select("a").attr("href", "javascript:void(0)");
			Elements pic = PicAndText.select("article").select("img");
			updateAttr(pic);
			PicAndText.select("span").remove();
			htmlStr = PicAndText.html();
			analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String contentDesc ="";
			if(StringUtils.isEmpty(htmlStr)){
				String content = StringUtils.htmlDocument(htmlStr);
				contentDesc=content.length()> CONTENT_DESC_MAX_LENGTH ?content.substring(0, 200):content;
			}
			analysisResult.setContentDesc(contentDesc);
			analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
		}
		else{
			analysisResult
			.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
			return analysisResult;
		}
		analysisResult.setContent(htmlStr);
		analysisResult.setTitle(title);
		analysisResult.setWordTotal(analysisResult.getContent().length());
		analysisResult.setImgList(getImgListByHtml(body, LOAD_IMG_NUMBER, SourceTypeEnum.FROM_NETEASE));
		if(analysisResult.getImgList() != null && analysisResult.getImgList().size() > 0){
			map.put("pic", analysisResult.getImgList().get(0).getImgUrl());			
		}
		analysisResult.setExtendData(JsonUtil.toJson(map));
		return analysisResult;
	}

	private void updateAttr(Elements pic) {
		if(pic!=null && pic.size()>0){
            for (Element aPic : pic) {
                if (aPic.hasAttr("data-original")) {
                    String imgSrc = aPic.attr("data-original");
                    aPic.attr("src", imgSrc);
                    aPic.removeAttr("data-original");
                }
            }
        }
	}
}
