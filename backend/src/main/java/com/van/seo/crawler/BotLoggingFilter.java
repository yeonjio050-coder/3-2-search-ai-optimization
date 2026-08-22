package com.van.seo.crawler;

import com.van.seo.log.AccessLogService;
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
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-6, BE-5(DB 저장)
 */
@Component
public class BotLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(BotLoggingFilter.class);

    private final BotDetector botDetector;
    private final AccessLogService accessLogService;

    public BotLoggingFilter(BotDetector botDetector,
                            AccessLogService accessLogService) {
        this.botDetector = botDetector;
        this.accessLogService = accessLogService;
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

        // 404는 NotFoundController에서 별도 기록하므로 중복을 피한다
        if (botName != null && response.getStatus() != 404) {
            record(botName, request, response, userAgent);
        }
    }

    private void record(String botName,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        String userAgent) {

        boolean searchOrAi = botDetector.isSearchOrAiBot(botName);
        String referer = request.getHeader("Referer");

        log.info("[BOT] name={} searchOrAi={} method={} path={} status={} referer={} ua={}",
                botName, searchOrAi, request.getMethod(), request.getRequestURI(),
                response.getStatus(), referer, userAgent);

        accessLogService.save(
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                botName,
                searchOrAi,
                referer,
                userAgent
        );
    }
}