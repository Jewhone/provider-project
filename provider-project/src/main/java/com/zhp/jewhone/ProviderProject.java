package com.zhp.jewhone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@SpringBootApplication
@MapperScan("com.zhp.jewhone.dao")
public class ProviderProject {

    public static void main(String[] args) {
        //System.setProperty("spring.devtools.restart.enabled","false");//关闭热加载
        SpringApplication.run(ProviderProject.class, args);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return (container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/page/404.html");//
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/page/404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/page/404.html");
            container.addErrorPages(error401Page, error404Page, error500Page);
        });
    }
}
