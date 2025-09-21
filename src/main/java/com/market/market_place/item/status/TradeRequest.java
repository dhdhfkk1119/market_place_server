package com.market.market_place.item.status;

import com.market.market_place.item.core.Item;
import com.market.market_place.members.domain.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TradeRequest {
    @NotNull(message = "아이템 ID는 필수입니다.")
    private Long itemId;

    @NotNull(message = "판매자 ID는 필수입니다.")
    private Long sellerId;

    @NotNull(message = "구매자 ID는 필수입니다.")
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

    // 내 구매내역 조회 요청 DTO (예: 페이지/정렬 추가 기능)
    @Data
    @NoArgsConstructor
    public static class Purchases {
        @NotNull(message = "구매자 ID는 필수입니다.")
        private Long buyerId;
    }

}