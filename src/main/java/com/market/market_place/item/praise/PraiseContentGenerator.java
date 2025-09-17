package com.market.market_place.item.praise;

import java.util.List;

public class PraiseContentGenerator {

    public static String generateContentFromTopic(List<String> topicNames) {
        if (topicNames == null || topicNames.isEmpty()) {
            return "좋은 거래 감사합니다!";
        }
        if (topicNames.size() == 1) {
            return "다음과 같은 이유로 칭찬 드려요 : " + topicNames.get(0) + " . ";
        }
        String result = String.join(", ", topicNames.subList(0, topicNames.size() - 1))
                + " 그리고 " + topicNames.get(topicNames.size() - 1);

        return "다음과 같은 이유로 칭찬 드려요 : " + result + " . ";
    }
}