package com.van.seo.controller;

import com.van.seo.crawler.BotDetector;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 404 접근을 기록한다.
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-8
 *
 * 크롤러가 깨진 링크를 반복 요청하면 크롤링 예산이 낭비되므로,
 * 어떤 경로가 어디서 유입되어 실패하는지 추적한다.
 */
@ControllerAdvice
public class NotFoundController {

    private static final Logger log = LoggerFactory.getLogger(NotFoundController.class);

    private final BotDetector botDetector;

    public NotFoundController(BotDetector botDetector) {
        this.botDetector = botDetector;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex,
                                                 HttpServletRequest request) {
        record(request, ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 Not Found");
    }

    /**
     * 현재는 콘솔 로그로만 남긴다.
     * BE-4(접속 로깅 테이블) 구현 후 DB 저장으로 교체한다.
     */
    private void record(HttpServletRequest request, String path) {
        String userAgent = request.getHeader("User-Agent");
        String botName = botDetector.detect(userAgent);

        log.warn("[404] path={} referer={} bot={} ua={}",
                path,
                request.getHeader("Referer"),
                botName == null ? "-" : botName,
                userAgent);
    }
}