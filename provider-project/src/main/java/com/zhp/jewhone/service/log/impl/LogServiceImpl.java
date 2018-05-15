package com.zhp.jewhone.service.log.impl;

import com.zhp.jewhone.core.util.DateTimeUtil;
import com.zhp.jewhone.core.util.file.CMyFileUtils;
import com.zhp.jewhone.service.log.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LogServiceImpl implements LogService {

	@Override
	public void deleteLog() {
		String path = System.getProperty("catalina.home");
		String filePath = path + "/logs";
		List<File> fiList = CMyFileUtils.traverseFolder2(filePath);
		Map<String, File> delFile = fiList.stream().collect(Collectors.toMap(File::getName,Function.identity()));
		for (Map.Entry<String, File> file : delFile.entrySet()) {
			try {
				if (file.getKey().split("\\.").length == 3) {//lochost.2018-02-15.log
					String dateStr = file.getKey().split("\\.")[1];
					if (DateTimeUtil.getDayFromNow(DateTimeUtil.toDate(dateStr)) > 15) {
						file.getValue().delete();
					}
				}
			} catch (Exception e) {
				log.error("删除日志文件错误："+file.getKey()+"_"+e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(DateTimeUtil.getDayFromNow(DateTimeUtil.toDate("2018-02-15")));
	}
}