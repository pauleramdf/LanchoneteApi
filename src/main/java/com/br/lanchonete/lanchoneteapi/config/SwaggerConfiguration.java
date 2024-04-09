package com.br.lanchonete.lanchoneteapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!production")
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .components(
                        new Components())
                .addServersItem(new Server().url("http://localhost:5001"))
                .info(new Info()
                        .title("Lanchonete API")
                        .version("v1.0")
                );
    }
}
