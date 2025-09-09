package com.market.market_place.item.item_report.repository;

import com.market.market_place.item.item_report.entity.ItemReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemReportRepository extends JpaRepository<ItemReport, Long> {
    Page<ItemReport> findByReporter_Id(Long reporterId, Pageable pageable);
    Optional<ItemReport> findByIdAndReporter_Id(Long id, Long reporterId);

}
