package markit.community.community_topic;

import markit.community.community_category.CommunityCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "community_topic_tb")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommunityTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CommunityCategory category;

    public void update(CommunityTopicRequest.UpdateDTO updateDTO, CommunityCategory category){
        this.name = updateDTO.getName();
        this.category = category;
    }
}
