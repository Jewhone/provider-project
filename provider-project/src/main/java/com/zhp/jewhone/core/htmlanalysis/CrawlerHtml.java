package com.zhp.jewhone.core.htmlanalysis;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.zhp.jewhone.core.constant.enums.SourceTypeEnum;
import com.zhp.jewhone.core.htmlanalysis.entity.AnalysisResult;
import com.zhp.jewhone.core.util.file.CMyFileUtils;
import com.zhp.jewhone.core.util.file.ImgProperty;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CrawlerHtml {
    /**
     * 加载网页里面的图片张数
     */
    protected static final int LOAD_IMG_NUMBER = 1;
    /**
     * 保存图片的最小宽高 300*300
     */
    private static final int SAVE_IMAGE_MIN_WIDTH = 300;
    private static final int SAVE_IMAGE_MIN_HEIGHT = 300;

    /**
     * 标题最长200
     */
    protected static final int CONTENT_DESC_MAX_LENGTH = 200;

    /**
     * 解析html页面内容
     *
     * @param url
     */
    public abstract AnalysisResult analysisByUrl(String url);

    /**
     * 加载url 获取html代码内容
     *
     * @param url
     */
    protected String getHtmlLoadUrl(String url) {
        String xmlHtml = "";
        WebClient webClient = null;
        try {
            webClient = new WebClient(BrowserVersion.CHROME); // 设置webClient的相关参数

            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.setJavaScriptTimeout(20000);// 设置JS执行的超时时间
            webClient.getOptions().setThrowExceptionOnScriptError(false);// 当JS执行出错的时候是否抛出异常
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);// 当HTTP的状态非200时是否抛出异常
            webClient.getOptions().setTimeout(30000);// 设置“浏览器”的请求超时时间
            webClient.getOptions().setCssEnabled(false);// 是否启用CSS
            webClient.getOptions().setJavaScriptEnabled(true); // 很重要，启用JS
            webClient.waitForBackgroundJavaScript(20000);// 设置JS后台等待执行时间
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());// 设置支持AJAX

            // 模拟浏览器打开一个目标网址
            HtmlPage rootPage = webClient.getPage(url);
            xmlHtml = rootPage.asXml();
        } catch (FailingHttpStatusCodeException | IOException e) {
            e.printStackTrace();
        } finally {
            if (webClient != null) {
                webClient.getCurrentWindow().getJobManager().removeAllJobs();
                webClient.close();
            }
            System.gc();
        }
        return xmlHtml;
    }

    /**
     * 根据html页面元素获取图片，没有图片则加上默认图片
     *
     * @param body
     * @param loadNumber 加载图片数量
     */
    protected List<ImgProperty> getImgListByHtml(Element body, int loadNumber,
                                                 SourceTypeEnum sourceTypeEnum) {
        List<ImgProperty> imgList = new ArrayList<>();// 图片内容List
        int i = 0;
        if (body != null) switch (sourceTypeEnum) {
            case FROM_NETEASE://网易
                Elements figureEl = body.select("figure.js-img.u-img-placeholder");
                Elements videos = body.select("div.video-holder");
                Elements sinaYulePic = body.select("article.art_box").select("section.art_pic_card.art_content").select("img");
                Elements sinaPic = body.select("article.art_box").select("img");
                Elements articleAndPic = body.select("div.n-words").select("p.detailPic").select("img");
                Elements ifeng = body.select("div.pic_word").select("article").select("img");
                Elements tuijianPic = body.select("div.n-words").select("img");
                Elements picAndText = body.select("img");
                Elements weixinImg = body.select("div.rich_media_content").select("img");
                if (figureEl != null && figureEl.size() > 0) {
                    i = getI(loadNumber, imgList,figureEl);
                } else if (videos != null && videos.size() > 0) {
                    String url = "http:" + videos.select("img").attr("src");
                    ImgProperty imageProperty = CMyFileUtils.getImgPropertyByUrl(url);
                    imgList.add(imageProperty);
                    i++;
                } else if (sinaYulePic != null && sinaYulePic.size() > 0) {
                    i = getI(loadNumber, imgList,sinaYulePic);
                } else if (sinaPic != null && sinaPic.size() > 0) {
                    i = getI(loadNumber, imgList,sinaPic);
                } else if (weixinImg != null && weixinImg.size() > 0) {
                    i = getI(loadNumber, imgList,weixinImg);
                } else if (articleAndPic != null && articleAndPic.size() > 0) {
                    i = getI(loadNumber, imgList,articleAndPic);
                } else if (ifeng != null && ifeng.size() > 0) {
                    i = getI(loadNumber, imgList,ifeng);
                } else if (tuijianPic != null && tuijianPic.size() > 0) {
                    i = getI(loadNumber, imgList,tuijianPic);
                } else if (picAndText != null && picAndText.size() > 0) {
                    i = getI(loadNumber, imgList,picAndText);
                }
                break;
            case FROM_TOUTIAO:
                // 今日头条
                Elements pngs = body.select("img[src~=http|https]");// 文章图片
                Elements gmw = body.select("p").select("img"); // 光明网封面图
                // Elements video = body.select("div.player-inner"); // 视频封面图
                if (pngs.size() != 0 && pngs != null) {
                    i = getI(loadNumber, imgList,pngs);
                } else if (gmw.size() != 0 && gmw != null) {
                    i = getI(loadNumber, imgList,gmw);
                }
                break;
            case FROM_EASYBOOK:
                Elements image = body.select("div.show-content").select("img");
                if (image != null && image.size() > 0) {
                    i = getI(loadNumber, imgList,image);
                }
                break;
            case FROM_TENCENT:
                Elements images = body.select("div.show-content").select("img");
                if (images != null && images.size() > 0) {
                    i = getI(loadNumber, imgList,images);
                }
                break;
            case FROM_SOHU:
                Elements textImage = body.select("div.at-cnt-main").select("img");
                if (textImage != null && textImage.size() > 0) {
                    i = getI(loadNumber, imgList,textImage);
                }
                break;
            case FROM_PAPER:
                Elements paperImage = body.select("div.news_content").select("div.news_part_father").select("img");
                if (paperImage != null && paperImage.size() > 0) {
                    i = getI(loadNumber, imgList,paperImage);
                }
                break;
            case FROM_ZHIHU:
                Elements imageText = body.select("div.RichContent-inner").select("img");
                Elements zhuanLanImg = body.select("div.RichText.PostIndex-content.av-paddingSide.av-card").select("img");
                if (imageText != null && imageText.size() > 0) {
                    i = getI(loadNumber, imgList,imageText);
                } else if (zhuanLanImg != null && zhuanLanImg.size() > 0) {
                    i = getI(loadNumber, imgList,zhuanLanImg);
                }
                break;
            case FROM_36KR:
                Elements krPic = body.select("script");
                String subScript = null;
                if (CollectionUtils.isEmpty(krPic)) {
                    for (Element element : krPic) {
                        String script = element.toString();
                        if (script.contains("\"content\":")) {
                            subScript = script.substring(script.indexOf("\"cover\":") + "\"cover\":".length(), script.indexOf("source_type") - 2);
                            subScript = subScript.trim();
                            subScript = subScript.substring(1, subScript.length() - 1);
                            break;
                        }
                    }
                }
                if (subScript != null && subScript.length() > 0) {
                    ImgProperty imageProperty = CMyFileUtils.getImgPropertyByUrl(subScript);
                    if (imageProperty == null || imageProperty.getWidth() < SAVE_IMAGE_MIN_WIDTH
                            || imageProperty.getHeight() < SAVE_IMAGE_MIN_HEIGHT) {// 这里获取封面图图片太小，过滤掉
                        break;
                    }
                    imgList.add(imageProperty);
                    i++;
                    if (i == loadNumber)
                        break;// 最多获取 loadNumber 张图
                }
                break;
            default:
                break;
        }
        /*if (i == 0) {// 一张封面都没有，则随机获取一张图片
            String userReceiveShareDefaultCoverKey = PropertiesUtil
                    .getValueByKey("user_receive_share_default_cover_key");
            String userReceiveShareDefaultCoverNumber = PropertiesUtil
                    .getValueByKey("user_receive_share_default_cover_number");
            Random ran = new Random();
            String number = userReceiveShareDefaultCoverNumber.split(",")[ran
                    .nextInt(userReceiveShareDefaultCoverNumber.split(",").length)];
            String imgSrc = Const.IMAGE_URL + userReceiveShareDefaultCoverKey + number;
            ImgProperty imageProperty = CMyFileUtils.getImgPropertyByUrl(imgSrc);
            imgList.add(imageProperty);
        }*/
        return imgList;
    }

    private int getI(int loadNumber, List<ImgProperty> imgList,Elements pics) {
        for (Element aPic : pics) {
            String imgSrc = aPic.attr("src");
            if (StringUtil.isNotEmpty(imgSrc) && !imgSrc.startsWith("http")) {
                imgSrc = "http:" + imgSrc;
            }
            ImgProperty imageProperty = CMyFileUtils.getImgPropertyByUrl(imgSrc);
            if (imageProperty == null) {
                break;
            }
            if (imageProperty.getWidth() < SAVE_IMAGE_MIN_WIDTH
                    || imageProperty.getHeight() < SAVE_IMAGE_MIN_HEIGHT) {// 这里获取封面图图片太小，过滤掉
                continue;
            }
            imgList.add(imageProperty);
            if (imgList.size() == loadNumber){
                break;// 最多获取 loadNumber 张图
            }
        }
        return imgList.size();
    }

}
