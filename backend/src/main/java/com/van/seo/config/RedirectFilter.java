package com.van.seo.config;

import com.van.seo.controller.RedirectRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 변경된 URL로 들어온 요청을 301로 영구 이동시킨다.
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-7
 *
 * 301은 검색엔진에 기존 페이지의 평가를 새 URL로 이전하라는 신호다.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RedirectFilter extends OncePerRequestFilter {

    private final RedirectRegistry redirectRegistry;

    public RedirectFilter(RedirectRegistry redirectRegistry) {
        this.redirectRegistry = redirectRegistry;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String target = redirectRegistry.findTarget(request.getRequestURI());

        if (target != null) {
            String query = request.getQueryString();
            String location = (query == null) ? target : target + "?" + query;

            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", location);
            return;
        }

        filterChain.doFilter(request, response);
    }
}