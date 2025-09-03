package com.market.market_place.item.status;

import com.market.market_place.item.core.Item;
import com.market.market_place.members.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TradeRequest {

    private Long itemId;
    private Long sellerId;
    private Long buyerId;

    public Trade toEntity(Item item, Member seller, Member buyer) {
        return Trade.builder()
                .item(item)
                .seller(seller)
                .buyer(buyer)
                .buyerReviewed(false)
                .sellerReviewed(false)
                .build();
    }
}