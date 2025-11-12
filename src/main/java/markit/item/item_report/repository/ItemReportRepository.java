package markit.item.item_report.repository;

import markit.item.item_report._enum.ItemReportStatus;
import markit.item.item_report.entity.ItemReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemReportRepository extends JpaRepository<ItemReport, Long> {
    Page<ItemReport> findByReporter_Id(Long reporterId, Pageable pageable);

    Optional<ItemReport> findByIdAndReporter_Id(Long id, Long reporterId);

    @Query("""
    SELECT r.item.id, r.status
    FROM ItemReport r
    WHERE r.item.id IN :itemIds
      AND r.createdAt = (
         SELECT MAX(r2.createdAt)
         FROM ItemReport r2
         WHERE r2.item.id = r.item.id
      )
""")
    List<Object[]> findLatestStatusRaw(@Param("itemIds") List<Long> itemIds);

    default Map<Long, ItemReportStatus> findLatestStatusByItemIds(List<Long> itemIds) {
        Map<Long, ItemReportStatus> map = new HashMap<>();
        for (Object[] row : findLatestStatusRaw(itemIds)) {
            map.put((Long) row[0], (ItemReportStatus) row[1]);
        }
        return map;
    }
}
