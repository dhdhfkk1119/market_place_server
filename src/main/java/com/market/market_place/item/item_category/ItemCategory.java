package com.market.market_place.item.item_category;

import com.market.market_place.item.core.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item_category_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
