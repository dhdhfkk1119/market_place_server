package com.market.market_place.item.item_image;

import lombok.Data;

@Data
public class ItemImageRequestDTO {

    private String fileName;
    private String imageData;
    private String base64Image;

    public ItemImage toEntity() {

        ItemImage itemImage = new ItemImage();
        itemImage.setFileName(this.fileName);
        itemImage.setImageData(this.imageData);
        return itemImage;

    }


}
