package org.example.hotelrest.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permet les requêtes CORS depuis localhost:3000
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // Origine autorisée
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Méthodes autorisées
                .allowedHeaders("*")  // En-têtes autorisés
                .allowCredentials(true);
    }
}
