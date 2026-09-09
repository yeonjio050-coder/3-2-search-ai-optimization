package com.van.seo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 정책
 * 3-2 검색·AI 노출 최적화
 *
 * 프론트엔드(공식 홈페이지, 컨퍼런스 사이트)가 브라우저에서
 * JSON-LD 생성 API를 직접 호출할 수 있도록 허용 출처를 설정한다.
 * 허용 목록은 application.properties의 seo.cors.allowed-origins 에서 관리한다.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${seo.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}