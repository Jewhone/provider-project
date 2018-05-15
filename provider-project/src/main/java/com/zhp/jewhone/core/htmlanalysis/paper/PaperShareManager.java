package com.zhp.jewhone.core.htmlanalysis.paper;

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
 * @Title:  澎湃页面分析
 */
public class PaperShareManager extends CrawlerHtml {

	@SuppressWarnings("all")
	@Override
	public AnalysisResult analysisByUrl(String url) {
		AnalysisResult analysisResult = new AnalysisResult();
		Document parse;
		try {
			//如果JSOUP获取网页数据返回403错误 原因是请求内容不全
			//Document doc = Jsoup.connect(urlString).
			//header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").
			//get();  
			parse = Jsoup.connect(url).get();
			Map<String, Object> map = new HashMap<String, Object>();
			String picUrl = "";
			if (StringUtils.isEmpty(parse)) {
				analysisResult
						.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
			}
			String title = parse.select("div.news_content").select("h1.t_newsinfo").html();
			Element body = parse.body();
			String htmlStr = "";

			Elements articleMain = parse.select("div.news_content").select("div.news_part_father");// 带图片文章
			if (articleMain.size() != 0) {
				articleMain.select("a").attr("href", "javascript:void(0)");
				articleMain.prepend("<meta name='referrer' content='no-referrer' />");
				htmlStr = articleMain.html();
				htmlStr = htmlStr.replaceAll("http:", "");
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
					SourceTypeEnum.FROM_PAPER));
			if (analysisResult.getImgList() != null
					&& analysisResult.getImgList().size() > 0) {
				map.put("pic", analysisResult.getImgList().get(0).getImgUrl());
				map.put("rawUrl", url);
			}
			analysisResult.setExtendData(JsonUtil.toJson(map));
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return analysisResult;
	}
}
