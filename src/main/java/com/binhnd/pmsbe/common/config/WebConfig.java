package com.binhnd.pmsbe.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private static final Logger L = LoggerFactory.getLogger(WebConfig.class);

    @Value("${pms.cors.origins}")
    private String corsOrigins;
    @Value("${pms.methods.allowed}")
    private String allowedMethods;
    @Value("${pms.header.allowed}")
    private String allowedHeaders;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        L.info("CORS: " + corsOrigins);

        registry.addMapping("/api/**")
                .allowCredentials(true)
                .allowedOrigins(corsOrigins.split(","))
                .allowedMethods(allowedMethods.split(","))
                .maxAge(3600)
                .exposedHeaders("Content-Disposition");
    }
}
