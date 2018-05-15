package com.zhp.jewhone.core.util.file;

import com.zhp.jewhone.core.constant.Const;
import com.zhp.jewhone.core.util.JsonUtil;
import com.zhp.jewhone.core.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Title:<BR>
 * Description: class CMyFile —— 文件操作通用函数的定义和实现
 * 
 * @version 1.0
 */
public class CMyFileUtils {
	private static org.apache.log4j.Logger m_oLogger = org.apache.log4j.Logger
			.getLogger(CMyFileUtils.class);

	/**
	 * 构造函数
	 */
	public CMyFileUtils() {

	}

	// ==================================================================
	// 文件名称分解的几个工具函数
	/**
	 * 获取应用根路径
	 */
	public static String getAppPath(String _sPath, String _sAppName) {
		int nPos = _sPath.indexOf(_sAppName);
		return _sPath.substring(0, (nPos + _sAppName.length()));
	}

	/**
	 * 检查指定文件是否存在
	 * 
	 * @param _sPathFileName
	 *            文件名称(含路径）
	 * @return 若存在，则返回true；否则，返回false
	 */
	public static boolean fileExists(String _sPathFileName) {
		File file = new File(_sPathFileName);
		return file.exists();
	}

	/**
	 * 检查指定文件的路径是否存在
	 * 
	 * @param _sPathFileName
	 *            文件名称(含路径）
	 * @return 若存在，则返回true；否则，返回false
	 */
	public static boolean pathExists(String _sPathFileName) {
		String sPath = extractFilePath(_sPathFileName);
		return fileExists(sPath);
	}

	/**
	 * 从文件的完整路径名（路径+文件名）中提取文件名(包含扩展名) <br>
	 * 如：d:\path\file.ext --> file.ext
	 * 
	 * @param _sFilePathName
	 * @return
	 */
	public static String extractFileName(String _sFilePathName) {
		return extractFileName(_sFilePathName, File.separator);
	}

	public static String extractRelationAppPath(String _sFileName,
			String _sAppName) {
		int nPos = _sFileName.toUpperCase().indexOf(_sAppName.toUpperCase());
		if (nPos == -1)
			return "";

		String sTemp = CMyFileUtils.extractFileName(_sFileName, File.separator);
		int nEndPos = _sFileName.indexOf(sTemp);

		return _sFileName.substring((nPos + _sAppName.length()), nEndPos - 1);
	}

	/**
	 * 从文件的完整路径名（路径+文件名）中提取文件名(包含扩展名) <br>
	 * 如：d:\path\file.ext --> file.ext
	 * 
	 * @param _sFilePathName
	 *            全文件路径名
	 * @param _sFileSeparator
	 *            文件分隔符
	 * @return
	 */
	public static String extractFileName(String _sFilePathName,
			String _sFileSeparator) {
		int nPos = -1;
		if (_sFileSeparator == null) {
			nPos = _sFilePathName.lastIndexOf(File.separatorChar);
			if (nPos < 0) {
				nPos = _sFilePathName
						.lastIndexOf(File.separatorChar == '/' ? '\\' : '/');
			}
		} else {
			nPos = _sFilePathName.lastIndexOf(_sFileSeparator);
		}

		if (nPos < 0) {
			return _sFilePathName;
		}

		return _sFilePathName.substring(nPos + 1);
	}

	public static String extractFilePreName(String _sFilePathName) {
		String fileName = extractFileName(_sFilePathName, File.separator);
		int pos = fileName.lastIndexOf(".");
		if (pos > 0)
			return fileName.substring(0, pos);
		return fileName;
	}

	// 020513
	/**
	 * 从EB路径地址中提取: 文件名
	 * 
	 * @param _sFilePathName
	 * @return
	 */
	public static String extractHttpFileName(String _sFilePathName) {
		int nPos = _sFilePathName.lastIndexOf("/");
		return _sFilePathName.substring(nPos + 1);
	}

