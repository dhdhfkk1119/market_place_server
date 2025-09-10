package com.market.market_place.item.praise;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PraiseRequest {
    private Long praisedMemberId;
    private Long tradeId;
}
