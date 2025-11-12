package markit.item.status;

import markit.item.core.Item;
import markit.item.review.TradeReview;
import markit.members.domain.Member;
import lombok.*;

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
        private TradeStatus itemStatus; // 상품 상태 (ItemStatus -> TradeStatus로 변경)
        private String itemStatusLabel; // 상품 상태 라벨
        private TradeStatus tradeStatus; // 거래 상태
        private String tradeStatusLabel; // 거래 상태 라벨
        private Long sellerId;
        private String sellerName;
        private boolean isReviewed;

        // 추가된 리뷰 정보
        private Long reviewId;
        private String reviewContent;
        private Double reviewRating;

        public static PurchaseListItemDTO fromPurchase(Trade trade) {
            Item item = trade.getItem();
            Member seller = item.getMember();

            // 구매자가 작성한 리뷰를 찾음
            TradeReview buyerReview = trade.getReviews().stream()
                    .filter(review -> review.getReviewer().getId().equals(trade.getBuyer().getId()))
                    .findFirst()
                    .orElse(null);

            return PurchaseListItemDTO.builder()
                    .tradeId(trade.getId())
                    .itemId(item.getId())
                    .title(item.getTitle())
                    .price(item.getPrice())
                    .thumbnailUrl(item.getThumbnailUrl()) // 썸네일 URL이 없다면 기본 이미지 URL 제공 고려
                    .tradedAt(trade.getTime())
                    .itemStatus(item.getStatus())
                    .itemStatusLabel(item.getStatus() != null ? item.getStatus().getLabel() : "알수없음")
                    .tradeStatus(trade.getStatus())
                    .tradeStatusLabel(trade.getStatus() != null ? trade.getStatus().getLabel() : "알수없음")
                    .sellerId(seller.getId())
                    .sellerName(seller.getMemberProfile().getName())
                    .isReviewed(buyerReview != null) // 리뷰 존재 여부로 판단
                    // 리뷰가 존재할 경우, 리뷰 정보 추가
                    .reviewId(buyerReview != null ? buyerReview.getId() : null)
                    .reviewContent(buyerReview != null ? buyerReview.getContent() : null)
                    .reviewRating(buyerReview != null ? buyerReview.getRating() : null)
                    .build();

        }

        // 외부에서 미리 로딩한 구매자 리뷰를 주입하여 DTO 생성
        public static PurchaseListItemDTO fromPurchaseWithBuyerReview(Trade trade, TradeReview buyerReview) {
            Item item = trade.getItem();
            Member seller = item.getMember();

            return PurchaseListItemDTO.builder()
                    .tradeId(trade.getId())
                    .itemId(item.getId())
                    .title(item.getTitle())
                    .price(item.getPrice())
                    .thumbnailUrl(item.getThumbnailUrl())
                    .tradedAt(trade.getTime())
                    .itemStatus(item.getStatus())
                    .itemStatusLabel(item.getStatus() != null ? item.getStatus().getLabel() : "알수없음")
                    .tradeStatus(trade.getStatus())
                    .tradeStatusLabel(trade.getStatus() != null ? trade.getStatus().getLabel() : "알수없음")
                    .sellerId(seller.getId())
                    .sellerName(seller.getMemberProfile().getName())
                    .isReviewed(buyerReview != null)
                    .reviewId(buyerReview != null ? buyerReview.getId() : null)
                    .reviewContent(buyerReview != null ? buyerReview.getContent() : null)
                    .reviewRating(buyerReview != null ? buyerReview.getRating() : null)
                    .build();
        }

    }
}