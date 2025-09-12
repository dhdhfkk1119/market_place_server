package com.market.market_place.moderation.sanction.community_sanction;

import com.market.market_place.community.community_report.CommunityReport;
import com.market.market_place.members.domain.Member;
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

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "member_id",nullable = false,updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "report_id",nullable = false,updatable = false)
    private CommunityReport report;

    private String reason;


    private int sanctionCount;

    private LocalDateTime startAt;
    private LocalDateTime endAt;



}
