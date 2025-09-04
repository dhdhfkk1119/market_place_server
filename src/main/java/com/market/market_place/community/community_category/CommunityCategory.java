package com.market.market_place.community.community_category;

import com.market.market_place.community.community_topic.CommunityTopic;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "community_category_tb")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityTopic> communityTopics = new ArrayList<>();

    public void update(CommunityCategoryRequest.UpdateDTO updateDTO){
       this.name = updateDTO.getName();
    }
}
