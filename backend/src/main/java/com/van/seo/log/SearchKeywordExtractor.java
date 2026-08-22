package com.van.seo.log;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Referer URL에서 검색 유입 키워드를 추출한다.
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-5
 *
 * 검색엔진마다 키워드를 담는 쿼리 파라미터가 다르다.
 */
@Component
public class SearchKeywordExtractor {

    /** 도메인 일부 → 검색어 파라미터명 */
    private static final Map<String, List<String>> KEYWORD_PARAMS = Map.of(
            "google.", List.of("q"),
            "naver.", List.of("query", "q"),
            "daum.", List.of("q"),
            "bing.", List.of("q"),
            "search.yahoo", List.of("p"),
            "duckduckgo", List.of("q")
    );

    /**
     * @return 추출된 검색어. 검색엔진 유입이 아니거나 파싱 실패 시 null
     */
    public String extract(String referer) {
        if (referer == null || referer.isBlank()) {
            return null;
        }

        try {
            URI uri = URI.create(referer);
            String host = uri.getHost();
            String query = uri.getQuery();

            if (host == null || query == null) {
                return null;
            }

            List<String> params = findParams(host.toLowerCase());
            if (params == null) {
                return null;
            }

            for (String pair : query.split("&")) {
                int idx = pair.indexOf('=');
                if (idx <= 0) continue;

                String key = pair.substring(0, idx);
                if (params.contains(key)) {
                    String value = URLDecoder.decode(
                            pair.substring(idx + 1), StandardCharsets.UTF_8);
                    return value.isBlank() ? null : value;
                }
            }
        } catch (Exception e) {
            // 파싱 실패는 로깅 실패로 이어지지 않도록 무시한다
            return null;
        }

        return null;
    }

    private List<String> findParams(String host) {
        for (Map.Entry<String, List<String>> entry : KEYWORD_PARAMS.entrySet()) {
            if (host.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}