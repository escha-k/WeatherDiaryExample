package com.project.weather.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <a href="http://localhost:8080/swagger-ui/index.html">Api Docs</a>
 */

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(info());
    }

    private Info info() {
        String description = "날씨 일기를 CRUD 할 수 있는 백엔드 API";

        return new Info()
                .title("날씨 일기 프로젝트")
                .description(description)
                .version("2.1");
    }
}
