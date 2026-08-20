package com.van.seo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * sitemap.xml 동적 생성
 * 3-2 검색·AI 노출 최적화 / SEO-002
 */
@RestController
public class SitemapController {

    @Value("${seo.site-url}")
    private String siteUrl;

    /**
     * 사이트맵에 포함할 URL 한 건.
     * path: 사이트 내 경로, changefreq: 변경 주기, priority: 상대 우선순위
     */
    private record SitemapEntry(String path, String changefreq, String priority) {}

    /**
     * 현재는 정적 목록. 기사 데이터가 생기면 이 메서드가 DB에서 조회하도록 교체한다.
     */
    private List<SitemapEntry> collectEntries() {
        return List.of(
                new SitemapEntry("/", "daily", "1.0"),
                new SitemapEntry("/news", "hourly", "0.9"),
                new SitemapEntry("/about", "monthly", "0.5"),
                new SitemapEntry("/contact", "yearly", "0.3")
        );
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap() {
        String today = LocalDate.now().toString();

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        for (SitemapEntry entry : collectEntries()) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(siteUrl).append(entry.path()).append("</loc>\n");
            xml.append("    <lastmod>").append(today).append("</lastmod>\n");
            xml.append("    <changefreq>").append(entry.changefreq()).append("</changefreq>\n");
            xml.append("    <priority>").append(entry.priority()).append("</priority>\n");
            xml.append("  </url>\n");
        }

        xml.append("</urlset>\n");
        return xml.toString();
    }
}