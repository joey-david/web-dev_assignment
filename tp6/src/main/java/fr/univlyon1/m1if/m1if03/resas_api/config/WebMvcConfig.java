package fr.univlyon1.m1if.m1if03.resas_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe de configuration principale.
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("https://editor-next.swagger.io");
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE");
        registry.addMapping("/**").allowedHeaders("Authorization", "Location", "Link");
        registry.addMapping("/**").exposedHeaders("Authorization", "Location", "Link");
    }
}
