package com.zhp.jewhone.core.htmlanalysis.biquge;

import com.zhp.jewhone.core.htmlanalysis.CrawlerHtml;
import com.zhp.jewhone.core.htmlanalysis.entity.AnalysisResult;
import com.zhp.jewhone.core.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BiqugeShareManager extends CrawlerHtml {

    @Override
    public AnalysisResult analysisByUrl(String url) {
        AnalysisResult analysisResult = new AnalysisResult();
        String xmlHtml = getHtmlLoadUrl(url);
        if (StringUtils.isEmpty(xmlHtml)) {
            analysisResult.setAnalysisState(AnalysisResult.ANALYSIS_STATE_FAIL);
        }
        Document document = Jsoup.parse(xmlHtml);
        Elements elements = document.select("");



        return null;
    }
}
