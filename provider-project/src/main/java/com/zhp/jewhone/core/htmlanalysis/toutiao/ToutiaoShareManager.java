package com.zhp.jewhone.core.htmlanalysis.toutiao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhp.jewhone.core.constant.enums.SourceTypeEnum;
import com.zhp.jewhone.core.htmlanalysis.CrawlerHtml;
import com.zhp.jewhone.core.htmlanalysis.entity.AnalysisResult;
import com.zhp.jewhone.core.util.JsonUtil;
import com.zhp.jewhone.core.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Base64Utils;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title:  今日头条页面解析
 */
public class ToutiaoShareManager extends CrawlerHtml {

	@SuppressWarnings("all")
	@Override
	public AnalysisResult analysisByUrl(String url) {
		AnalysisResult analysisResult = new AnalysisResult();
		String xmlHtml = getHtmlLoadUrl(url);
		Map<String, Object> map = new HashMap<String, Object>();
		String videoUrl = "";
		String picUrl ="";
		String videoId ="";
		if (StringUtils.isEmpty(xmlHtml)) {
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
		}
		Document parse = Jsoup.parse(xmlHtml);
		String videoTitle ="";
		String title = parse.title();
		if (StringUtils.isNotEmpty(title)) {
			title = title.replace("- 今日头条(www.toutiao.com)", "").trim();
		}
		Element body = parse.body();
		body.select("img.ceicon").remove();
		String htmlStr = "";
		Elements articleMain = body.select("div.article-content");				// 头条自带文章
		Elements articleMainPlayer = body.select("span.fitem");		    		// 视频
		Elements xinHuaNetArticleMain = body.select("div#content");             // 新华网
		Elements pic = body.select("div.bui-box.gallery-inner").select("img");	//今日头条内置的滑动图片
		Elements youthpic=body.select("figure");								//中国青年网滑动图片,光明网滑动图片
		Elements guanChaArticleMain = body.select("article");					// 头条文章里的观察者网,中国青年网文章,中国经济网,光明网
		Elements huanQiuArticleMain =body.select("div.text").select("p");		//环球网
		Elements wuKongAnswer = body.select("div.answer-text-full.rich-text");  //悟空问答
		Elements huiShengHuoArticleMain=body.select("div.content");             //会生活		
		if (articleMain.size() != 0) {
			articleMain.select("a").attr("href", "javascript:void(0)");
			htmlStr = articleMain.html();
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = articleMain.text();
			String contentDesc = "";
			int len = CONTENT_DESC_MAX_LENGTH;
			if (StringUtils.isNotEmpty(allContentText)) {
				if (allContentText.length() < len) {
					len = allContentText.length();
				}
				contentDesc = allContentText.substring(0, len);
			}
			analysisResult.setTitle(title);
			analysisResult.setContentDesc(contentDesc);
			analysisResult
			.setShareMedia(analysisResult.SHARE_MEDIA_ARTICLE);
		}else if(xinHuaNetArticleMain.size() != 0) {
			title=title.replace("-新华网", "");			
			xinHuaNetArticleMain.select("iframe.video-frame").remove();
			xinHuaNetArticleMain.select("a").attr("href", "javascript:void(0)");			
			htmlStr = xinHuaNetArticleMain.html();
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = xinHuaNetArticleMain.text();
			String contentDesc = "";
			int len = CONTENT_DESC_MAX_LENGTH;
			if (StringUtils.isNotEmpty(allContentText)) {
				if (allContentText.length() < len) {
					len = allContentText.length();
				}
				contentDesc = allContentText.substring(0, len);
			}
			analysisResult.setTitle(title);
			analysisResult.setContentDesc(contentDesc);
			analysisResult
			.setShareMedia(analysisResult.SHARE_MEDIA_ARTICLE);
		}else if(articleMainPlayer.size()!=0 && articleMainPlayer != null){
        	String toutiao_url="http://ib.365yg.com/video/urls/v/1/toutiao/mp4/";
//        	String befoCrc = "http://ib.365yg.com/video/urls/v/1/toutiao/mp4/";
            String classPath = ToutiaoShareManager.class.getResource("/").toString();
            String jsPath = classPath + "toutiao/videourl.js";   
            jsPath = jsPath.substring(5);
            String base64Url = "";          
            try {
            	 Elements e = parse.getElementsByTag("script"); //通过script标签找到videoId的值                 
                 for (Element element : e) {
                 	String script = element.data().toString();
                 	if(script.contains("videoid")){
                 		String subScript = script.substring(script.indexOf("videoid:'")+"videoid:'".length(),script.indexOf("share_url"));
                 		videoId=subScript.substring(0, subScript.indexOf("'"));
                 		break;
                 	}else if(script.contains("videoId")){
                 		int index = script.indexOf("videoId:");
                 		int length = "videoId:'".length();
                 		videoId=script.substring(index+length+1, index+length+33);
                 		break; 		
                 	}
     			}
                 for (Element element : e) {
                  	String script = element.data().toString();
                  	 if(script.contains("abstract={")){
                 		videoTitle=script.substring(script.indexOf("abstract={")+"abstract={".length(),script.indexOf("'.replace"));
                 		String[] videoTitleArray=videoTitle.split("'");
                 		if(videoTitleArray.length>0){
                 			videoTitle=videoTitleArray[1];
                 		}
                   		break;
                   	}
      			}
                
                toutiao_url= toutiao_url + videoId;  //重构url
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
                Bindings bind =engine.createBindings();
                bind.put("factor", 0);
                engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);               
                engine.eval(new FileReader(jsPath));
                Object js_result = engine.eval("crc32(' + " + toutiao_url + "')");
                base64Url = js_result.toString();
                Document doc = Jsoup.connect(base64Url).ignoreContentType(true).get();
                Element newBody = doc.body();
                String json =newBody.text();
                //json中main_url为视频地址,通过base64解码得到视频链接
                String base64 = json.substring(json.indexOf("main_url\":\"")+"main_url\":\"".length(),json.indexOf("\",\"backup_url_1"));//视频地址
                picUrl = json.substring(json.indexOf("poster_url\":\"")+"poster_url\":\"".length(),json.indexOf("\",\"video_duration"));//封面图片
                byte[] bytes=Base64Utils.decodeFromString(base64);
                //视频链接
                videoUrl = new String(bytes);
                if(articleMainPlayer.select("source").hasAttr("src")){
                	articleMainPlayer.select("source").attr("src",videoUrl);//页面地址换为base64解码后的链接               	
                }else if(articleMainPlayer !=null){
                	articleMainPlayer.append("<img src='" + picUrl + "'/>"+"<video src='" + videoUrl + "'/>"+"<source type='video/mp4' src='" + videoUrl + "'/>"+"</video>");
                }
			}            
        	catch (Exception e) {
				e.printStackTrace();
			}    	
//        	body.select("img[src~=http|https]").remove();//去掉多余的图片，头条每次都会丢一个头像图片进来
        	body.select("hi.abs-title").remove();
//        	for (int n = 0; n < articleMainPlayer.size(); n++) {  //添加封面图到content
//        		articleMainPlayer.get(n).attr("player-inner");
//				articleMainPlayer.get(n).append("<img src='" + picUrl + "'/>");
//			}
        	   	htmlStr = articleMainPlayer.html();
        	   	analysisResult.setContent(htmlStr);
        	   	map.put("video", videoUrl);
        	   	map.put("pic", picUrl);
        	   	map.put("videoId", videoId);
        	   	analysisResult.setExtendData(JsonUtil.toJson(map));
        	   	analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS); 
        	   	String allContentText = videoTitle;
    			String contentDesc = "";
    			int len = CONTENT_DESC_MAX_LENGTH;
    			if (StringUtils.isNotEmpty(allContentText)) {
    				if (allContentText.length() < len) {
    					len = allContentText.length();
    				}
    				contentDesc = allContentText.substring(0, len);
    			}
    			analysisResult.setTitle(videoTitle);
    			analysisResult.setContentDesc(contentDesc);
        	   	analysisResult.setShareMedia(analysisResult.SHARE_MEDIA_VIDEO);
       }  
		else if (guanChaArticleMain.size() != 0) {
			title=title.replace("- 今日头条 -手机光明网", "");
			guanChaArticleMain.select("div#yuanweninfo").remove();
			guanChaArticleMain.select("script").remove();
			guanChaArticleMain.select("a").attr("href", "javascript:void(0)");
			String str="../../../";
			String changeUrl = "http://m.gmw.cn/";
			if(url.indexOf("m.gmw.cn/")>0 ||(guanChaArticleMain.select("p")).html().indexOf(str)>0){
				htmlStr =guanChaArticleMain.html().replaceAll(str,changeUrl);

			}else if(url.indexOf("m2.people.cn/")>0||guanChaArticleMain.select("div").hasClass("slash")){
				title=guanChaArticleMain.select("h1").html();
				htmlStr = guanChaArticleMain.html();
			}else{
				htmlStr = guanChaArticleMain.html();
			}
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = guanChaArticleMain.text();
			String contentDesc = "";
			int len = CONTENT_DESC_MAX_LENGTH;
			if (StringUtils.isNotEmpty(allContentText)) {
				if (allContentText.length() < len) {
					len = allContentText.length();
				}
				contentDesc = allContentText.substring(0, len);
			}
			analysisResult.setTitle(title);
			analysisResult.setContentDesc(contentDesc);
			analysisResult
			.setShareMedia(analysisResult.SHARE_MEDIA_ARTICLE);
		}else if (huanQiuArticleMain.size() != 0) {
			huanQiuArticleMain.select("a").attr("href", "javascript:void(0)");
			htmlStr = huanQiuArticleMain.html();
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = huanQiuArticleMain.text();
			String contentDesc = "";
			int len = CONTENT_DESC_MAX_LENGTH;
			if (StringUtils.isNotEmpty(allContentText)) {
				if (allContentText.length() < len) {
					len = allContentText.length();
				}
				contentDesc = allContentText.substring(0, len);
			}
			analysisResult.setTitle(title);
			analysisResult.setContentDesc(contentDesc);
			analysisResult
			.setShareMedia(analysisResult.SHARE_MEDIA_ARTICLE);
		}else if (pic.size() != 0 && pic != null) {
				pic.select("img").removeAttr("src");
			Elements picTemp = new Elements();
			for (int n = 0; n < pic.size(); n++) {
				String imgSrc = pic.get(n).attr("data-src");
				if (StringUtils.isNotEmpty(imgSrc)) {
					pic.get(n).append("<img src='" + imgSrc + "'/>");
				} else {
					picTemp.add(pic.get(n));
				}
			}
			pic.removeAll(picTemp);
			Elements picAndText = body.select("script");
			Elements titleList = null;
			for (Element element : picAndText) {
				String script = element.data().toString();
				if (script.contains("gallery")) {
					String subScript = script.substring(
							script.indexOf("gallery: JSON.parse") + "gallery: JSON.parse'".length(),
							script.indexOf("siblingList") - 2);
					subScript = subScript.trim();
					subScript = subScript.substring(1, subScript.length() - 3);
					subScript = subScript.replaceAll("\\\\\\\\", "\\\\");
					subScript = subScript.replaceAll("\\\\\"", "'");
					Map<String, Object> mapSubScript = JsonUtil.toCollection(subScript,
							new TypeReference<Map<String, Object>>() {
							});
					int count = Integer.valueOf(mapSubScript.get("count").toString());
					String str=mapSubScript.get("sub_abstracts").toString();
					str = str.substring(1, str.length() - 1);
					String[] strs = str.split(",");
					@SuppressWarnings("unchecked")
					List<String> text = Arrays.asList(strs);
					picAndText=parse.select("body").html("");
					for (int i = 0; i < count; i++) {
						picAndText.append(pic.get(i).toString());
						picAndText.append(	"<p>" + text.get(i) + "</p>");
					}
					break;
				}
			}
			htmlStr = picAndText.html();
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = picAndText.text();
			String contentDesc = "";
			int len = CONTENT_DESC_MAX_LENGTH;
			if (StringUtils.isNotEmpty(allContentText)) {
				if (allContentText.length() < len) {
					len = allContentText.length();
				}
				contentDesc = allContentText.substring(0, len);
			}
			analysisResult.setTitle(title);
			analysisResult.setContentDesc(contentDesc);
			analysisResult
			.setShareMedia(analysisResult.SHARE_MEDIA_ARTICLE);
		}else if (youthpic.size() != 0) {
			htmlStr = youthpic.html();
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = youthpic.text();
			String contentDesc = "";
			int len = CONTENT_DESC_MAX_LENGTH;
			if (StringUtils.isNotEmpty(allContentText)) {
				if (allContentText.length() < len) {
					len = allContentText.length();
				}
				contentDesc = allContentText.substring(0, len);
			}
			analysisResult.setTitle(title);
			analysisResult.setContentDesc(contentDesc);
			analysisResult
			.setShareMedia(analysisResult.SHARE_MEDIA_ARTICLE);
		}else if (wuKongAnswer.size() != 0) {
			title = body.select("h1.question-name").text();
			wuKongAnswer.select("a").attr("href", "javascript:void(0)");
			htmlStr = wuKongAnswer.html();
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = wuKongAnswer.text();
			String contentDesc = "";
			int len = CONTENT_DESC_MAX_LENGTH;
			if (StringUtils.isNotEmpty(allContentText)) {
				if (allContentText.length() < len) {
					len = allContentText.length();
				}
				contentDesc = allContentText.substring(0, len);
			}
			analysisResult.setTitle(title);
			analysisResult.setContentDesc(contentDesc);
			analysisResult
			.setShareMedia(analysisResult.SHARE_MEDIA_ARTICLE);
		}else if(huiShengHuoArticleMain.size() != 0) {		
			huiShengHuoArticleMain.select("a").attr("href", "javascript:void(0)");
			htmlStr = huiShengHuoArticleMain.html();
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_SUCCESS);
			String allContentText = huiShengHuoArticleMain.text();
			String contentDesc = "";
			int len = CONTENT_DESC_MAX_LENGTH;
			if (StringUtils.isNotEmpty(allContentText)) {
				if (allContentText.length() < len) {
					len = allContentText.length();
				}
				contentDesc = allContentText.substring(0, len);
			}
			analysisResult.setTitle(title);
			analysisResult.setContentDesc(contentDesc);
			
			analysisResult
			.setShareMedia(analysisResult.SHARE_MEDIA_ARTICLE);
		} 
		else {
			analysisResult
					.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
		}
		analysisResult.setImgList(getImgListByHtml(body, LOAD_IMG_NUMBER,
				SourceTypeEnum.FROM_TOUTIAO));
		if (analysisResult.getImgList() != null
				&& analysisResult.getImgList().size() > 0) {
			map.put("pic", analysisResult.getImgList().get(0).getImgUrl());
		}
		analysisResult.setContent(htmlStr);
		analysisResult.setWordTotal(analysisResult.getContent().length());
		analysisResult.setExtendData(JsonUtil.toJson(map));

		return analysisResult;
	}
}
