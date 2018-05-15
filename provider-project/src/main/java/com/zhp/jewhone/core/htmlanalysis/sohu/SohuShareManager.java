package com.zhp.jewhone.core.htmlanalysis.sohu;

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
 * @Title:  搜狐页面分析
 */
public class SohuShareManager extends CrawlerHtml {

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
			String title = parse.title();
			if (StringUtils.isNotEmpty(title)) {
				title = title.replace("-虎嗅网", "").trim();
			}
			Element body = parse.body();
			String htmlStr = "";

			Elements articleMain = parse.select("div.at-cnt-main").select("p");// 带图片文章
			if (articleMain.size() != 0) {
				articleMain.select("a").attr("href", "javascript:void(0)");
				
				htmlStr = articleMain.html();
				analysisResult
						.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
				String contentDesc = "";
				int len = CONTENT_DESC_MAX_LENGTH;
				if (StringUtils.isNotEmpty(htmlStr)) {
						String content = StringUtils.htmlDocument(htmlStr);
						contentDesc=content.length()>len ?content.substring(0, 200):content;
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
					SourceTypeEnum.FROM_SOHU));
			if (analysisResult.getImgList() != null
					&& analysisResult.getImgList().size() > 0) {
				if(analysisResult.getImgList().get(0)!=null && analysisResult.getImgList().get(0).getImgUrl() !=null){
					map.put("pic", analysisResult.getImgList().get(0).getImgUrl());
					map.put("rawUrl", url);
				}
			}
			analysisResult.setExtendData(JsonUtil.toJson(map));
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return analysisResult;
	}
}
