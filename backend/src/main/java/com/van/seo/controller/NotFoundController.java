package com.van.seo.controller;

import com.van.seo.crawler.BotDetector;
import com.van.seo.log.AccessLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 404 접근을 기록한다.
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-8, BE-5(DB 저장)
 */
@ControllerAdvice
public class NotFoundController {

    private static final Logger log = LoggerFactory.getLogger(NotFoundController.class);

    private final BotDetector botDetector;
    private final AccessLogService accessLogService;

    public NotFoundController(BotDetector botDetector,
                              AccessLogService accessLogService) {
        this.botDetector = botDetector;
        this.accessLogService = accessLogService;
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseBody
    public ResponseEntity<String> handleNotFound(Exception ex,
                                                 HttpServletRequest request) {
        record(request, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 Not Found");
    }

    private void record(HttpServletRequest request, String path) {
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");
        String botName = botDetector.detect(userAgent);

        log.warn("[404] path={} referer={} bot={} ua={}",
                path, referer, botName == null ? "-" : botName, userAgent);

        accessLogService.save(
                request.getMethod(),
                path,
                404,
                botName,
                botDetector.isSearchOrAiBot(botName),
                referer,
                userAgent
        );
    }
}