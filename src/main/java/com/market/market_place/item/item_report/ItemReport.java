package com.market.market_place.item.item_report;

import com.market.market_place.item.core.Item;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "item_report_tb")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member reporter;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ItemReportStatus status = ItemReportStatus.PENDING;

    @CreationTimestamp
    private Timestamp createdAt;

}
