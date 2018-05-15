package com.zhp.jewhone.core.htmlanalysis.krypton;

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
 * 36氪页面解析
 */
public class KryptonShareManager extends CrawlerHtml {

	public AnalysisResult analysisByUrl(String url){
		AnalysisResult analysisResult = new AnalysisResult();
		String xmlHtml = getHtmlLoadUrl(url);
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isEmpty(xmlHtml)){
			analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
		}
		Document parse = Jsoup.parse(xmlHtml);
		String title = parse.title();
		if(StringUtils.isNotEmpty(title)){
			title= title.replace("_36氪", "").trim();
		}
		Element body = parse.body();
		String subScript;
		String reScript ="";

		Elements articleMain = parse.select("script");
		if(articleMain != null && articleMain.size() > 0){
			for (Element element : articleMain) {
				String script = element.toString();
				if (script.contains("\"content\":")) {
					subScript = script.substring(
							script.indexOf("\"content\":") + "\"content\":".length(),
							script.indexOf("cover") - 2);
					subScript = subScript.trim();
					subScript = subScript.substring(1, subScript.length() - 1);
					reScript = subScript.replaceAll("\\\\", "");
					System.out.println(reScript);
					reScript = reScript.replaceAll("<p><a", "<p><span");
					System.out.println(reScript);
					reScript = reScript.replaceAll("</a", "</span");
					System.out.println(reScript);
					break;
				}
			}
			analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
//			String allContentText = parse.select("meta[name=description]").attr("content").toString();
			String contentDesc ="";
			if(StringUtils.isNotEmpty(reScript)){
				String content = StringUtils.htmlDocument(reScript);
				contentDesc=content.length()> CONTENT_DESC_MAX_LENGTH ?content.substring(0, 200):content;
			}
			analysisResult.setContentDesc(contentDesc);
			analysisResult.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);

		}else{
			analysisResult
			.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
			return analysisResult;
		}
		analysisResult.setContent(reScript);
		analysisResult.setTitle(title);
		analysisResult.setWordTotal(analysisResult.getContent().length());
		analysisResult.setImgList(getImgListByHtml(body, LOAD_IMG_NUMBER,SourceTypeEnum.FROM_36KR));
		if(analysisResult.getImgList() != null && analysisResult.getImgList().size() > 0){
			map.put("pic", analysisResult.getImgList().get(0).getImgUrl());
		}
		analysisResult.setExtendData(JsonUtil.toJson(map));
		return analysisResult;
	}	
}
