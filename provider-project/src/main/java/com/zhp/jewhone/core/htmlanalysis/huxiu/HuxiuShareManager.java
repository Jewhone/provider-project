/**  
 * @Title:  虎嗅页面分析
 * @Package com.immouo.moyou.service.userreceiveshare.manager   
 * @Description:    TODO
 * @author: chenhuajiang
 * @date:   2017年11月30日 下午2:45:20    
 * @version V3.4
 */
package com.zhp.jewhone.core.htmlanalysis.huxiu;

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

public class HuxiuShareManager extends CrawlerHtml {

	@SuppressWarnings("all")
	@Override
	public AnalysisResult analysisByUrl(String url) {
		AnalysisResult userReceiveShareDataAnalysisResult = new AnalysisResult();
		String xmlHtml = getHtmlLoadUrl(url);
		Map<String, Object> map = new HashMap<String, Object>();
		String picUrl = "";
		if (StringUtils.isEmpty(xmlHtml)) {
			userReceiveShareDataAnalysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
		}
		Document parse = Jsoup.parse(xmlHtml);
		String title = parse.title();
		if (StringUtils.isNotEmpty(title)) {
			title = title.replace("-虎嗅网", "").trim();
		}
		Element body = parse.body();
		String htmlStr = "";

		Elements articleMain = parse.select("div.article-content");// 带图片文章
		if (articleMain.size() != 0) {
			articleMain.select("a").attr("href", "javascript:void(0)");
			
			htmlStr = articleMain.html();
			userReceiveShareDataAnalysisResult
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
			userReceiveShareDataAnalysisResult.setContentDesc(contentDesc);
			userReceiveShareDataAnalysisResult
					.setShareMedia(AnalysisResult.SHARE_MEDIA_ARTICLE);
		} else {
			userReceiveShareDataAnalysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
			return userReceiveShareDataAnalysisResult;
		}
		userReceiveShareDataAnalysisResult.setContent(htmlStr);
		userReceiveShareDataAnalysisResult.setTitle(title);
		userReceiveShareDataAnalysisResult.setWordTotal(userReceiveShareDataAnalysisResult.getContent().length());
		userReceiveShareDataAnalysisResult.setImgList(getImgListByHtml(body, LOAD_IMG_NUMBER,
				SourceTypeEnum.FROM_NETEASE));
		if (userReceiveShareDataAnalysisResult.getImgList() != null
				&& userReceiveShareDataAnalysisResult.getImgList().size() > 0) {
			map.put("pic", userReceiveShareDataAnalysisResult.getImgList().get(0).getImgUrl());
		}
		userReceiveShareDataAnalysisResult.setExtendData(JsonUtil.toJson(map));
		return userReceiveShareDataAnalysisResult;
	}
}
