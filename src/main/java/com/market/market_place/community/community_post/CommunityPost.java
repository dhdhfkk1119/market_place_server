package com.market.market_place.community.community_post;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.community.community_comment.CommunityComment;
import com.market.market_place.community.community_post_image.CommunityPostImage;
import com.market.market_place.community.community_post_like.CommunityPostLike;
import com.market.market_place.community.community_topic.CommunityTopic;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community_post_tb")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String location; // 임시 위치 정보

    @CreationTimestamp
    private Timestamp createdAt;

    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private int viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private CommunityTopic topic;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityPostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 순환 참조 방지
    private List<CommunityComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityPostLike> likes = new ArrayList<>();

    public boolean isOwner(Long checkMemberId) {
        return this.member.getId().equals(checkMemberId);
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void updateLikeCount(int count) {
        this.likeCount = Math.max(0, count); // 0 보다 작아지지 않도록 설정
    }

    public void update(CommunityPostRequest.UpdateDTO updateDTO) {
        this.title = updateDTO.getTitle();
        this.content = updateDTO.getContent();
        this.location = updateDTO.getLocation();
    }

    public String getTime(){
        return DateUtil.dateTimeFormat(createdAt);
    }

}
