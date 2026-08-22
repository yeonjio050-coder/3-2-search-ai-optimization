package com.van.seo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 접속 로그 저장 진입점.
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-5
 *
 * 로그 저장 실패가 실제 요청 처리에 영향을 주지 않도록 예외를 흡수한다.
 */
@Service
public class AccessLogService {

    private static final Logger log = LoggerFactory.getLogger(AccessLogService.class);

    private final AccessLogRepository repository;
    private final SearchKeywordExtractor keywordExtractor;

    public AccessLogService(AccessLogRepository repository,
                            SearchKeywordExtractor keywordExtractor) {
        this.repository = repository;
        this.keywordExtractor = keywordExtractor;
    }

    @Transactional
    public void save(String method, String path, int status,
                     String botName, boolean searchOrAiBot,
                     String referer, String userAgent) {
        try {
            String keyword = keywordExtractor.extract(referer);

            AccessLog entry = new AccessLog(
                    LocalDateTime.now(),
                    method,
                    truncate(path, 500),
                    status,
                    botName,
                    searchOrAiBot,
                    truncate(referer, 1000),
                    truncate(keyword, 200),
                    truncate(userAgent, 500)
            );

            repository.save(entry);

        } catch (Exception e) {
            log.error("접속 로그 저장 실패: path={}", path, e);
        }
    }

    /** 컬럼 길이를 넘는 값은 잘라서 저장한다 */
    private String truncate(String value, int maxLength) {
        if (value == null) return null;
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}