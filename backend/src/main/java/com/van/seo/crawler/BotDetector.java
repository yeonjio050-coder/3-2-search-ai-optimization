package com.van.seo.crawler;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User-Agent 문자열로 크롤러 종류를 식별한다.
 * 3-2 검색·AI 노출 최적화 / 세부업무 BE-6
 */
@Component
public class BotDetector {

    /** 식별 대상 봇. key = 분류명, value = User-Agent에 포함되는 문자열 */
    private static final Map<String, String> KNOWN_BOTS = new LinkedHashMap<>();

    static {
        // 검색엔진
        KNOWN_BOTS.put("Googlebot", "googlebot");
        KNOWN_BOTS.put("Yeti", "yeti");
        KNOWN_BOTS.put("Bingbot", "bingbot");
        KNOWN_BOTS.put("DaumBot", "daum");
        // 생성형 AI
        KNOWN_BOTS.put("GPTBot", "gptbot");
        KNOWN_BOTS.put("ClaudeBot", "claudebot");
        KNOWN_BOTS.put("PerplexityBot", "perplexitybot");
        KNOWN_BOTS.put("Google-Extended", "google-extended");
        KNOWN_BOTS.put("Applebot", "applebot");
        // SNS 미리보기
        KNOWN_BOTS.put("facebookexternalhit", "facebookexternalhit");
        KNOWN_BOTS.put("Twitterbot", "twitterbot");
        KNOWN_BOTS.put("KakaoTalk", "kakaotalk");
    }

    /**
     * @return 식별된 봇 이름. 봇이 아니면 null
     */
    public String detect(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return null;
        }

        String lower = userAgent.toLowerCase();

        for (Map.Entry<String, String> entry : KNOWN_BOTS.entrySet()) {
            if (lower.contains(entry.getValue())) {
                return entry.getKey();
            }
        }

        // 목록에 없지만 봇으로 보이는 경우
        if (lower.contains("bot") || lower.contains("crawler") || lower.contains("spider")) {
            return "Unknown";
        }

        return null;
    }

    /** 검색·AI 노출과 직접 관련된 봇인지 */
    public boolean isSearchOrAiBot(String botName) {
        if (botName == null) return false;
        return !botName.equals("Unknown")
                && !botName.equals("facebookexternalhit")
                && !botName.equals("Twitterbot")
                && !botName.equals("KakaoTalk");
    }
}