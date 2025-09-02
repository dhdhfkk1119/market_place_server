package com.market.market_place.item.item_image;

import com.market.market_place.item.core.Item;
import lombok.Data;

import java.awt.*;

@Data
public class ItemImageRequestDTO {

    private String imageUrl;

    public ItemImage toEntity(Item item) {
        return ItemImage.builder()
                .item(item)
                .imageUrl(this.imageUrl)
                .build();
    }
}
