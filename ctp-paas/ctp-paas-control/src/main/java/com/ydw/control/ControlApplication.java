package com.ydw.control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1215:50
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ControlApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ControlApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ControlApplication.class);
    }
}
