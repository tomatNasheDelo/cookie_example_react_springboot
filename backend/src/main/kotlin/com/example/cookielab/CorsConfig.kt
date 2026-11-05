package com.example.cookielab

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig(
    @Value("\${app.cors.allowed-origins}") private val allowedOrigins: String,
) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
            .allowedOrigins(*allowedOrigins.split(",").map(String::trim).filter(String::isNotEmpty).toTypedArray())
            .allowedMethods("GET", "POST", "OPTIONS")
            .allowedHeaders("Content-Type")
            .allowCredentials(true)
            .maxAge(3600)
    }
}
