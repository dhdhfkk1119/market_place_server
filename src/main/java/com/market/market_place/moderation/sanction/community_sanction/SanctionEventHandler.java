package com.market.market_place.moderation.sanction.community_sanction;

import com.market.market_place.community.community_post.CommunityPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SanctionEventHandler {

    private final CommunityPostService communityPostService;

    @EventListener
    @Transactional
    public void on(PostRemovalRequestedEvent event) {
        try {
            communityPostService.forceDelete(event.postId(), event.reason());
            log.info("[SanctionEvent] postId={} force deleted. reason={}, reportId={}, memberId={}",
                    event.postId(), event.reason(), event.reportId(), event.sanctionedMemberId());
        } catch (Exception e) {
            log.warn("[SanctionEvent] 게시글 삭제 실패 postId={}, err={}", event.postId(), e.getMessage());
        }
    }
}
