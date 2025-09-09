package com.market.market_place.community.community_report_process;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.community.community_report.CommunityReport;
import com.market.market_place.community.community_report.CommunityReportStatus;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "community_report_process_tb")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityReportProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    @JsonBackReference // 순환 참조 방지
    private CommunityReport report;

    @Enumerated(EnumType.STRING)
    private CommunityReportStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonIgnore // json 응답에서 제외
    private Member admin;

    @Column(length = 500)
    private String adminComment;

    @CreationTimestamp
    private Timestamp createdAt;

    public String getTime(){
        return DateUtil.dateTimeFormat(createdAt);
    }
}
