package markit.moderation.sanction.community_sanction;

import markit.community.community_report.CommunityReport;
import markit.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "community_sanction_tb")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunitySanction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false, updatable = false)
    private CommunityReport report;

    @Column(length = 500)
    private String reason;

    @Column(nullable = false)
    private int sanctionCount;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
