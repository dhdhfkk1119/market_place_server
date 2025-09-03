package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_favorite.ItemFavorite;
import com.market.market_place.item.item_image.ItemImage;
import com.market.market_place.item.status.ItemStatus;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberAddress;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String title;
    private String content;
    private Long price;

    @Column(name = "trade_location", length = 255)
    private String tradeLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;// 판매자 정보 (user 엔티티 생성 시 타입 변경)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id")
    private ItemCategory itemCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_address_id")
    private MemberAddress memberAddress;
    @Enumerated(EnumType.STRING)
    private ItemStatus status;
    private Double averageRating;


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
    public Item(String content,MemberAddress memberAddress, List<ItemFavorite> favorites, ItemCategory itemCategory,Long price, String title,String tradeLocation,Member member,ItemStatus status) {
        this.content = content;
        this.favorites = favorites;
        this.itemCategory = itemCategory;
        this.tradeLocation = tradeLocation;
        this.member = member;
        this.price = price;
        this.title = title;
        this.status = status;
        this.memberAddress = memberAddress;
    }

    public void update(ItemRequest.ItemUpdateDTO dto, MemberAddress address) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.price = dto.getPrice();
        this.memberAddress = address;
    }
}
