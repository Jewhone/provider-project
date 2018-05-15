package com.zhp.jewhone.core.htmlanalysis.service.impl;

import com.zhp.jewhone.core.constant.enums.SourceTypeEnum;
import com.zhp.jewhone.core.htmlanalysis.entity.H5ShareResult;
import com.zhp.jewhone.core.htmlanalysis.service.AnalysisService;
import com.zhp.jewhone.core.htmlanalysis.toutiao.ToutiaoShareManager;
import com.zhp.jewhone.core.util.JsonUtil;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;

@Service
public class AnalysisServiceImpl implements AnalysisService {
	private static final Logger LOG = Logger.getLogger(AnalysisServiceImpl.class);

	@Override
	public H5ShareResult analysisToutiaoVideoUrl(String videoId,
												 SourceTypeEnum SourceTypeEnum, H5ShareResult h5ShareResult) {
		@SuppressWarnings("unused")
		String videoUrl = "";
		// H5ShareResult diaryH5ShareResult = JsonUtil.toObject(
		// diaryMapper.selectExtendDataById(id), H5ShareResult.class);
		switch (SourceTypeEnum) {
		case FROM_TOUTIAO:
			String toutiaoUrl = "http://ib.365yg.com/video/urls/v/1/toutiao/mp4/";
			String classPath = ToutiaoShareManager.class.getResource("/").toString();
			String jsPath = classPath + "toutiao/videoUrl.js";
			jsPath = jsPath.substring(5);
			String base64Url = "";
			toutiaoUrl = toutiaoUrl + videoId; // 重构url
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
			Bindings bind = engine.createBindings();
			bind.put("factor", 0);
			engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
			try {
				engine.eval(new FileReader(jsPath));
				Object js_result = engine.eval("crc32(' + " + toutiaoUrl + "')");
				base64Url = js_result.toString();
				Document doc = Jsoup.connect(base64Url).ignoreContentType(true).get();
				Element newBody = doc.body();
				String json = newBody.text();
				// json中main_url为视频地址,通过base64解码得到视频链接
				String base64 = json.substring(
						json.indexOf("main_url\":\"") + "main_url\":\"".length(),
						json.indexOf("\",\"backup_url_1"));// 视频地址
				byte[] bytes = Base64Utils.decodeFromString(base64);
				// 视频链接
				 videoUrl = new String(bytes);
				return h5ShareResult;
			} catch (Exception e) {
				LOG.debug(e.getMessage());
			}
		case FROM_NETEASE:
			StringBuilder sb = new StringBuilder("<video src='");
			h5ShareResult.setExtend(JsonUtil.toMap(h5ShareResult.getExtendData()));
			sb.append(h5ShareResult.getExtend().get("video"));
			sb.append("' poster='");
			sb.append(h5ShareResult.getExtend().get("pic"));
			sb.append("' x5-video-player-type='h5' x5-video-player-fullscreen='false' x-webkit-airplay='true' webkit-playsinline='true' controls='controls' id='video'></video>");
			h5ShareResult.setExtendData(sb.toString());
			return h5ShareResult;
//		case FROM_SINA:
//			StringBuilder sbs = new StringBuilder("<video src='");
//			diaryH5ShareResult.setExtend(JsonUtil.toMap(diaryH5ShareResult.getExtendData()));
//			sbs.append(diaryH5ShareResult.getExtend().get("video"));
//			sbs.append("' poster='");
//			sbs.append(diaryH5ShareResult.getExtend().get("pic"));
//			sbs.append("' x5-video-player-type='h5' x5-video-player-fullscreen='false' x-webkit-airplay='true' webkit-playsinline='true' controls='controls' id='video'></video>");
//			diaryH5ShareResult.setExtendData(sbs.toString());
//			return diaryH5ShareResult;
		default:
			return h5ShareResult;
		}
	}
}
