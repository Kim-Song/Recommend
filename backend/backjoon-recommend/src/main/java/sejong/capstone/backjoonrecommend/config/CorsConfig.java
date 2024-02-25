package sejong.capstone.backjoonrecommend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cors 문제를 해결하기 위한 코드
 * 지금은 모든 소스들에 대해 열어두었지만
 * 보안상 배포 전에 우리 api를 호출하는 클라이언트에 대해서만 열어주어야 한다.
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*");
    }
}