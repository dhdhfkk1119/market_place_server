package com.market.market_place.community.community_report;

import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.community.community_post.CommunityPost;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "community_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신고자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    // 신고 대상 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private CommunityPost post;

    // 신고 사유
    @Column(nullable = false, length = 500)
    private String reason;

    // 신고 상태
    @Enumerated(EnumType.STRING)
    private CommunityReportStatus status = CommunityReportStatus.PENDING;

    @CreationTimestamp
    private Timestamp createdAt;

    public String getTime() {
        return DateUtil.dateTimeFormat(createdAt);
    }
}
