package markit.item.review;

import markit.item.status.Trade;
import markit.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// 거래 리뷰 엔티티 (DB 테이블 매핑)
@Entity
@Table(name = "trade_review_tb")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeReview {
    @Id // 리뷰 PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_id")
    private Trade trade; // 거래 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private Member reviewer; // 리뷰 작성자

    @Lob
    private String content; // 리뷰 내용

    private double rating; // 리뷰 평점

    @CreationTimestamp
    private LocalDateTime createdAt; // 리뷰 작성일시

    // 리뷰 내용/평점 수정
    public TradeReview update(String content, int rating) {
        this.content = content;
        this.rating = rating;
        return this;
    }
}