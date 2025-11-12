package markit.item.praise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PraiseRepository extends JpaRepository<Praise, Long>, QuerydslPredicateExecutor<Praise> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM praise_tb p WHERE p.praiser_id = :praiserId AND p.trade_id = :tradeId",
            nativeQuery = true)
    boolean existsByPraiserIdAndTradeId(@Param("praiserId") Long praiserId, @Param("tradeId") Long tradeId);
}