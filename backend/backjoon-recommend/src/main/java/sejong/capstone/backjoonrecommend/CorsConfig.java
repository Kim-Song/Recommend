package sejong.capstone.backjoonrecommend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/analysis")
                .allowedOrigins("chrome-extension://dmoehccjhiappieehmocglalfbnbegnd")
                .allowedMethods("POST")
                .allowedHeaders("")
                .allowCredentials(true);

        registry.addMapping("/problem")
                .allowedOrigins("chrome-extension://dmoehccjhiappieehmocglalfbnbegnd")
                .allowedMethods("GET")
                .allowedHeaders("")
                .allowCredentials(true);

        registry.addMapping("/video")
                .allowedOrigins("chrome-extension://dmoehccjhiappieehmocglalfbnbegnd")
                .allowedMethods("GET")
                .allowedHeaders("*")
                .allowCredentials(true);

    }
}