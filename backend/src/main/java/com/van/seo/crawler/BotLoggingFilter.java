package com.van.seo.crawler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 모든 요청의 User-Agent를 검사해 크롤러 접근을 기록한다.
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-6
 */
@Component
public class BotLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(BotLoggingFilter.class);

    private final BotDetector botDetector;

    public BotLoggingFilter(BotDetector botDetector) {
        this.botDetector = botDetector;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 먼저 요청을 통과시켜 응답 상태 코드까지 확보한다
        filterChain.doFilter(request, response);

        String userAgent = request.getHeader("User-Agent");
        String botName = botDetector.detect(userAgent);

        if (botName != null) {
            record(botName, request, response, userAgent);
        }
    }

    /**
     * 현재는 콘솔 로그로만 남긴다.
     * BE-4(접속 로깅 테이블) 구현 후 이 메서드에서 DB 저장으로 교체한다.
     */
    private void record(String botName,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        String userAgent) {

        boolean searchOrAi = botDetector.isSearchOrAiBot(botName);

        log.info("[BOT] name={} searchOrAi={} method={} path={} status={} referer={} ua={}",
                botName,
                searchOrAi,
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                request.getHeader("Referer"),
                userAgent);
    }
}