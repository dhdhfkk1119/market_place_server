package com.market.market_place.item.item_favorite;

import com.market.market_place.item.core.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "item_favorite_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // User 엔티티 추가 시 변경

//    private Item item;

}
