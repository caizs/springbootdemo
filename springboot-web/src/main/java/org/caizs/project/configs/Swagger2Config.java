package org.caizs.project.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //.apis(RequestHandlerSelectors.any()) //所有接口
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) //只提供有ApiOperation声明的接口
                //.apis(RequestHandlerSelectors.basePackage("org.caizs.web"))//包扫描下任意路径，可用通过@ApiIgnore排除
                .paths(PathSelectors.any())
                .build().pathMapping("/"); // 在这里可以设置请求的统一前缀，如/numas ;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTful APIs")
                .description("this is description. see more：http://swagger.io/")
                .version("1.0")
                .build();
    }
}