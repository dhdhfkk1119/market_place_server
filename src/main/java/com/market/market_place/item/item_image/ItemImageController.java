package com.market.market_place.item.item_image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ItemImageController {

    private final ItemImageService itemImageService;

    @PostMapping("/api/images")
    public ResponseEntity<ItemImageResponseDTO> uploadImage(
            @RequestBody ItemImageRequestDTO requestDTO) {

        ItemImageResponseDTO savedItemImageDTO = itemImageService.saveItemImage(requestDTO);
        return ResponseEntity.ok(savedItemImageDTO);
    }

    @GetMapping("/api/images")
    public ResponseEntity<List<ItemImageResponseDTO>> getAllItemImages() {
        List<ItemImageResponseDTO> itemImages = itemImageService.getAllItemImage();
        return ResponseEntity.ok(itemImages);
    }

    @GetMapping("/api/images/{id}")
    public ResponseEntity<ItemImageDetailResponseDTO> getItemDetailImage(@PathVariable Long id) {
        Optional<ItemImageDetailResponseDTO> itemImage = itemImageService.getItemImageDetailById(id);
        if (itemImage.isPresent()) {
            return ResponseEntity.ok(itemImage.get());
        } else  {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/images{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        itemImageService.deleteItemImage(id);
        return ResponseEntity.ok().build();
    }


}
