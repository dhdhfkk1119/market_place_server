package com.market.market_place.item.item_report;

import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;

@Entity
public class ItemReportProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_report_id")
    private ItemReport itemReport;

    @ManyToOne()
    private Member member;


}
