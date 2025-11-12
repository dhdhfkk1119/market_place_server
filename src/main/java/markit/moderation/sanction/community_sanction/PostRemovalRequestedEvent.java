package markit.moderation.sanction.community_sanction;

public record PostRemovalRequestedEvent(
        Long postId,
        String reason,
        Long reportId,
        Long sanctionedMemberId
) {}
