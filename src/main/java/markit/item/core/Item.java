package markit.item.core;

import markit._core._utils.DateUtil;
import markit.item.item_category.ItemCategory;
import markit.item.item_favorite.ItemFavorite;
import markit.item.item_image.ItemImage;
import markit.item.item_tag.ItemTag;
import markit.item.item_tag.Tag;
import markit.item.status.TradeStatus;
import markit.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item_tb")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id")
    private ItemCategory itemCategory;

    private String title;
    private String content;
    private Long price;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point tradeLocation;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(nullable = false)
    @Builder.Default
    private Long viewCount = 0L;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemFavorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemTag> itemTags = new ArrayList<>();

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void addItemImage(List<ItemImage> images) {
        this.images.clear();
        for (ItemImage image : images) {
            if (image != null) {
                this.images.add(image);
                image.setItem(this);
            }
        }
    }

    public void addTag(Tag tag) {
        ItemTag link = ItemTag.builder()
                .item(this)
                .tag(tag)
                .build();
        this.itemTags.add(link);
    }

    @PrePersist
    public void PrePersist() {
        if (averageRating == null) {
            averageRating = 2.5;
        }
    }

    public void update(ItemRequest.ItemUpdateDTO dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.price = dto.getPrice();
        this.tradeLocation = dto.getTradeLocation();
    }

    public String getThumbnailUrl() {
        return images.stream()
                .filter(ItemImage::isPrimary)
                .findFirst()
                .map(ItemImage::getImageUrl)
                .orElseGet(() -> images.stream()
                        .sorted((o1, o2) -> Integer.compare(o1.getOrderIndex(), o2.getOrderIndex()))
                        .findFirst()
                        .map(ItemImage::getImageUrl)
                        .orElse(null));
    }

    public String getTime() {
        return DateUtil.timestampFormat(createdAt);
    }

}