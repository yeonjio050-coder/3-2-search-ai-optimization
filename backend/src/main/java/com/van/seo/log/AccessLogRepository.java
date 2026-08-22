package com.van.seo.log;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 접속 로그 저장·조회
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-5
 */
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    /** 특정 기간의 봇 접근 기록 */
    List<AccessLog> findByBotNameIsNotNullAndOccurredAtBetween(
            LocalDateTime from, LocalDateTime to);

    /** 특정 상태 코드의 기록 (404 조회용) */
    List<AccessLog> findByStatusOrderByOccurredAtDesc(int status);

    /** 검색 유입 키워드가 있는 기록 */
    List<AccessLog> findBySearchKeywordIsNotNullOrderByOccurredAtDesc();
}