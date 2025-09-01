package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.members.domain.MemberAddress;
import lombok.Builder;
import lombok.Data;

public class ItemResponse {

    @Data
    public static class ItemDetailDTO {
        //이미지 거래방식 추가 필요
        private Long itemCategoryId;
        private Long memberAddressId;
        private String title;
        private String content;
        private Long price;

        @Builder
        public ItemDetailDTO(Item item) {
            this.content = item.getContent();
            this.itemCategoryId = item.getItemCategory().getId();
            this.memberAddressId = item.getMemberAddress().getId();
            this.price = item.getPrice();
            this.title = item.getTitle();
        }
    }
    @Data
    public static class ItemSaveDTO {
        //이미지 거래방식 추가 필요
        private Long itemCategoryId;
        private Long memberAddressId;
        private String title;
        private String content;
        private Long price;

        @Builder
        public ItemSaveDTO(Item item) {
            this.content = item.getContent();
            this.itemCategoryId = item.getItemCategory().getId();
            this.memberAddressId = item.getMemberAddress().getId();
            this.price = item.getPrice();
            this.title = item.getTitle();
        }
    }


}
