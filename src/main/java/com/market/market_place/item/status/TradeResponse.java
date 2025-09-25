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
        this.sellerId = trade.getItem().getMember().getId();
        this.buyerId = trade.getBuyer().getId();
        this.buyerReviewed = trade.isBuyerReviewed();
        this.sellerReviewed = trade.isSellerReviewed();
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class PurchaseListItemDTO {
        private Long tradeId;
        private Long itemId;
        private String title;
        private Long price;
        private String thumbnailUrl;
        private String tradedAt;
        private TradeStatus tradeStatus;
        private String tradeStatusLabel;
        private Long sellerId;
        private String sellerName;
        private boolean isReviewed;

        public static PurchaseListItemDTO fromPurchase(Trade trade) {
            Item item = trade.getItem();
            Member seller = item.getMember();
            return PurchaseListItemDTO.builder()
                    .tradeId(trade.getId())
                    .itemId(item.getId())
                    .title(item.getTitle())
                    .price(item.getPrice())
                    .thumbnailUrl(item.getThumbnailUrl()) // 썸네일 URL이 없다면 기본 이미지 URL 제공 고려
                    .tradedAt(trade.getTime())
                    .tradeStatus(trade.getStatus())
                    .tradeStatusLabel(toStatusLabel(trade.getStatus()))
                    .sellerId(seller.getId())
                    .sellerName(seller.getMemberProfile().getName())
                    .isReviewed(trade.isBuyerReviewed())
                    .build();

        }

        private static String toStatusLabel(TradeStatus status) {
            if (status == null) return "알수없음";
            return switch (status) {
                case SOLD -> "거래완료";
                case PENDING -> "거래중";
                default -> "알수없음";
            };
        }

    }
}