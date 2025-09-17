package com.market.market_place.moderation.sanction.community_sanction;

public record PostRemovalRequestedEvent(
        Long postId,
        String reason,
        Long reportId,
        Long sanctionedMemberId
) {}
