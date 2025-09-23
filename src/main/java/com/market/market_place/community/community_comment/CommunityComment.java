package com.market.market_place.community.community_comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.community.community_comment_like.CommunityCommentLike;
import com.market.market_place.community.community_post.CommunityPost;
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
@Data
@Table(name = "community_comment_tb")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference // 순환 참조 방지
    private CommunityPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @Builder.Default
    @Column(nullable = false)
    private int likeCount = 0;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityCommentLike> likes = new ArrayList<>();

    public boolean isOwner(Long sessionId){
        return this.member.getId().equals(sessionId);
    }

    public void update(CommunityCommentRequest.UpdateDTO updateDTO) {
        this.content = updateDTO.getContent();
    }

    public void updateLikeCount(int count) {
        this.likeCount = Math.max(0, count);
    }

    // 수정시간이 10초 이상 차이나면 수정됨
    public boolean isModified(){
        return this.updatedAt != null && (this.updatedAt.getTime() - this.createdAt.getTime()> 10000);
    }

    public String getCreateTime(){
        return DateUtil.timestampFormat(createdAt);
    }

    public String getUpdateTime(){
        return DateUtil.timestampFormat(updatedAt);
    }
}
