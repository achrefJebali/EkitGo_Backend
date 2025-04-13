package tn.esprit.examenspring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL path "/uploads/**" to the physical upload directory
        String uploadPath = Paths.get(uploadDir).toAbsolutePath().toString();
        
        // Add resource handler for uploaded profile photos
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
        
        // Also ensure that our endpoint paths keep their context path properly
        registry.addResourceHandler("/ElitGo/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
                
        System.out.println("Configured resource handler for uploaded files at: " + uploadPath);
    }
}
