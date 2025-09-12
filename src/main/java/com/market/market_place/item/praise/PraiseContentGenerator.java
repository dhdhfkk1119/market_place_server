package com.market.market_place.item.praise;

import java.util.List;

public class PraiseContentGenerator {

    public static String generateContentFromTopic(List<String> topicNames) {
        if (topicNames == null || topicNames.isEmpty()) {
            return "좋은 거래 감사합니다!";
        }

        StringBuilder stringBuilder = new StringBuilder("다음과 같은 이유로 칭찬 드려요:");

        for (int i = 0; i < topicNames.size(); i++) {
            stringBuilder.append(topicNames.get(i));
            if (i < topicNames.size() - 2) {
                stringBuilder.append(", ");
            } else if (i == topicNames.size() - 2) {
                stringBuilder.append("그리고");
            }
        }
        stringBuilder.append(".");
        return stringBuilder.toString();
    }
}
