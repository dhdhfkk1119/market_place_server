package com.market.market_place.item.item_category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "item_category_tb")
@Entity
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    private List<Item> items = new ArrayList<>();

    public void update(ItemCategoryRequest.UpdateDTO updateDTO) {
        this.name = updateDTO.getName();
    }

}
