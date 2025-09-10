package com.market.market_place.item.item_report.repository;

import com.market.market_place.item.item_report.entity.ItemReport;
import com.market.market_place.item.item_report.entity.ItemReportProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemReportProcessRepository extends JpaRepository<ItemReportProcess,Long> {
    @Override
    Page<ItemReportProcess> findAll(Pageable pageable);

    Optional<ItemReportProcess> findByItemReportId(Long reportId);
}
