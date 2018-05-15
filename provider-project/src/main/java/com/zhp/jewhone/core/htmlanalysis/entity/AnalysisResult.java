package com.zhp.jewhone.core.htmlanalysis.entity;

import com.zhp.jewhone.core.util.file.ImgProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Title:  用户分享数据结果返回
 */
public class AnalysisResult implements Serializable{
	private static final long serialVersionUID = 4746610328386669317L;
	
	/**
	 * 0未分析1分析成功2分析失败
	 */
	public static final int ANALYSIS_STATE_NO = 0;
	public static final int ANALYSIS_STATE_SUCCESS = 1;
	public static final int ANALYSIS_STATE_FAIL = 2;
	/**
	 * 分析状态
	 */
	private int analysisState;
	
	/**
	 * 字数统计
	 */
	private int wordTotal;
	/**
	 * 分析后的标题
	 */
	private String title;
	/**
	 * 内容简介
	 */
	private String contentDesc;
	/**
	 * 分析后的内容
	 */
	private String content;
	/**
	 * 待分析的url
	 */
	private String rawUrl;
	/**
	 * 分析后的图片集合
	 */
	private List<ImgProperty> imgList;
	
	/**
	 * 0文章,1视频
	 */
	public static final int SHARE_MEDIA_ARTICLE = 0;
	public static final int SHARE_MEDIA_VIDEO = 1;
	/**
	 * 共享媒体
	 */
	private int shareMedia;
	
	/**
	 * 扩展数据
	 */
	private String extendData;
	
	/**
	 * 视频的播放地址当分享内容是视频的时候使用
	 */
	private String videoUrl;
	
	
	/**
	 * @return the videoUrl
	 */
	public String getVideoUrl() {
		return videoUrl;
	}
	/**
	 * @param videoUrl the videoUrl to set
	 */
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	/**
	 * @return the analysisState
	 */
	public int getAnalysisState() {
		return analysisState;
	}
	/**
	 * @param analysisState the analysisState to set
	 */
	public void setAnalysisState(int analysisState) {
		this.analysisState = analysisState;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the imgList
	 */
	public List<ImgProperty> getImgList() {
		return imgList;
	}
	/**
	 * @param imgList the imgList to set
	 */
	public void setImgList(List<ImgProperty> imgList) {
		this.imgList = imgList;
	}
	/**
	 * @return the contentDesc
	 */
	public String getContentDesc() {
		return contentDesc;
	}
	/**
	 * @param contentDesc the contentDesc to set
	 */
	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}
	/**
	 * @return the rawUrl
	 */
	public String getRawUrl() {
		return rawUrl;
	}
	/**
	 * @param rawUrl the rawUrl to set
	 */
	public void setRawUrl(String rawUrl) {
		this.rawUrl = rawUrl;
	}

	/**
	 * @return the extendData
	 */
	public String getExtendData() {
		return extendData;
	}
	/**
	 * @param extendData the extendData to set
	 */
	public void setExtendData(String extendData) {
		this.extendData = extendData;
	}
	/**
	 * @return the shareMedia
	 */
	public int getShareMedia() {
		return shareMedia;
	}
	/**
	 * @param shareMedia the shareMedia to set
	 */
	public void setShareMedia(int shareMedia) {
		this.shareMedia = shareMedia;
	}
	/**
	 * @return the wordTotal
	 */
	public int getWordTotal() {
		return wordTotal;
	}
	/**
	 * @param wordTotal the wordTotal to set
	 */
	public void setWordTotal(int wordTotal) {
		this.wordTotal = wordTotal;
	}

}
