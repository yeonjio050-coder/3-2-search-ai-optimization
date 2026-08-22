package com.van.seo.controller;

import com.van.seo.log.AccessLog;
import com.van.seo.log.AccessLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 접속 로그 조회 API (내부 확인용)
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-5
 *
 * robots.txt에서 /api/ 경로를 색인 차단하고 있다.
 */
@RestController
@RequestMapping("/api/access-logs")
public class AccessLogController {

    private final AccessLogRepository repository;

    public AccessLogController(AccessLogRepository repository) {
        this.repository = repository;
    }

    /** 최근 로그 */
    @GetMapping
    public List<AccessLog> recent(@RequestParam(defaultValue = "50") int limit) {
        return repository.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "occurredAt"))
        ).getContent();
    }

    /** 404 발생 기록 */
    @GetMapping("/not-found")
    public List<AccessLog> notFound() {
        return repository.findByStatusOrderByOccurredAtDesc(404);
    }

    /** 검색 유입 키워드가 기록된 건 */
    @GetMapping("/search-keywords")
    public List<AccessLog> searchKeywords() {
        return repository.findBySearchKeywordIsNotNullOrderByOccurredAtDesc();
    }
}