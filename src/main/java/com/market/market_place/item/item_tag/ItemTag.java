package com.market.market_place.item.item_tag;

import com.market.market_place.item.core.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_tag", uniqueConstraints = {
        @UniqueConstraint(name = "uk_item_tag", columnNames = {"item_id","tag_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}
