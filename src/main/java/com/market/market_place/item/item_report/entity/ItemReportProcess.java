package com.market.market_place.item.item_report.entity;

import com.market.market_place.item.item_report._enum.ProcessResult;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_report_process_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemReportProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_report_id", nullable = false)
    private ItemReport itemReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Column(name = "process_date", nullable = false)
    private LocalDateTime processDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessResult result;

}
