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
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community_post_tb")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 모든 조회에서 deleted_at 이 null 인 행만 자동 조회되도록 전역 필터 적용
@Where(clause = "deleted_at is null")
public class CommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String location;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder.Default
    @Column(nullable = false)
    private int likeCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private int viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private CommunityTopic topic;

    // 관리자가 논리적인 삭제를 하기 위해 추가한 컬럼
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityPostImage> images = new ArrayList<>();

    @Builder.Default
    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityPostLike> likes = new ArrayList<>();

    public boolean isOwner(Long checkMemberId) {
        return this.member.getId().equals(checkMemberId);
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void updateLikeCount(int count) {
        this.likeCount = Math.max(0, count);
    }

    public void update(CommunityPostRequest.UpdateDTO updateDTO) {
        this.title = updateDTO.getTitle();
        this.content = updateDTO.getContent();
        this.location = updateDTO.getLocation();
    }

    public String getTime() {
        return DateUtil.timestampFormat(createdAt);
    }

}
