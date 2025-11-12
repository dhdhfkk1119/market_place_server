package markit.item.item_image;

import markit.item.core.Item;
import lombok.Data;

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
