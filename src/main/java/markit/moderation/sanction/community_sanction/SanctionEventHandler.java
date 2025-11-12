package markit.moderation.sanction.community_sanction;

import markit.community.community_post.CommunityPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component // 빈 등록 (이벤트 리스너 역할)
@RequiredArgsConstructor
public class SanctionEventHandler {

    private final CommunityPostService communityPostService;

    // 스프링 이벤트 리스너로 등록
    @EventListener
    @Transactional
    public void on(PostRemovalRequestedEvent event) {
        try {
            // 이벤트에 담긴 postId, reason을 이용해 게시글 강제 삭제 실행
            communityPostService.forceDelete(event.postId(), event.reason());
            log.info("[SanctionEvent] postId={} force deleted. reason={}, reportId={}, memberId={}",
                    event.postId(), event.reason(), event.reportId(), event.sanctionedMemberId());
        } catch (Exception e) {
            log.warn("[SanctionEvent] 게시글 삭제 실패 postId={}, err={}", event.postId(), e.getMessage());
        }
    }
}
