package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_image.ItemImage;
import com.market.market_place.members.domain.MemberAddress;
import lombok.Data;

import java.util.List;

public class ItemRequest {

    @Data
    public static class ItemSaveDTO {

        private Long itemCategoryId;
        private Long memberAddressId;
        private String title;
        private String content;
        private Long price;
        private List<String> imageUrls;


        public Item toEntity(ItemCategory itemCategory, MemberAddress memberAddress) {
            return Item.builder()
                    .itemCategory(itemCategory)
                    .memberAddress(memberAddress)
                    .title(this.title)
                    .content(this.content)
                    .price(this.price)
                    .build();
        }
    }

    @Data
    public static class ItemUpdateDTO {

        private Long memberAddressId;
        private String title;
        private String content;
        private Long price;
        private List<String> imageUrls;
    }


}
