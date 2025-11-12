package markit.item.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// 리뷰 응답 DTO (리뷰 전체/요약/작성자 등 반환)
@Getter
@AllArgsConstructor
public class TradeReviewResponse {
    private Long id; // 리뷰 ID
    private String content; // 리뷰 내용
    private String shortContent; // 리뷰 내용 요약
    private double rating; // 리뷰 평점
    private String reviewerLoginId; // 리뷰 작성자 로그인 ID
    private LocalDateTime createdAt; // 리뷰 작성일시

    // 리뷰 엔티티를 DTO로 변환
    public static TradeReviewResponse from(TradeReview tradeReview) {
        String content = tradeReview.getContent();
        String shortContent = content != null && content.length() > 20 ? content.substring(0, 20) + "..." : content;
        return new TradeReviewResponse(
                tradeReview.getId(),
                content,
                shortContent,
                tradeReview.getRating(),
                tradeReview.getReviewer().getLoginId(),
                tradeReview.getCreatedAt()
        );
    }
}