package com.market.market_place.community.community_comment;

import com.market.market_place.community.community_comment_like.CommunityCommentLike;
import com.market.market_place.community.community_post.CommunityPost;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private CommunityPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private int likeCount = 0;

    @CreationTimestamp
    private Timestamp createdAt;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityCommentLike> likes = new ArrayList<>();

    public boolean isOwner(Long sessionId){
        return this.member.getId().equals(sessionId);
    }

    // update 메서드
    public void update(CommunityCommentRequest.UpdateDTO updateDTO) {
        this.content = updateDTO.getContent();
    }

    public void updateLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
