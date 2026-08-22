package com.van.seo.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * 접속 로그 — 크롤러 접근과 404 발생을 하나의 테이블로 관리한다.
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-5
 *
 * 개인정보 최소 수집 원칙에 따라 IP는 저장하지 않는다.
 */
@Entity
@Table(name = "access_log", indexes = {
        @Index(name = "idx_access_log_occurred_at", columnList = "occurredAt"),
        @Index(name = "idx_access_log_bot_name", columnList = "botName"),
        @Index(name = "idx_access_log_status", columnList = "status")
})
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 발생 시각 */
    @Column(nullable = false)
    private LocalDateTime occurredAt;

    /** HTTP 메서드 */
    @Column(length = 10)
    private String method;

    /** 요청 경로 */
    @Column(length = 500)
    private String path;

    /** 응답 상태 코드 */
    private int status;

    /** 식별된 봇 이름. 봇이 아니면 null */
    @Column(length = 50)
    private String botName;

    /** 검색·AI 노출과 직접 관련된 봇 여부 */
    private boolean searchOrAiBot;

    /** 유입 경로 */
    @Column(length = 1000)
    private String referer;

    /** Referer에서 추출한 검색 유입 키워드 */
    @Column(length = 200)
    private String searchKeyword;

    /** 원본 User-Agent */
    @Column(length = 500)
    private String userAgent;

    protected AccessLog() {
        // JPA 기본 생성자
    }

    public AccessLog(LocalDateTime occurredAt, String method, String path, int status,
                     String botName, boolean searchOrAiBot,
                     String referer, String searchKeyword, String userAgent) {
        this.occurredAt = occurredAt;
        this.method = method;
        this.path = path;
        this.status = status;
        this.botName = botName;
        this.searchOrAiBot = searchOrAiBot;
        this.referer = referer;
        this.searchKeyword = searchKeyword;
        this.userAgent = userAgent;
    }

    public Long getId() { return id; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public int getStatus() { return status; }
    public String getBotName() { return botName; }
    public boolean isSearchOrAiBot() { return searchOrAiBot; }
    public String getReferer() { return referer; }
    public String getSearchKeyword() { return searchKeyword; }
    public String getUserAgent() { return userAgent; }
}