package com.zhp.jewhone.core.htmlanalysis;

import com.zhp.jewhone.core.htmlanalysis.baidu.BaiduShareManager;
import com.zhp.jewhone.core.htmlanalysis.easybook.EasybookShareManager;
import com.zhp.jewhone.core.htmlanalysis.entity.AnalysisResult;
import com.zhp.jewhone.core.htmlanalysis.huxiu.HuxiuShareManager;
import com.zhp.jewhone.core.htmlanalysis.ifeng.IfengShareManager;
import com.zhp.jewhone.core.htmlanalysis.krypton.KryptonShareManager;
import com.zhp.jewhone.core.htmlanalysis.neteasy.NeteaseShareManager;
import com.zhp.jewhone.core.htmlanalysis.paper.PaperShareManager;
import com.zhp.jewhone.core.htmlanalysis.qingmang.QingmangShareManager;
import com.zhp.jewhone.core.htmlanalysis.sina.SinaShareManager;
import com.zhp.jewhone.core.htmlanalysis.sina.SinaVideoShareManager;
import com.zhp.jewhone.core.htmlanalysis.sohu.SohuShareManager;
import com.zhp.jewhone.core.htmlanalysis.tencent.TencentShareManager;
import com.zhp.jewhone.core.htmlanalysis.toutiao.ToutiaoShareManager;
import com.zhp.jewhone.core.htmlanalysis.wechat.WeChatShareManager;
import com.zhp.jewhone.core.htmlanalysis.zhihu.ZhihuShareManager;


public class CrawlerAnalysis {

    public static void main(String[] args) throws Exception {








    }





    /**
     * 分析网页
     *
     * @param str
     * @param type
     * @return AnalysisResult
     */
    public AnalysisResult analysisHtml(Integer type, String str) {
        AnalysisResult analysisResult = new AnalysisResult();
        CrawlerHtml crawlerHtml;
        switch (type) {
            case 2:
                crawlerHtml = new NeteaseShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 3:
                crawlerHtml = new ToutiaoShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 4:
                crawlerHtml = new EasybookShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 5:
                crawlerHtml = new ZhihuShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 6:
                crawlerHtml = new BaiduShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 7:
                crawlerHtml = new KryptonShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 8:
                crawlerHtml = new HuxiuShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 9:
                crawlerHtml = new SinaShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 10:
                crawlerHtml = new WeChatShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 11:
                crawlerHtml = new SinaVideoShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 12:
                crawlerHtml = new PaperShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 16:
                crawlerHtml = new IfengShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 17:
                crawlerHtml = new TencentShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 18:
                crawlerHtml = new SohuShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            case 20:
                crawlerHtml = new QingmangShareManager();
                analysisResult = crawlerHtml.analysisByUrl(str);
                break;
            default:
                break;
        }
        return analysisResult;
    }

}
