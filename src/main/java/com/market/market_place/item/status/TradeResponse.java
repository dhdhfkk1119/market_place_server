package com.market.market_place.item.status;

import com.market.market_place.item.core.Item;
import com.market.market_place.members.domain.Member;
import lombok.*;

import java.sql.Timestamp;

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

    @Data
    @AllArgsConstructor
    @Builder
    public static class MyTradeListItemDTO {
        private Long id;
        private Long itemId;
        private String title;
        private Long price;
        private String thumbnailUrl;
        private String completedAt;
        private String statusLabel;
        private TradeStatus status;
        private Long counterPartyId;
        private String counterPartyName;

        public static MyTradeListItemDTO fromPurchase(Trade trade) {
            Item item = trade.getItem();
            Member seller = trade.getSeller();
            return MyTradeListItemDTO.builder()
                    .id(trade.getId())
                    .itemId(item.getId())
                    .title(item.getTitle())
                    .price(item.getPrice())
                    .thumbnailUrl(item.getThumbnailUrl())
                    .completedAt(trade.getTime())
                    .statusLabel(toStatusLabel(trade.getStatus()))
                    .status(trade.getStatus())
                    .counterPartyId(seller.getId())
                    .counterPartyName(seller.getMemberProfile().getName())
                    .build();

        }

        private static String toStatusLabel(TradeStatus status) {
            if (status == null) return "알수없음";
            return switch (status) {
                case SOLD -> "거래완료";
                case PENDING -> "거래 진행중";
                default -> "알수없음";
            };
        }

    }
}