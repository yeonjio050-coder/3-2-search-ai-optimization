package com.van.seo.structureddata;

import java.time.LocalDateTime;

/**
 * Event JSON-LD 생성 요청
 * 3-2 검색·AI 노출 최적화 / SEO-003
 */
public record EventRequest(
        String name,
        String description,
        String path,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String locationName,
        String locationAddress,
        String organizerName
) {}