	/**
	 * 从文件的完整路径名（路径+文件名）中提取:主文件名（不包括路径和扩展名）
	 * 
	 * @param _sFilePathName
	 * @return
	 */
	public static String extractMainFileName(String _sFilePathName) {
		String sFileMame = extractFileName(_sFilePathName);
		int nPos = sFileMame.lastIndexOf('.');
		if (nPos > 0) {
			return sFileMame.substring(0, nPos);
		}
		return sFileMame;
	}

	/**
	 * 排除文件的扩展名,只保留路径(如果存在)和主文件名
	 * 
	 * @param sFileMame
	 * @return
	 */
	public static String excludeFileExt(String sFileMame) {
		int nPos = sFileMame.lastIndexOf('.');
		if (nPos > 0) {
			return sFileMame.substring(0, nPos);
		}
		return sFileMame;
	}

	/**
	 * 从文件的完整路径名（路径+文件名）中提取: 文件扩展名
	 * 
	 * @param _sFilePathName
	 * @return
	 */
	public static String extractFileExt(String _sFilePathName) {
		int nPos = _sFilePathName.lastIndexOf('.');
		return (nPos >= 0 ? _sFilePathName.substring(nPos + 1) : "");
	}

	/**
	 * 从文件的完整路径名（路径+文件名）中提取 路径（包括：Drive+Directroy )
	 * 
	 * @param _sFilePathName
	 * @return
	 */
	public static String extractFilePath(String _sFilePathName) {

		StringUtils.replaceStr(_sFilePathName, "\\", "/");
		int nPos = _sFilePathName.lastIndexOf('/');
		if (nPos < 0) {
			nPos = _sFilePathName.lastIndexOf('\\');
		}
		return (nPos >= 0 ? _sFilePathName.substring(0, nPos + 1) : "");
	}

	/**
	 * 将文件/路径名称转化为绝对路径名
	 * 
	 * @param _sFilePathName
	 *            文件名或路径名
	 * @return
	 */
	public static String toAbsolutePathName(String _sFilePathName) {
		File file = new File(_sFilePathName);
		return file.getAbsolutePath();
	}

	/**
	 * 从文件的完整路径名（路径+文件名）中提取文件所在驱动器 <br>
	 * 注意：区分两种类型的文件名表示 <br>
	 * [1] d:\path\filename.ext --> return "d:" <br>
	 * [2] \\host\shareDrive\shareDir\filename.ext --> return
	 * "\\host\shareDrive"
	 * 
	 * @param _sFilePathName
	 * @return
	 */
	public static String extractFileDrive(String _sFilePathName) {
		int nPos;
		int nLen = _sFilePathName.length();

		// 检查是否为 "d:\path\filename.ext" 形式
		if ((nLen > 2) && (_sFilePathName.charAt(1) == ':'))
			return _sFilePathName.substring(0, 2);

		// 检查是否为 "\\host\shareDrive\shareDir\filename.ext" 形式
		if ((nLen > 2) && (_sFilePathName.charAt(0) == File.separatorChar)
				&& (_sFilePathName.charAt(1) == File.separatorChar)) {
			nPos = _sFilePathName.indexOf(File.separatorChar, 2);
			if (nPos >= 0)
				nPos = _sFilePathName.indexOf(File.separatorChar, nPos + 1);
			return (nPos >= 0 ? _sFilePathName.substring(0, nPos)
					: _sFilePathName);
		}

		return "";
	}// END:extractFileDrive

	/**
	 * 删除指定的文件
	 * 
	 * @param _sFilePathName
	 *            指定的文件名
	 * @return
	 */
	public static boolean deleteFile(String _sFilePathName) {
		File file = new File(_sFilePathName);
		return file.exists() ? file.delete() : false;
	}

	// =======================================================================
	// 目录操作函数

	/**
	 * 创建目录
	 * 
	 * @param _sDir
	 *            目录名称
	 * @param _bCreateParentDir
	 *            如果父目录不存在，是否创建父目录
	 * @return
	 */
	public static boolean makeDir(String _sDir, boolean _bCreateParentDir) {
		boolean zResult = false;
		File file = new File(_sDir);
		if (_bCreateParentDir)
			zResult = file.mkdirs(); // 如果父目录不存在，则创建所有必需的父目录
		else
			zResult = file.mkdir(); // 如果父目录不存在，不做处理
		if (!zResult)
			zResult = file.exists();
		return zResult;
	}

