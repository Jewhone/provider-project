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
 * @Title:  新浪视频页面分析
 */
public class SinaVideoShareManager extends CrawlerHtml {

	@SuppressWarnings("all")
	@Override
	public AnalysisResult analysisByUrl(String url) {
		AnalysisResult analysisResult = new AnalysisResult();
		Document parse;
		try {
			parse = Jsoup.connect(url).get();
			Map<String, Object> map = new HashMap<String, Object>();
			String videoUrl = "";
			String picUrl = "";
			if (StringUtils.isEmpty(parse)) {
				analysisResult
						.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
			}
			String title = parse.select("head").select("title").text();
			if (StringUtils.isNotEmpty(title)) {
				title = title.replace("_新浪视频", "").trim();
			}
			Element body = parse.body();
			String htmlStr = "";
			Elements Video = body.select("script");// 带图片文章
				if (Video.size() != 0) {
					for (Element element : Video) {
	                 	String script = element.data().toString();
	                 	if(script.contains("_wxShareConfig")){
	                 		String subScript = script.substring(script.indexOf("link: '")+"link: '".length(),script.indexOf("imgUrl"));
	                 		videoUrl=subScript.substring(0, subScript.indexOf("'"));
	                 		String subScripts = script.substring(script.indexOf("imgUrl: '")+"imgUrl: '".length(),script.indexOf("type"));
	                 		picUrl=subScripts.substring(0, subScript.indexOf("'"));
	                 		break;
	     			}
					}
					htmlStr ="<img src='" +"http:"+ picUrl + "'/>";
					analysisResult.setContent(htmlStr);
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
					map.put("video", videoUrl);
					map.put("pic", picUrl);
					map.put("contentDesc", contentDesc);
					analysisResult.setVideoUrl(videoUrl);
					analysisResult.setExtendData(JsonUtil.toJson(map));
					analysisResult
							.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
					analysisResult
							.setShareMedia(analysisResult.SHARE_MEDIA_ARTICLE);
				} else {
				analysisResult
						.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
				return analysisResult;
			}
			analysisResult.setContent(htmlStr);
			analysisResult.setTitle(title);
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
