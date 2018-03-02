package org.caizs.project.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
* @ClassName: CORSConfiguration 
* @Description: 全局跨域配置，或采用@CrossOrigin注解配置允许局部请求跨域
*               参考 http://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/htmlsingle/#cors
*
 */
@Configuration
public class CORSConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")    //匹配了所有的URL
                        .allowedHeaders("*")  //允许跨域请求包含任意的头信息
                        .allowedMethods("*")  //允许外域发起请求任意HTTP Method
                        .allowedOrigins("*"); //允许所有的外域发起跨域请求
            }
        };
    }
}