	/**
	 * 删除指定的目录下所有的文件 注意：若文件或目录正在使用，删除操作将失败。
	 * 
	 * @param _sDir
	 *            目录名
	 * @param _bDeleteChildren
	 *            是否删除其子目录或子文件（可省略，默认不删除）
	 * @return <code>true</code> if the directory exists and has been deleted
	 *         successfully.
	 * @deprecated to use deleteDir(String _sPath) or deleteDir(File _path)
	 *             instead.
	 */
	public static boolean deleteDir(String _sDir, boolean _bDeleteChildren) {
		File file = new File(_sDir);
		if (!file.exists())
			return false;

		if (_bDeleteChildren) { // 删除子目录及其中文件
			File[] files = file.listFiles(); // 取目录中文件和子目录列表
			File aFile;
			for (int i = 0; i < files.length; i++) {
				aFile = files[i];
				if (aFile.isDirectory()) {
					deleteDir(aFile);
				} else {
					aFile.delete();
				}
			}// end for
		}// end if
		return file.delete(); // 删除该目录
	}// END:deleteDir

	/**
	 * Deletes a file path, and all the files and subdirectories in this path
	 * will also be deleted.
	 * 
	 * @param _sPath
	 *            the specified path.
	 * @return <code>true</code> if the path exists and has been deleted
	 *         successfully; <code>false</code> othewise.
	 */
	public static boolean deleteDir(String _sPath) {
		File path = new File(_sPath);
		return deleteDir(path);
	}

	/**
	 * Deletes a file path, and all the files and subdirectories in this path
	 * will also be deleted.
	 * 
	 * @param _path
	 *            the specified path.
	 * @return <code>true</code> if the path exists and has been deleted
	 *         successfully; <code>false</code> othewise.
	 */
	public static boolean deleteDir(File _path) {
		// 1. to check whether the path exists
		if (!_path.exists()) {
			return false;
		}

		// 2. to delete the files in the path
		if (_path.isDirectory()) {
			// if _path is not a dir,files=null
			File[] files = _path.listFiles();
			File aFile;
			for (int i = 0; i < files.length; i++) {
				aFile = files[i];
				if (aFile.isDirectory()) {
					deleteDir(aFile);
				} else {
					aFile.delete();
				}
			}// endfor
		}

		// 3. to delete the path self
		return _path.delete();
	}

	/**
	 * 获取某一路径下的特殊文件
	 * 
	 * @param dir
	 *            路径名称
	 * @param extendName
	 *            文件扩展名，"."可以包含也可以不包含
	 * @return
	 */
	public static File[] listFiles(String _dir, String _extendName) {
		File fDir = new File(_dir);
		if (_extendName.charAt(0) != '.')
			_extendName = "." + _extendName;
		File[] Files = fDir.listFiles(new CMyFilenameFilter(_extendName));
		return Files;
	}

	/**
	 * 获取某一路径下的子文件夹
	 * 
	 * @param _dir
	 *            路径名称
	 * @return 子文件夹对象数组
	 */
	public static File[] listSubDirectories(String _dir) {
		File fDir = new File(_dir);
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};

