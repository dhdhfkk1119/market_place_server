package com.market.market_place.item.status;

import com.market.market_place.item.core.Item;
import com.market.market_place.item.praise.Praise;
import com.market.market_place.item.review.TradeReview;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trade_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL)
    private List<TradeReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL)
    private List<Praise> praises = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @Setter
    private boolean buyerReviewed;
    @Setter
    private boolean sellerReviewed;

    private double averageScore;

    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;

    public static Trade createTrade(Item item, Member seller, Member buyer) {
        return Trade.builder()
                .item(item)
                .seller(seller)
                .buyer(buyer)
                .buyerReviewed(false)
                .sellerReviewed(false)
                .averageScore(0.0)
                .tradeStatus(TradeStatus.PENDING)
                .build();
    }

    public void addReview(TradeReview review) {
        this.reviews.add(review);
        review.setTrade(this);
        calculateAverageScore();
    }

    public void calculateAverageScore() {
        if (reviews.isEmpty()) {
            this.averageScore = 0.0;
            return;
        }

        double totalScore = reviews.stream().mapToDouble(TradeReview::getScore).sum();
        this.averageScore = totalScore / reviews.size();
    }

    public void markAsReviewed(Member reviewer) {
        if (reviewer.equals(this.buyer)) {
            this.buyerReviewed = true;
        } else if (reviewer.equals(this.seller)) {
            this.sellerReviewed = true;
        }

        if (this.buyerReviewed && this.sellerReviewed) {
            this.tradeStatus = TradeStatus.SOLD;
        }
    }
}