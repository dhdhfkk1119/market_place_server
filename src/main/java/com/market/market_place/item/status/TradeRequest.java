package com.market.market_place.item.status;

import com.market.market_place.item.core.Item;
import com.market.market_place.item.review.TradeReview;
import com.market.market_place.members.domain.Member;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TradeRequest {

    private Long itemId;
    private Long sellerId;
    private Long buyerId;
    private List<TradeReview> reviews = new ArrayList<>();

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