		File[] files = fDir.listFiles(fileFilter);
		return files;
	}

	// =======================================================================
	// 文件读写操作函数

	// 读取文件的内容，返回字符串类型的文件内容
	/**
	 * 读取文件的内容，返回字符串类型的文件内容
	 * 
	 * @param _sFileName
	 *            文件名
	 * @return
	 * @throws Exception
	 */
	public static String readFile(String _sFileName) throws Exception {
		return readFile(_sFileName, Const.FILE_WRITING_ENCODING);
	}// END: readFile()

	/**
	 * 读取文件的内容，返回字符串类型的文件内容
	 * 
	 * @param _sFileName
	 *            文件名
	 * @param _sEncoding
	 *            以指定的字符编码读取文件内容,默认为"UTF-8"
	 * @return
	 * @throws Exception
	 */
	public static String readFile(String _sFileName, String _sEncoding)
			throws Exception {
		FileReader fileReader = null;

		StringBuffer buffContent = null;
		String sLine;

		// 增加异常是对于资源的释放
		FileInputStream fis = null;
		BufferedReader buffReader = null;
		if (_sEncoding == null) {
			_sEncoding = "UTF-8";
		}

		try {
			// 增加读取文件的字符编码StringUtil.FILE_WRITING_ENCODING
			fis = new FileInputStream(_sFileName);
			buffReader = new BufferedReader(new InputStreamReader(fis,
					_sEncoding));
			// 依次读取文件中的内容
			while ((sLine = buffReader.readLine()) != null) {
				if (buffContent == null) {
					buffContent = new StringBuffer();
				} else {
					buffContent.append("\n");
				}
				buffContent.append(sLine);
			}// end while
				// 关闭打开的字符流和文件流

			// 返回文件的内容
			return (buffContent == null ? "" : buffContent.toString());
		} catch (FileNotFoundException ex) {
			throw new Exception("要读取得文件没有找到(CMyFile.readFile)", ex);
		} catch (IOException ex) {
			throw new Exception("读文件时错误(CMyFile.readFile)", ex);
		} finally {
			// 增加异常时资源的释放
			try {
				if (fileReader != null)
					fileReader.close();
				if (buffReader != null)
					buffReader.close();
				if (fis != null)
					fis.close();
			} catch (Exception ex) {
			}

		}// end try
	}

	public static byte[] readBytesFromFile(String _sFileName) throws Exception {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(_sFileName);
			byte[] buffer = new byte[1024];
			bos = new ByteArrayOutputStream(2048);
			int nLen = 0;
			while ((nLen = fis.read(buffer)) > 0) {
				bos.write(buffer, 0, nLen);
			}
			return bos.toByteArray();
		} catch (Exception e) {
			throw new Exception("读取文件[" + _sFileName + "]失败！");
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
	}

	// 以指定内容_sFileContent生成新的文件_sFileName
	/**
	 * 以指定内容_sFileContent生成新的文件_sFileName
	 * 
	 * @param _sFileName
	 *            文件名
	 * @param _sFileContent
	 *            指定的内容
	 * @return
	 * @throws Exception
	 */
	public static boolean writeFile(String _sFileName, String _sFileContent)
			throws Exception {
		return writeFile(_sFileName, _sFileContent, Const.FILE_WRITING_ENCODING);
	}// END: writeFile()

	/**
	 * 以指定内容_sFileContent生成新的文件_sFileName
	 * 
	 * @param _sFileName
	 *            文件名
	 * @param _sFileContent
	 *            指定的内容
	 * @return
	 * @throws Exception
	 */
	public static boolean writeFile(String _sFileName, String _sFileContent,
			String _encoding) throws Exception {
		return writeFile(_sFileName, _sFileContent, _encoding, false);
	}// END: writeFile()

	public static boolean writeFile(String _sFileName, String _sFileContent,
			String _sFileEncoding, boolean _bWriteUnicodeFlag) throws Exception {
		// 1.创建目录
		String sPath = extractFilePath(_sFileName);
		if (!CMyFileUtils.pathExists(sPath)) {
			CMyFileUtils.makeDir(sPath, true);
		}
		String sFileEncoding = StringUtils.showNull(_sFileEncoding,
				Const.FILE_WRITING_ENCODING);

		boolean bRet = false;
		// 加入异常的处理
		FileOutputStream fos = null;
		Writer outWriter = null;
		try {
			fos = new FileOutputStream(_sFileName);
			outWriter = new OutputStreamWriter(fos, sFileEncoding); // 指定编码方式
			if (_bWriteUnicodeFlag)
				outWriter.write(0xFEFF);
			outWriter.write(_sFileContent); // 写操作

			bRet = true;
		} catch (Exception ex) {
			m_oLogger.error("写文件[" + _sFileName + "]发生异常");
			throw new Exception("写文件错误(CMyFile.writeFile)");
		} finally {
			try {
				if (outWriter != null) {
					outWriter.flush();
					outWriter.close();
				}
				if (fos != null)
					fos.close();
			} catch (Exception ex) {
			}
		}
		return bRet;
	}// END: writeFile()

	// 把指定的内容_sAddContent追加到文件_sFileName中
	/**
	 * 把指定的内容_sAddContent追加到文件_sFileName中
	 * 
	 * @param _sFileName
	 *            文件名
	 * @param _sAddContent
	 *            追加的内容
	 * @return
	 * @throws Exception
	 */
	public static boolean appendFile(String _sFileName, String _sAddContent)
			throws Exception {
		boolean bResult = false;
		// 释放资源
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(_sFileName, "rw");
			raf.seek(raf.length());
			raf.writeBytes(_sAddContent);
			bResult = true;
		} catch (Exception ex) {
			throw new Exception("向文件追加内容时发生异常(CMyFile.appendFile)");
		} finally {
			// 释放资源
			try {
				if (raf != null)
					raf.close();
			} catch (Exception ex) {
			}
		}
		return bResult;
	}// END: appendFile()

	public static boolean appendFileWithWriter(String _sFileName,
			String _sAddContent) throws Exception {
		FileOutputStream fOuts = null;
		BufferedWriter writer = null;
		try {
			fOuts = new FileOutputStream(_sFileName, true);
			writer = new BufferedWriter(new OutputStreamWriter(fOuts, "UTF-8"));
			writer.write(_sAddContent);
			writer.flush();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (fOuts != null) {
				fOuts.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
		return true;
	}

	/**
	 * 移动文件
	 * 
	 * @param _sSrcFile
	 *            待移动的文件
	 * @param _sDstFile
	 *            目标文件
	 * @return
	 * @throws Exception
	 */
	public static boolean moveFile(String _sSrcFile, String _sDstFile)
			throws Exception {
		return moveFile(_sSrcFile, _sDstFile, true);
	}

	/**
	 * 移动文件
	 * 
	 * @param _sSrcFile
	 *            待移动的文件
	 * @param _sDstFile
	 *            目标文件
	 * @param _bMakeDirIfNotExists
	 *            若目标路径不存在，是否创建;可缺省,默认值为true.
	 * @return
	 * @throws Exception
	 */
	public static boolean moveFile(String _sSrcFile, String _sDstFile,
			boolean _bMakeDirIfNotExists) throws Exception {
		// 1.拷贝
		copyFile(_sSrcFile, _sDstFile, _bMakeDirIfNotExists);
		// 2.删除
		deleteFile(_sSrcFile);
		return false;
	}

	/**
	 * 复制文件
	 * 
	 * @param _sSrcFile
	 *            源文件（必须包含路径）
	 * @param _sDstFile
	 *            目标文件（必须包含路径）
	 * @param
	 *            若目标路径不存在，是否创建;可缺省,默认值为true.
	 * @return 若文件复制成功，则返回true；否则，返回false.
	 * @throws Exception
	 *             源文件不存在,或目标文件所在目录不存在,或文件复制失败,会抛出异常.
	 */
	public static boolean copyFile(String _sSrcFile, String _sDstFile)
			throws Exception {
		return copyFile(_sSrcFile, _sDstFile, true);
	}

	public static boolean copyFile(String _sSrcFile, String _sDstFile,
			boolean _bMakeDirIfNotExists) throws Exception {
		return copyFile(_sSrcFile, _sDstFile, _bMakeDirIfNotExists, false);
	}// END: copyFile()

	public static boolean copyFile(String _sSrcFile, String _sDstFile,
			boolean _bMakeDirIfNotExists, boolean preserveFileDate)
			throws Exception {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(_sSrcFile); // 若文件不存在,会抛出异常

			// 如果目录不存在，则创建目录
			try {
				fos = new FileOutputStream(_sDstFile);
			} catch (FileNotFoundException ex) {
				if (_bMakeDirIfNotExists) { // 自动创建目录
					if (!CMyFileUtils.makeDir(CMyFileUtils.extractFilePath(_sDstFile),
							true)) {
						throw new Exception("为目标文件[" + _sDstFile + "]创建目录失败！");
					}
					fos = new FileOutputStream(_sDstFile);
				} else {
					throw new Exception("指定目标文件[" + _sDstFile + "]所在目录不存在！");
				}
			}// end try

			byte buffer[] = new byte[4096];
			int bytes;
			while ((bytes = fis.read(buffer, 0, 4096)) > 0) {
				fos.write(buffer, 0, bytes);
			}// end while
		} catch (FileNotFoundException ex) {
			throw new Exception("要复制的原文件没有发现(CMyFile.copyFile)");
		} catch (IOException ex) {
			throw new Exception("复制文件时发生异常(CMyFile.copyFile)");
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (Exception ex) {
				}
			if (fis != null)
				try {
					fis.close();
				} catch (Exception ex) {
				}
		}// end try
		if (preserveFileDate) {
			new File(_sDstFile).setLastModified(new File(_sSrcFile)
					.lastModified());
		}
		return true;
	}// END: copyFile()

	/**
	 * map the resource related path to full real path
	 * 
	 * @param _resource
	 *            related path of resource
	 * @return the full real path
	 * @throws Exception
	 *             if encounter errors
	 */
	public static String mapResouceFullPath(String _resource) throws Exception {
		// URL url = CMyFile.class.getResource(_resource);
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource(_resource);
		if (url == null) {
			throw new Exception("文件[" + _resource + "]没有找到！");
		}

		// else
		String sPath = null;
		try {
			sPath = url.getFile();
			if (sPath.indexOf('%') >= 0) {
				// 2007-8-23 13:19:30 加上enc参数，以免调用时抛空指针异常
				// sPath = URLDecoder.decode(url.getFile(), null);
				String enc = System.getProperty("file.encoding", "GBK");
				sPath = URLDecoder.decode(url.getFile(), enc);

			}
		} catch (Exception ex) {
			throw new Exception("文件[" + url.getFile() + "]转换失败！");
		}
		return sPath;
	}
	@SuppressWarnings("all")
	public static String mapResouceFullPath(String _resource, Class _currClass)
			throws Exception {
		URL url = _currClass.getResource(_resource);
		if (url == null) {
			throw new Exception("文件[" + _resource + "]没有找到！");
		}

		String sPath = null;
		try {
			sPath = url.getFile();
			if (sPath.indexOf('%') >= 0) {
				// @2007-8-23 13:19:30 加上enc参数，以免调用时抛空指针异常
				// sPath = URLDecoder.decode(url.getFile(), null);
				String enc = System.getProperty("file.encoding", "GBK");
				sPath = URLDecoder.decode(url.getFile(), enc);

			}
		} catch (Exception ex) {
			throw new Exception("文件[" + url.getFile() + "]转换失败！");
		}
		return sPath;
	}

	/**
	 * 返回一个临时的文件名(通常用作目录)
	 * 
	 * @return 临时文件名
	 */
	public static String getTempFileName() {
		String sTime = new Timestamp(System.currentTimeMillis()).toString();
		sTime = sTime.substring(0, 19).replace('-', '.').replace(' ', '_')
				.replace(':', '.');

		StringBuffer sb = new StringBuffer(256);

		sb.append("__deleted_");
		sb.append(sTime);
		sb.append("__");

		return sb.toString();
	}

	/**
	 * 获取文件总行数
	 * 
	 * @param sFilePath
	 * @param encode
	 * @param bIgnoreBlankLine
	 * @return
	 * @throws Exception
	 */
	public static int getLineCounts(String sFilePath, String encode,
			boolean bIgnoreBlankLine) throws Exception {
		FileInputStream fIns = null;
		BufferedReader reader = null;
		try {
			fIns = new FileInputStream(sFilePath);
			reader = new BufferedReader(new InputStreamReader(fIns, encode));
			String line;
			int lineCounts = 0;
			while ((line = reader.readLine()) != null) {
				if (bIgnoreBlankLine && line.trim().length() == 0) {
					continue;
				}
				lineCounts++;
			}
			return lineCounts;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (fIns != null) {
				fIns.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * 是否符合文件名
	 * 
	 * @param extentName
	 *            扩展名
	 * @param containExtName
	 *            包含的扩展名
	 * @return
	 */
	public static boolean isExtentName(String extentName, String containExtName) {
		String picExt[] = containExtName.split(",");
		boolean bo = false;
		for (int i = 0; i < picExt.length; i++) {
			if (picExt[i].equals(extentName.toLowerCase())) {
				bo = true;
				i += picExt.length;
			}
		}
		return bo;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileExtentName(String fileName) {
		String extentName = "";
		if (StringUtils.isNotEmpty(fileName)) {
			extentName = fileName.substring(fileName.lastIndexOf("."));
		}
		return extentName;
	}

	/**
	 * 获取制定文件名的文件
	 * 
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static File getFileByPath(String filePath, String fileName) {
		File folder = new File(filePath);
		return getFileByFolder(folder, fileName);
	}

	/**
	 * 获取制定文件名的文件
	 * 
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static File getFileByFolder(File folder, String fileName) {
		File files[] = folder.listFiles(new FileNameSelector(fileName));
		File file = null;
		if (files != null && files.length > 0) {
			file = files[0];
		}
		return file;
	}
	
	/**
	 * 遍历文件夹下所有文件  包括子文件夹
	 * @param path
	 */
	public static List<File> traverseFolder2(String path) {
        File file = new File(path);
        List<File> filess=new ArrayList<File>();
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return null;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
//                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder2(file2.getAbsolutePath());
                    } else {
//                        System.out.println("文件:" + file2.getAbsolutePath());
                        filess.add(file2);
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
		return filess;
    }
	
	/**
	 * 获取公钥字符串
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKeyStringFromFile(String filePath) {
		StringBuilder pubKeyString = new StringBuilder();
		FileInputStream in = null;
		InputStreamReader inReader = null;
		BufferedReader bf = null;
		try {
			in = new FileInputStream(filePath);
			inReader = new InputStreamReader(in, "UTF-8");
			bf = new BufferedReader(inReader);
		
			String line;
				do {
					line = bf.readLine();
					if (line != null) {
						if (pubKeyString.length() != 0) {
							pubKeyString.append("\n");
						}
						pubKeyString.append(line);
					}
				} while (line != null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				inReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pubKeyString.toString();
	}
	/**
	 * 根据图片地址获取图片属性
	 * @param url
	 */
	 public static ImgProperty getImgPropertyByUrl(String url) {
	    	InputStream inputStream = null;
	    	BufferedImage sourceImg = null;
	    	ImgProperty image = null;
			try {
				image = new ImgProperty();
				inputStream = new URL(url).openStream();
				sourceImg =ImageIO.read(inputStream);   
		        image.setHeight(sourceImg.getHeight());
		        image.setWidth(sourceImg.getWidth());
		        image.setImgUrl(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (sourceImg != null) {
					sourceImg.flush();
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return image;
	 }
	 public static void main(String[] args) {
		 ImgProperty imp = getImgPropertyByUrl("https://myimage.immouo.com/talk/pic/ec8b7402-9991-47de-a298-ad76895d1ef3.jpg");
		 System.out.println(JsonUtil.toJson(imp));
	 }
	 
	 /**
	  * 把指定的内容_sAddContent追加到文件_sFileName中（换行)
	  * @param _sFileName
	  * @param _sAddContent
	  * @return
	  * @throws Exception
	  */
	 public static boolean appendFileWithWriterNewLine(String _sFileName,
				String _sAddContent) throws Exception {
			FileOutputStream fOuts = null;
			BufferedWriter writer = null;
			try {
				fOuts = new FileOutputStream(_sFileName, true);
				writer = new BufferedWriter(new OutputStreamWriter(fOuts, "UTF-8"));
				writer.write("\r\n"+_sAddContent);
				writer.flush();
			} catch (Exception ex) {
				throw ex;
			} finally {
				if (fOuts != null) {
					fOuts.close();
				}
				if (writer != null) {
					writer.close();
				}
			}
			return true;
		}
}