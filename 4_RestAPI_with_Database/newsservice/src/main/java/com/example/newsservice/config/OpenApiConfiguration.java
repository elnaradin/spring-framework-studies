package com.example.newsservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;
import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Value("${server.host}")
    private String host;
    @Value("${server.port}")
    private String port;

    @Bean
    public OpenAPI openApiDescription() {
        Server localhostServer = new Server();
        localhostServer.setUrl(MessageFormat.format("http://{0}:{1}", host, port));
        localhostServer.setDescription("local env");
        Info info = new Info()
                .title("News service API")
                .description("API for news service")
                .version("1.0");

        return new OpenAPI().info(info).servers(List.of(localhostServer));

    }
}
