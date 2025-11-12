package markit.item.item_tag;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tag",uniqueConstraints = {
        @UniqueConstraint(name = "uk_tag_name", columnNames = "name_normalized")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 64)
    private String nameNormalized;

    @Column(nullable = false,length = 64)
    private String displayName;
}
