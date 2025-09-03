package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_favorite.ItemFavorite;
import com.market.market_place.item.item_image.ItemImage;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberAddress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

    //    private Long status; // 판매상태 엔티티 생성 시 타입 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;// 판매자 정보 (user 엔티티 생성 시 타입 변경)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id")
    private ItemCategory itemCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_address_id")
    private MemberAddress memberAddress;
//    @Enumerated(EnumType.STRING)
//    private ItemStatus status;



    private String title;
    private String content;
    private Long price;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ItemImage> images = new ArrayList<>();
    @OneToMany(mappedBy = "item",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ItemFavorite> favorites = new ArrayList<>();

    public void addImage(ItemImage image) {
        images.add(image); image.setItem(this); }
    public void removeImage(ItemImage image) {
        images.remove(image); image.setItem(null); }



    @Builder
    public Item(String content, List<ItemFavorite> favorites,ItemCategory itemCategory,MemberAddress memberAddress, Long price, String title) {
        this.content = content;
        this.favorites = favorites;
        this.itemCategory = itemCategory;
        this.memberAddress = memberAddress;
        this.price = price;
        this.title = title;
    }

    public void update(ItemRequest.ItemUpdateDTO dto, MemberAddress address) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.price = dto.getPrice();
        this.memberAddress = address;
    }
}
