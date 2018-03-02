package org.caizs.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

//默认属性使用 @Configuration ， @EnableAutoConfiguration 和 @ComponentScan 
@SpringBootApplication
@ImportResource("classpath:spring-job.xml")
public class Application  {

    public static ConfigurableApplicationContext sContext;

    public static void main(String[] args) {
        sContext = SpringApplication.run(Application.class, args);
    }
}
