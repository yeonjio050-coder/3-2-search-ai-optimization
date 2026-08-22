package com.van.seo.structureddata;

import java.time.LocalDate;
import java.util.List;

/**
 * NewsArticle JSON-LD 생성 요청
 * 3-2 검색·AI 노출 최적화 / SEO-003
 */
public record ArticleRequest(
        String headline,
        String description,
        String path,
        LocalDate datePublished,
        LocalDate dateModified,
        List<String> authors,
        String imagePath,
        String articleSection
) {}