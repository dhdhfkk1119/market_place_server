package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_favorite.ItemFavorite;
import com.market.market_place.item.item_image.ItemImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "item_tb")
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private Long price;

    private String status; // 판매상태 엔티티 생성 시 타입 변경
    private Long seller; // 판매자 정보 (user 엔티티 생성 시 타입 변경)

//    private ItemCategory itemCategory;
//
//    private List<ItemImage> images = new ArrayList<>();
//    private List<ItemFavorite> favorites = new ArrayList<>();

}
