package tn.esprit.examenspring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Use allowedOriginPatterns instead of allowedOrigins when allowing credentials
                .allowedOriginPatterns("http://localhost:4200", "http://127.0.0.1:4200")
                // Allow all common HTTP methods
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                // Allow common headers explicitly
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization")
                // Allow credentials
                .allowCredentials(true)
                // Expose these headers to the client
                .exposedHeaders("Authorization", "Content-Type")
                // Cache preflight requests for 1 hour
                .maxAge(3600);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Use allowedOriginPatterns instead of allowedOrigins when using credentials
        config.setAllowedOriginPatterns(List.of("http://localhost:4200", "http://127.0.0.1:4200"));

        // Set allowed headers explicitly
        config.setAllowedHeaders(List.of(
            "Origin", 
            "Content-Type", 
            "Accept", 
            "Authorization", 
            "Access-Control-Allow-Origin", 
            "Access-Control-Request-Method", 
            "Access-Control-Request-Headers"
        ));

        // Set allowed methods explicitly
        config.setAllowedMethods(List.of("OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH"));

        // Allow credentials (must use allowedOriginPatterns, not allowedOrigins with wildcard)
        config.setAllowCredentials(true);

        // Expose headers
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));

        // Cache preflight requests
        config.setMaxAge(3600L);

        // Apply this configuration to all paths
        source.registerCorsConfiguration("/**", config);
        source.registerCorsConfiguration("/User/**", config); // Explicitly add for User endpoints
        source.registerCorsConfiguration("/Interview/**", config);

        return new CorsFilter(source);
    }
}
