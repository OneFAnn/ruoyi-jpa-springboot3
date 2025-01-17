package com.ruoyi.web.core.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Swagger2的接口配置
 * 
 * @author ruoyi
 */
@Configuration
public class SwaggerConfig
{
//    /** 系统基础配置 */
//    @Autowired
//    private RuoYiConfig ruoyiConfig;
//
//    /** 是否开启swagger */
//    @Value("${swagger.enabled}")
//    private boolean enabled;
//
//    /** 设置请求的统一前缀 */
//    @Value("${swagger.pathMapping}")
//    private String pathMapping;
//
//    /**
//     * 创建API
//     */
//    @Bean
//    public Docket createRestApi()
//    {
//        return new Docket(DocumentationType.OAS_30)
//                // 是否启用Swagger
//                .enable(enabled)
//                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
//                .apiInfo(apiInfo())
//                // 设置哪些接口暴露给Swagger展示
//                .select()
//                // 扫描所有有注解的api，用这种方式更灵活
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                // 扫描指定包中的swagger注解
//                // .apis(RequestHandlerSelectors.basePackage("com.ruoyi.project.tool.swagger"))
//                // 扫描所有 .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                /* 设置安全模式，swagger可以设置访问token */
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts())
//                .pathMapping(pathMapping);
//    }
//
//    /**
//     * 安全模式，这里指定token通过Authorization头请求头传递
//     */
//    private List<SecurityScheme> securitySchemes()
//    {
//        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
//        apiKeyList.add(new ApiKey("Authorization", "Authorization", In.HEADER.toValue()));
//        return apiKeyList;
//    }
//
//    /**
//     * 安全上下文
//     */
//    private List<SecurityContext> securityContexts()
//    {
//        List<SecurityContext> securityContexts = new ArrayList<>();
//        securityContexts.add(
//                SecurityContext.builder()
//                        .securityReferences(defaultAuth())
//                        .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
//                        .build());
//        return securityContexts;
//    }
//
//    /**
//     * 默认的安全上引用
//     */
//    private List<SecurityReference> defaultAuth()
//    {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        List<SecurityReference> securityReferences = new ArrayList<>();
//        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
//        return securityReferences;
//    }
//
//    /**
//     * 添加摘要信息
//     */
//    private ApiInfo apiInfo()
//    {
//        // 用ApiInfoBuilder进行定制
//        return new ApiInfoBuilder()
//                // 设置标题
//                .title("标题：若依管理系统_接口文档")
//                // 描述
//                .description("描述：用于管理集团旗下公司的人员信息,具体包括XXX,XXX模块...")
//                // 作者信息
//                .contact(new Contact(ruoyiConfig.getName(), null, null))
//                // 版本
//                .version("版本号:" + ruoyiConfig.getVersion())
//                .build();
//    }
        @Bean
        public GroupedOpenApi publicApi() {
            return GroupedOpenApi.builder()
                    .group("ruoyi-test")
                    .pathsToMatch("/test/**")
                    .build();
        }
    @Bean
    public OpenAPI openApi(@Value("${spring.application.name}") String applicationName, ObjectProvider<BuildProperties> buildProperties) {
        OpenAPI openAPI = new OpenAPI();
        // 基本信息
        openAPI.info(new Info().title(applicationName)
                .description("服务名称TEST")
                .version(Optional.ofNullable(buildProperties.getIfAvailable()).map(BuildProperties::getVersion).orElse("1.0.0")));
        return openAPI;
    }
}
