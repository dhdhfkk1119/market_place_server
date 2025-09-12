package com.market.market_place.notification;

import com.market.market_place._core._utils.SseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseUtil sseUtil;

    // 커뮤니티 댓글 알림
    @Async
    public void sendComment(String userId, String postTitle) {
        try {
            String message = String.format("%s 게시글에 %s 님이 댓글을 남겼습니다.", postTitle, userId);

            log.info("댓글 알림 전송 {}", userId);

            sseUtil.sendToUser(userId, "Comment", message);
        } catch (Exception e) {
            sseUtil.sendToUser(userId, "error", "알림 전송 중 오류가 발생했습니다.");
        }
    }

    // 커뮤니티,상품 게시글 좋아요 알림
    public void sendPostLike(String userId, String postTitle) {
        try{
            String message = String.format("%s 님이 %s 게시글에 좋아요를 눌렀습니다.", userId, postTitle);

            log.info("좋아요 알림 전송 {}", userId);
            sseUtil.sendToUser(userId, "PostLike", message);
        } catch (Exception e) {
            sseUtil.sendToUser(userId, "error", "알림 전송 중 오류가 발생했습니다.");
        }
    }

    // 커뮤니티 댓글 좋아요 알림
    public void sendCommentLike(String userId, String commentContent) {
        try{
            String message = String.format("%s 님이 %s 댓글에 좋아요를 눌렀습니다.", userId, commentContent);
            log.info("댓글 좋아요 알림 전송 {}", commentContent);
            sseUtil.sendToUser(userId, "CommentLike", message);
        } catch (Exception e) {
            sseUtil.sendToUser(userId, "error", "알림 전송 중 오류가 발생했습니다.");
        }
    }
}
