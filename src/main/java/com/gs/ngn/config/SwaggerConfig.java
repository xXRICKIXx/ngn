package com.gs.ngn.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("NGN – Nova Gaia Nexus API")
                .version("1.0.0")
                .description("Sistema aeroespacial de monitoramento e gestão de habitats espaciais.")
                .contact(new Contact().name("Grupo GS").email("gs@ngn.space"))
            );
    }
}
