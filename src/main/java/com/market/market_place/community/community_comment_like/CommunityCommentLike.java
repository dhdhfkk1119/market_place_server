package com.market.market_place.community.community_comment_like;

import com.market.market_place.community.community_comment.CommunityComment;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Builder
@Table(name = "community_comment_like_tb",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"comment_id", "member_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CommunityCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommunityComment comment;

    @CreationTimestamp
    private Timestamp createdAt;

}
