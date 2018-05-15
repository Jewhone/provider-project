package com.zhp.jewhone.core.htmlanalysis.qingmang;

import com.zhp.jewhone.core.htmlanalysis.CrawlerHtml;
import com.zhp.jewhone.core.htmlanalysis.entity.AnalysisResult;
import com.zhp.jewhone.core.htmlanalysis.huxiu.HuxiuShareManager;
import com.zhp.jewhone.core.util.JsonUtil;
import com.zhp.jewhone.core.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title:  青芒页面分析
 */
public class QingmangShareManager extends CrawlerHtml {

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
		Element body = parse.body();
		String htmlStr = "";
		String articleUrl = "";
		Elements articleMain = body.select("article.text-center").select("p").select("a.btn.btn-primary");//文章地址
		if (articleMain.size() != 0) {
			articleUrl = articleMain.attr("href").toString();   
			if(articleUrl.indexOf("huxiu")>0){      //虎嗅网
				if(articleUrl.indexOf("www")>0){
					articleUrl=articleUrl.replaceAll("www", "m")+"?";
					HuxiuShareManager huxiuShareManager = new HuxiuShareManager();
					analysisResult=huxiuShareManager.analysisByUrl(articleUrl);
				}
			} 
			}else {
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
			return analysisResult;
		}
		if (analysisResult.getImgList() != null
				&& analysisResult.getImgList().size() > 0) {
			map.put("pic", analysisResult.getImgList().get(0).getImgUrl());
			map.put("rawUrl", articleUrl);
		}
		analysisResult.setExtendData(JsonUtil.toJson(map));
		return analysisResult;
	}
	
}
