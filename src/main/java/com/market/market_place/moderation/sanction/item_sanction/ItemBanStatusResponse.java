package com.market.market_place.moderation.sanction.item_sanction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemBanStatusResponse {
    private Long memberId;
    private boolean banned;
}
