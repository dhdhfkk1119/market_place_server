package com.market.market_place.item.status;

import com.market.market_place.item.core.Item;
import com.market.market_place.members.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
public class TradeRequest {

    private Long itemId;
    private Long sellerId;
    private Long buyerId;

    @Builder
    public TradeRequest(Long itemId, Long sellerId, Long buyerId) {
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
    }

    public Trade toEntity(Item item, Member seller, Member buyer) {
        return Trade.createTrade(item, seller, buyer);
    }
}