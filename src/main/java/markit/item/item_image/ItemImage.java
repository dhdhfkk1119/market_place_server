package markit.item.item_image;

import markit.item.core.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_image_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id",nullable = false)
    private Item item;

    @Column(nullable = false)
    @Builder.Default
    private boolean isPrimary = false;

    @Column(nullable = false)
    @Builder.Default
    private int orderIndex = 0;

    public static ItemImage of(String url) {
        return ItemImage.builder().imageUrl(url).build();
    }
}
