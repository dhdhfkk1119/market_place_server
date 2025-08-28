package com.market.market_place.item.item_image;

import lombok.Data;

@Data
public class ItemImageDetailResponseDTO {

    private Long id;
    private String fileName;
    private String imageData;

    public static ItemImageDetailResponseDTO fromEntity(ItemImage itemImage) {
        ItemImageDetailResponseDTO dto = new ItemImageDetailResponseDTO();
        dto.setId(itemImage.getId());
        dto.setFileName(itemImage.getFileName());
        dto.setImageData(itemImage.getImageData());
        return dto;
    }
}
