package com.market.market_place.item.item_image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemImageService {

    public final ItemImageRepository itemImageRepository;

    public ItemImageResponseDTO saveItemImage(ItemImageRequestDTO requestDTO) {
        ItemImage itemImage = requestDTO.toEntity();

        ItemImage savedImage = itemImageRepository.save(itemImage);

        return ItemImageResponseDTO.fromEntity(savedImage);
    }

    public List<ItemImageResponseDTO> getAllItemImage() {
        List<ItemImage> itemImages = itemImageRepository.findAll();

        List<ItemImageResponseDTO> dtoList = new ArrayList<>();
        for (ItemImage itemImage : itemImages) {
            dtoList.add(ItemImageResponseDTO.fromEntity(itemImage));
        }
        return dtoList;
    }

    public Optional<ItemImageDetailResponseDTO> getItemImageDetailById(Long id) {
        Optional<ItemImage> optionalItemImage = itemImageRepository.findById(id);
        if (optionalItemImage.isPresent()) {
            return Optional.of(ItemImageDetailResponseDTO.fromEntity(optionalItemImage.get()));
        } else {
            return Optional.empty();
        }
    }

    public void deleteItemImage(Long id) {
        itemImageRepository.deleteById(id);
    }

}
