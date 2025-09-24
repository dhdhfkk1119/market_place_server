package com.market.market_place.item.status;

import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.item.core.Item;
import com.market.market_place.item.praise.Praise;
import com.market.market_place.item.review.TradeReview;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trade_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TradeReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Praise> praises = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id",nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id",nullable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id",nullable = false)
    private Member buyer;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @Column(nullable = false)
    private boolean buyerReviewed;

    @Column(nullable = false,updatable = false)
    private boolean sellerReviewed;

    private Timestamp createdAt;

    private Timestamp completedAt;

    public void setBuyerReviewed(boolean buyerReviewed) {
        this.buyerReviewed = buyerReviewed;
    }

    public void setSellerReviewed(boolean sellerReviewed) {
        this.sellerReviewed = sellerReviewed;
    }

    public String getTime() {
        return DateUtil.timestampFormat(completedAt);
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = new Timestamp(System.currentTimeMillis());
        if (status == null) status = TradeStatus.PENDING;
        if (reviews == null) reviews = new ArrayList<>();
        if (praises == null) praises = new ArrayList<>();
        if (status == TradeStatus.SOLD && completedAt == null) {
            completedAt = new Timestamp(createdAt.getTime() + 2 * 60 * 60 * 1000);
        }
    }
}