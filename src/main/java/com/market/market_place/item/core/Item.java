package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_favorite.ItemFavorite;
import com.market.market_place.item.item_image.ItemImage;
import com.market.market_place.item.status.ItemStatus;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item_tb")
@Data
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id")
    private ItemCategory itemCategory;

    private String title;
    private String content;
    private Long price;
    private String tradeLocation;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;
    private Double averageRating;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> images = new ArrayList<>();
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemFavorite> favorites = new ArrayList<>();

    public void addImage(ItemImage image) {
        images.add(image);
        image.setItem(this);
    }

    public void removeImage(ItemImage image) {
        images.remove(image);
        image.setItem(null);
    }


    @Builder
    public Item(String content, List<ItemFavorite> favorites, ItemCategory itemCategory, Long price, String title, String tradeLocation) {
        this.content = content;
        this.favorites = favorites;
        this.itemCategory = itemCategory;
        this.tradeLocation = tradeLocation;
        this.price = price;
        this.title = title;
    }

    public void update(ItemRequest.ItemUpdateDTO dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.price = dto.getPrice();
    }
}
