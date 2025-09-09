package com.market.market_place.community.community_report;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.community.community_post.CommunityPost;
import com.market.market_place.community.community_report_process.CommunityReportProcess;
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
@Table(name = "community_report_tb")
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

    // 신고 답변
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 순환 참조 방지
    private List<CommunityReportProcess> adminComments = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdAt;

    public String getTime() {
        return DateUtil.dateTimeFormat(createdAt);
    }
}
