package com.zhp.jewhone.core.config;

import com.zhp.jewhone.core.constant.Const;
import com.zhp.jewhone.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 应用配置类
 */
@Slf4j
public class SysConfig extends Properties {
    private static final long serialVersionUID = 4595789767351251968L;
    private volatile static SysConfig properties = null;

    private SysConfig() {
    }

    /**
     * 初始化存储实体
     */
    public static SysConfig getInstance() {
        if (properties == null) {
            syncLoadProperties();
        }
        return properties;
    }

    private static synchronized void syncLoadProperties() {
        try {
            if (properties == null) {
                properties = new SysConfig();
                properties.load(new InputStreamReader(SysConfig.class
                        .getClassLoader().getResourceAsStream(
                                Const.SYS_CONF_PATH), Const.FILE_WRITING_ENCODING));
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    public String getValueByKey(String strKey) {
        return properties.getProperty(strKey);
    }

    public String getValueByKey(String strKey, Object... papamsArr) {
        String ret = properties.getProperty(strKey);
        if (ret != null && papamsArr != null && papamsArr.length > 0) {
            for (int i = 0; i < papamsArr.length; i++) {
                ret = ret.replace("{" + i + "}", papamsArr[i].toString());
            }
        }
        return ret;
    }

    public int getIntValueByKey(String strKey) {
        int result = 0;
        try {
            result = StringUtils.getInt(new String(properties
                    .getProperty(strKey).getBytes(Const.FILE_ISO_ENCODING), Const.FILE_WRITING_ENCODING));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
