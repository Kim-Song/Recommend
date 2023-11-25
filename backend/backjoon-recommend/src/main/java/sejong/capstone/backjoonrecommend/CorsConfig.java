package sejong.capstone.backjoonrecommend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/analysis")
                .allowedOrigins("chrome-extension://hbmbmlflnmmaahlibbddokehloeodpmd")
                .allowedMethods("POST")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}