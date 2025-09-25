package com.market.market_place.item.status;

import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.item.core.Item;
import com.market.market_place.item.praise.Praise;
import com.market.market_place.item.review.TradeReview;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trade_tb")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"reviews", "praises", "item", "buyer"})
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<TradeReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Praise> praises = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id",nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id",nullable = false)
    private Member buyer;

    @Setter
    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @Setter
    @Column(nullable = false)
    private boolean buyerReviewed;

    @Setter
    @Column(nullable = false)
    private boolean sellerReviewed;

    private Timestamp createdAt;

    private Timestamp completedAt;

    public String getTime() {
        return DateUtil.timestampFormat(completedAt);
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = new Timestamp(System.currentTimeMillis());
        if (status == null) status = TradeStatus.PENDING;
    }

    public void fillNulls(Timestamp created, Timestamp completed, TradeStatus status) {
        if (this.createdAt == null) this.createdAt = created;
        if (this.completedAt == null) this.completedAt = completed;
        if (this.status == null) this.status = status;
        if (this.reviews == null) this.reviews = new ArrayList<>();
        if (this.praises == null) this.praises = new ArrayList<>();
    }

    public static Trade of(Item item, Member buyer) {
        return Trade.builder()
                .item(item)
                .buyer(buyer)
                .buyerReviewed(false)
                .sellerReviewed(false)
                .status(TradeStatus.PENDING)
                .build();
    }

    public void completeTrade() {
        this.status = TradeStatus.SOLD;
        this.completedAt = Timestamp.valueOf(LocalDateTime.now());
    }
}