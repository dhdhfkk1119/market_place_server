package com.market.market_place.item.review;

import com.market.market_place.item.status.Trade;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "trade_review_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TradeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_id")
    private Trade trade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private Member reviewer;

    @Lob
    private String content;

    private double score;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public TradeReview update(String content, double score) {
        this.content = content;
        this.score = score;
        return this;
    }

    public static TradeReview createReview(Trade trade, Member reviewer, String content, double score) {
        return TradeReview.builder()
                .trade(trade)
                .reviewer(reviewer)
                .content(content)
                .score(score)
                .build();
    }
}