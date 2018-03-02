package org.caizs.project.configs;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import org.caizs.project.common.utils.DateConvertor;

/**
 * 
* @ClassName: WebMVCConfig 
* @Description: web MVC配置
*
 */
@Configuration
public class WebMVCConfig extends  WebMvcConfigurerAdapter {

    /**
     * 这里增加对swagger的支持
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    }

    /**
     * 这里提供rest请求常见日期字符串到java.util.Date对象的解析
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                if (StringUtils.isBlank(source)) {
                    return null;
                }
                return DateConvertor.parseDate(source.trim());
            }
        });
        super.addFormatters(registry);
    }
    

}