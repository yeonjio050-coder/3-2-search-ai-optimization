package com.van.seo.controller;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * URL 구조 변경에 따른 301 영구 리다이렉트 매핑을 보관한다.
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-7
 */
@Component
public class RedirectRegistry {

    private static final Logger log = LoggerFactory.getLogger(RedirectRegistry.class);

    @Value("${seo.redirects:}")
    private String rawRedirects;

    private Map<String, String> redirects = Collections.emptyMap();

    @PostConstruct
    void init() {
        Map<String, String> parsed = new HashMap<>();

        if (rawRedirects != null && !rawRedirects.isBlank()) {
            for (String pair : rawRedirects.split(",")) {
                String[] parts = pair.trim().split(":");
                if (parts.length == 2) {
                    parsed.put(parts[0].trim(), parts[1].trim());
                }
            }
        }

        this.redirects = Collections.unmodifiableMap(parsed);
        log.info("[REDIRECT] {}건의 301 매핑 로드됨", redirects.size());
    }

    /** @return 이동할 새 경로. 매핑이 없으면 null */
    public String findTarget(String path) {
        return redirects.get(path);
    }
}