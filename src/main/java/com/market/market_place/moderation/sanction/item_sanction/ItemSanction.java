package com.market.market_place.moderation.sanction.item_sanction;

import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.item.item_report.entity.ItemReport;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_sanction_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSanction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false, updatable = false)
    private ItemReport report;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemSanctionType type;

    @Column(nullable = false)
    private int acceptedCountAtCreation;

    @Column(length = 500)
    private String reason;

    private LocalDateTime startAt;
    private LocalDateTime endAt;


    @Column(nullable = false)
    private boolean active;

    public boolean isActiveNow(LocalDateTime now) {
        if (!active) return false;
        if (type == ItemSanctionType.PERM_BAN) return true;
        return endAt != null && now.isBefore(endAt);
    }

    public void deactivate() {
        this.active = false;
    }

    public String getTime(){
        return DateUtil.localDateTimeFormat(endAt);
    }
}
