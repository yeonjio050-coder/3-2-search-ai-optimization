package com.van.seo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * 정적 리소스 캐시 정책
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-9
 *
 * 페이지 로딩 속도는 검색 순위 평가 요소이므로
 * 변경되지 않는 리소스는 브라우저 캐시를 최대한 활용한다.
 */
@Configuration
public class CacheConfig implements WebMvcConfigurer {

    @Value("${seo.cache.static-max-age}")
    private long staticMaxAge;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 해시가 붙은 빌드 산출물 — 장기 캐시 + immutable
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCacheControl(
                        CacheControl.maxAge(staticMaxAge, TimeUnit.SECONDS)
                                .cachePublic()
                                .immutable()
                );

        // 파일명이 고정된 이미지·아이콘 — 하루 캐시 후 재검증
        registry.addResourceHandler("/*.svg", "/*.png", "/*.jpg", "/*.ico")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(
                        CacheControl.maxAge(1, TimeUnit.DAYS)
                                .cachePublic()
                                .mustRevalidate()
                );
    }
}