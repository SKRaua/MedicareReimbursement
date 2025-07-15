package com.cqupt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.Contact;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
 * @author Wang Yanchen
 * @date 2025/07/12/08:57:33
 * @description
 */
@Configuration
@EnableSwagger2 // 启用Swagger2自动配置
public class SwaggerConfig {
    // Swagger 帮助生成接口文档
    // 配置生成的文档信息：名称，作者，版本。。。
    // 配置文档的生成规则

    // Docket: 封装的接口信息，定义api的生成规则
    @Bean
    public Docket getDocket() {
        // 封装封面信息
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title("《医疗保险报销系统》后端接口说明")
                .description("文档说明：《医疗保险报销系统》后端接口说明")
                .version("v2.1.1")
                .contact(new Contact("skraua", "https://github.com/skraua", "ltghbwy@gamil.com"));

        // 设置文档的基本信息，指定生成的文档的生成规则：DocumentationType.SWAGGER_2
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfoBuilder.build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cqupt.controller"))
                .paths(PathSelectors.any()) // 指定路径为...下的所有路径
                .build();
    }
}
