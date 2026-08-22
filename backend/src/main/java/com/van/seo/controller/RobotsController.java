package com.van.seo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * robots.txt 동적 서빙
 * 3-2 검색·AI 노출 최적화 / SEO-008, 세부업무 BE-9(캐시)
 */
@RestController
public class RobotsController {

    @Value("${seo.site-url}")
    private String siteUrl;

    @Value("${seo.robots.allow-indexing}")
    private boolean allowIndexing;

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> robots() {

        if (!allowIndexing) {
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                    .body("""
                            # 색인 차단 환경
                            User-agent: *
                            Disallow: /
                            """);
        }

        String body = """
                # VAN 뉴스 크롤러 접근 규칙

                User-agent: *
                Allow: /
                Disallow: /admin/
                Disallow: /api/
                Disallow: /test/

                # 검색엔진 크롤러
                User-agent: Googlebot
                Allow: /

                User-agent: Yeti
                Allow: /

                # 생성형 AI 크롤러 — 답변·인용 노출을 위해 허용
                User-agent: GPTBot
                Allow: /

                User-agent: ClaudeBot
                Allow: /

                User-agent: PerplexityBot
                Allow: /

                User-agent: Google-Extended
                Allow: /

                Sitemap: %s/sitemap.xml
                """.formatted(siteUrl);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
                .body(body);
    }
}