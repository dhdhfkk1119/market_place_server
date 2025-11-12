package markit.community.community_report_process;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityReportProcessRepository extends JpaRepository<CommunityReportProcess, Long> {

    @Query("select p from CommunityReportProcess p join fetch p.report r join fetch r.post")
    Page<CommunityReportProcess> findAllWithPost(Pageable pageable);
}
