package com.van.seo.controller;

import com.van.seo.structureddata.ArticleRequest;
import com.van.seo.structureddata.EventRequest;
import com.van.seo.structureddata.JsonLdGenerator;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 구조화 데이터(JSON-LD) 생성 API
 * 3-2 검색·AI 노출 최적화 / SEO-003, 세부업무 BE-10
 *
 * 사이트(2-3·2-4·2-5)가 이 API로 받은 JSON-LD를
 * head의 script[type="application/ld+json"]에 삽입한다.
 */
@RestController
@RequestMapping("/api/json-ld")
public class JsonLdController {

    private final JsonLdGenerator generator;

    public JsonLdController(JsonLdGenerator generator) {
        this.generator = generator;
    }

    /** 단체 정보 — 값이 고정이므로 캐시를 길게 잡는다 */
    @GetMapping(value = "/organization", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> organization() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
                .body(generator.organization());
    }

    /** 기사 */
    @PostMapping(value = "/news-article",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> newsArticle(@RequestBody ArticleRequest request) {
        return ResponseEntity.ok(generator.newsArticle(request));
    }

    /** 행사 */
    @PostMapping(value = "/event",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> event(@RequestBody EventRequest request) {
        return ResponseEntity.ok(generator.event(request));
    }
}