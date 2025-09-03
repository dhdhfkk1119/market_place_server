package com.market.market_place.item.status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TradeResponse {

    private Long id;
    private Long itemId;
    private Long sellerId;
    private Long buyerId;
    private boolean buyerReviewed;
    private boolean sellerReviewed;

    public TradeResponse(Trade trade) {
        this.id = trade.getId();
        this.itemId = trade.getItem().getId();
        this.sellerId = trade.getSeller().getId();
        this.buyerId = trade.getBuyer().getId();
        this.buyerReviewed = trade.isBuyerReviewed();
        this.sellerReviewed = trade.isSellerReviewed();
    }
}