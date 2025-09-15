package com.market.market_place.item.status;

import com.market.market_place.item.core.Item;
import com.market.market_place.item.praise.Praise;
import com.market.market_place.item.review.TradeReview;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trade_tb")
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    private boolean buyerReviewed;
    private boolean sellerReviewed;

    public void setBuyerReviewed(boolean buyerReviewed) {
        this.buyerReviewed = buyerReviewed;
    }

    public void setSellerReviewed(boolean sellerReviewed) {
        this.sellerReviewed = sellerReviewed;
    }


}