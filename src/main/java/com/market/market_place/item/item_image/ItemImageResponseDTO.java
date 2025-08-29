package com.market.market_place.item.item_image;

import lombok.Data;

@Data
public class ItemImageResponseDTO {

    private Long id;
    private String fileName;
    private String imageData;

    public static ItemImageResponseDTO fromEntity(ItemImage itemImage) {
        ItemImageResponseDTO dto = new ItemImageResponseDTO();
        dto.setId(itemImage.getId());
        dto.setFileName(itemImage.getFileName());
        dto.setImageData(itemImage.getImageData());
        return dto;
    }

}
