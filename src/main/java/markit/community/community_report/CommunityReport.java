package markit.community.community_report;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import markit._core._utils.DateUtil;
import markit.community.community_post.CommunityPost;
import markit.community.community_report_process.CommunityReportProcess;
import markit.members.domain.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private CommunityPost post;

    @Column(nullable = false, length = 500)
    private String reason;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private CommunityReportStatus status = CommunityReportStatus.PENDING;

    @Builder.Default
    @JsonManagedReference
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityReportProcess> adminComments = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdAt;

    public String getTime() {
        return DateUtil.timestampFormat(createdAt);
    }
}
