package com.zhp.jewhone;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 外部tomcat启动类配置
 */
public class ServletInitializer extends SpringBootServletInitializer {  

    @Override  
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
        return application.sources(ProviderProject.class);
    }  

}