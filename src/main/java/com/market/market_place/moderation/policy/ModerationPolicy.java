package com.market.market_place.moderation.policy;

import java.time.Duration;

public class ModerationPolicy {

    private ModerationPolicy() {
    }

    public static Duration tempBanDurationByAcceptedCount(int count) {
        return switch (count) {
            case 1 -> Duration.ofHours(12);   // 1회 -> 12시간 정지
            case 2 -> Duration.ofHours(24);   // 2회 -> 24시간(1일) 정지
            case 3 -> Duration.ofHours(72);   // 3회 -> 72시간(3일) 정지
            case 4 -> Duration.ofDays(7);     // 4회 -> 7일 정지
            default -> Duration.ZERO;        // 5회 이상은 영구 정지
        };
    }

    // 5회 이상이면 영구정지 처리
    public static boolean isPermanentThreshold(int count) {
        return count >= 5;
    }
}
