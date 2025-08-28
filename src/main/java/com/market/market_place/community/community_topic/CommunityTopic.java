package com.market.market_place.community.community_topic;

import com.market.market_place.community.community_category.CommunityCategory;
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

    public void update(CommunityTopicRequest.UpdateDTO updateDTO){
        this.name = updateDTO.getName();
    }

}
