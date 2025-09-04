package com.market.market_place.item.review;

import com.market.market_place.item.status.Trade;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "trade_review_tb")
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    private double rating;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public TradeReview update(String content, int rating) {
        this.content = content;
        this.rating = rating;
        return this;
    }
}