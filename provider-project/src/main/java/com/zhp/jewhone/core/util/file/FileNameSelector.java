package com.zhp.jewhone.core.util.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 文件过滤类
 */
public class FileNameSelector implements FilenameFilter {
	private String fileName = "";

	public FileNameSelector(String fileExtensionNoDot) {
		fileName = fileExtensionNoDot;
	}

	@Override
	public boolean accept(File dir, String name) {
		return name.toUpperCase().equals(fileName.toUpperCase());
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
