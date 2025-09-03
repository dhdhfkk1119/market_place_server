package com.market.market_place.community.community_post.community_post_tag;

import com.market.market_place.community.community_post.CommunityPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "community_post_tag")
public class CommunityPostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private CommunityPost post;

    private String name;

    @Builder
    public CommunityPostTag(CommunityPost post, String name){
        this.post = post;
        this.name = name;
    }
}
