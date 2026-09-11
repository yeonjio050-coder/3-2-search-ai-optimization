package com.van.seo.structureddata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * schema.org 구조화 데이터(JSON-LD) 생성기
 * 3-2 검색·AI 노출 최적화 / SEO-003, 세부업무 BE-10
 *
 * 생성형 AI가 인용 시 참조하는 데이터이므로
 * 기관 정보와 출처 표기의 일관성을 서버에서 보장한다.
 */
@Component
public class JsonLdGenerator {

    private static final String SCHEMA_CONTEXT = "https://schema.org";

    @Value("${seo.site-url}")
    private String siteUrl;

    @Value("${seo.org.name}")
    private String orgName;

    @Value("${seo.org.description}")
    private String orgDescription;

    @Value("${seo.org.logo-path}")
    private String orgLogoPath;

    @Value("${seo.org.name-en}")
    private String orgNameEn;

    @Value("${seo.org.description-en}")
    private String orgDescriptionEn;

    /** 단체 정보 */
    public Map<String, Object> organization() {
        return organization("ko");
    }

    public Map<String, Object> organization(String language) {
        boolean english = "en".equalsIgnoreCase(language);

        Map<String, Object> node = base("Organization");
        node.put("name", english ? orgNameEn : orgName);
        node.put("url", siteUrl + "/");
        node.put("description", english ? orgDescriptionEn : orgDescription);
        node.put("inLanguage", english ? "en" : "ko-KR");
        node.put("logo", imageObject(orgLogoPath));
        return node;
    }

    /** 기사 */
    public Map<String, Object> newsArticle(ArticleRequest req) {
        Map<String, Object> node = base("NewsArticle");
        node.put("headline", req.headline());
        node.put("mainEntityOfPage", absoluteUrl(req.path()));

        putIfPresent(node, "description", req.description());
        putIfPresent(node, "articleSection", req.articleSection());

        if (req.datePublished() != null) {
            node.put("datePublished", req.datePublished().format(DateTimeFormatter.ISO_DATE));
        }
        if (req.dateModified() != null) {
            node.put("dateModified", req.dateModified().format(DateTimeFormatter.ISO_DATE));
        }
        if (req.imagePath() != null && !req.imagePath().isBlank()) {
            node.put("image", imageObject(req.imagePath()));
        }
        if (req.authors() != null && !req.authors().isEmpty()) {
            node.put("author", authorNodes(req.authors()));
        }

        node.put("publisher", publisherNode());
        return node;
    }

    /** 행사 */
    public Map<String, Object> event(EventRequest req) {
        Map<String, Object> node = base("Event");
        node.put("name", req.name());
        node.put("url", absoluteUrl(req.path(), req.siteUrl()));

        putIfPresent(node, "description", req.description());

        if (req.startDate() != null) {
            node.put("startDate", req.startDate().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        if (req.endDate() != null) {
            node.put("endDate", req.endDate().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        if (req.locationName() != null && !req.locationName().isBlank()) {
            node.put("location", locationNode(req.locationName(), req.locationAddress()));
        }

        Map<String, Object> organizer = new LinkedHashMap<>();
        organizer.put("@type", "Organization");
        organizer.put("name", req.organizerName() != null ? req.organizerName() : orgName);
        node.put("organizer", organizer);

        return node;
    }

    // --- 내부 조립 도우미 ---

    private Map<String, Object> base(String type) {
        Map<String, Object> node = new LinkedHashMap<>();
        node.put("@context", SCHEMA_CONTEXT);
        node.put("@type", type);
        return node;
    }

    private Map<String, Object> publisherNode() {
        Map<String, Object> publisher = new LinkedHashMap<>();
        publisher.put("@type", "Organization");
        publisher.put("name", orgName);
        publisher.put("logo", imageObject(orgLogoPath));
        return publisher;
    }

    private List<Map<String, Object>> authorNodes(List<String> names) {
        List<Map<String, Object>> authors = new ArrayList<>();
        for (String name : names) {
            Map<String, Object> author = new LinkedHashMap<>();
            author.put("@type", "Person");
            author.put("name", name);
            authors.add(author);
        }
        return authors;
    }

    private Map<String, Object> locationNode(String name, String address) {
        Map<String, Object> location = new LinkedHashMap<>();
        location.put("@type", "Place");
        location.put("name", name);

        if (address != null && !address.isBlank()) {
            Map<String, Object> postal = new LinkedHashMap<>();
            postal.put("@type", "PostalAddress");
            postal.put("streetAddress", address);
            postal.put("addressCountry", "KR");
            location.put("address", postal);
        }
        return location;
    }

    private Map<String, Object> imageObject(String path) {
        Map<String, Object> image = new LinkedHashMap<>();
        image.put("@type", "ImageObject");
        image.put("url", absoluteUrl(path));
        return image;
    }

       private String absoluteUrl(String path) {
        return absoluteUrl(path, null);
    }

    private String absoluteUrl(String path, String overrideSiteUrl) {
        String base = (overrideSiteUrl != null && !overrideSiteUrl.isBlank())
                ? overrideSiteUrl.replaceAll("/+$", "")
                : siteUrl;
        if (path == null || path.isBlank()) {
            return base + "/";
        }
        return path.startsWith("http") ? path : base + path;
    }

    private void putIfPresent(Map<String, Object> node, String key, String value) {
        if (value != null && !value.isBlank()) {
            node.put(key, value);
        }
    }
}