package com.market.market_place.item.item_favorite;

import com.market.market_place.item.core.Item;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "item_favorite_tb",uniqueConstraints = @UniqueConstraint(columnNames = {"member_id","item_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

}
