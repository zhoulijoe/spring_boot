package com.zhou.quickpoll;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.models.dto.builder.ApiInfoBuilder;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger
public class SwaggerConfig {

    @Autowired
    private SpringSwaggerConfig springSwaggerConfig;

    private ApiInfo getApiInfo() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("QuickPoll REST API")
                .description("QuickPoll api for creating and manging polls")
                .termsOfServiceUrl("http://quickpoll.com/tos")
                .contact("hello@quickpoll.com")
                .license("MIT License")
                .licenseUrl("http://opensource.org/licenses/MIT")
                .build();

        return apiInfo;
    }

    @Bean
    public SwaggerSpringMvcPlugin v1ApiConfiguration() {
        SwaggerSpringMvcPlugin swaggerSpringMvcPlugin = new SwaggerSpringMvcPlugin(springSwaggerConfig);

        swaggerSpringMvcPlugin.apiInfo(getApiInfo())
                .apiVersion("1.0")
                .swaggerGroup("v1")
                .includePatterns("/v1/*.*")
                .useDefaultResponseMessages(false);

        return swaggerSpringMvcPlugin;
    }

    @Bean
    public SwaggerSpringMvcPlugin v2ApiConfiguration() {
        SwaggerSpringMvcPlugin swaggerSpringMvcPlugin = new SwaggerSpringMvcPlugin(springSwaggerConfig);

        swaggerSpringMvcPlugin.apiInfo(getApiInfo())
                .apiVersion("2.0")
                .swaggerGroup("v2")
                .includePatterns("/v2/*.*")
                .useDefaultResponseMessages(false);

        return swaggerSpringMvcPlugin;
    }
}
