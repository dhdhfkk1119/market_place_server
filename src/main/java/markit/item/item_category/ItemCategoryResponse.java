package markit.item.item_category;

import lombok.Builder;
import lombok.Data;

public class ItemCategoryResponse {

    @Data
    public static class ItemCategoryDetailDTO {
        private Long id;
        private String title;

        @Builder
        public ItemCategoryDetailDTO(ItemCategory itemCategory) {
            this.id = itemCategory.getId();
            this.title = itemCategory.getName();
        }
    }

    @Data
    @Builder
    public static class ItemCategoryListDTO {
        private Long id;
        private String name;

        public static ItemCategoryListDTO fromEntity(ItemCategory itemCategory) {
            return ItemCategoryListDTO.builder()
                    .id(itemCategory.getId())
                    .name(itemCategory.getName())
                    .build();
        }
    }

    @Data
    public static class ItemCategorySaveDTO {
        private String name;

        @Builder
        public ItemCategorySaveDTO(ItemCategory itemCategory) {
            this.name = itemCategory.getName();
        }
    }

    @Data
    public static class ItemCategoryUpdateDTO {
        private String name;

        @Builder
        public ItemCategoryUpdateDTO(ItemCategory itemCategory) {
            this.name = itemCategory.getName();
        }
    }

}
