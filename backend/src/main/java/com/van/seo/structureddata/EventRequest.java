package com.van.seo.structureddata;

import java.time.LocalDateTime;

/**
 * Event JSON-LD 생성 요청
 * 3-2 검색·AI 노출 최적화 / SEO-003
 *
 * siteUrl은 요청한 사이트의 도메인이다.
 * 값이 없으면 서버 설정값(seo.site-url)을 사용한다.
 */
public record EventRequest(
        String name,
        String description,
        String path,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String locationName,
        String locationAddress,
        String organizerName,
        String siteUrl
) {}