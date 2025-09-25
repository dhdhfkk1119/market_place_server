package com.market.market_place.item.status;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class TradeRequest {
    @Getter @Setter
    public static class CreateDTO {
        @NotNull(message = "상품 ID는 필수입니다.")
        private Long itemId;
    }
}