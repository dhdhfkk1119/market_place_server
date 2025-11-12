package markit.item.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom, QuerydslPredicateExecutor<Item> {

    // 키워드와 태그로 상품 검색 (N+1 방지)
    @Query("SELECT DISTINCT i FROM Item i JOIN FETCH i.member m LEFT JOIN i.itemCategory c " +
            "WHERE (:keyword IS NULL OR i.title LIKE %:keyword% OR i.content LIKE %:keyword%) " +
            "AND (:tags IS NULL OR c.name IN :tags) ORDER BY i.createdAt DESC")
    List<Item> search(@Param("keyword") String keyword, @Param("tags") List<String> tags);

    // 상품 제목으로 상품 조회
    Optional<Item> findByTitle(String title);

    // 판매자 ID로 상품 조회
    Page<Item> findByMemberId(Long memberId, Pageable pageable);

    // 거래 생성을 위한 비관적 잠금(Pessimistic Lock)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Item i where i.id = :id")
    Optional<Item> findByIdWithLock(@Param("id") Long id);

    // 좌표로 거래 아이템 불러오기
    @Query(
            value = "SELECT * FROM Item i WHERE ST_Distance_Sphere(i.tradeLocation, ST_MakePoint(:lng, :lat)) <= :radius",
            nativeQuery = true
    )
    List<Item> findPlacesInRadius(
            @Param("lng") double lng,   // 중심점 경도
            @Param("lat") double lat,   // 중심점 위도
            @Param("radius") int radius  // 반경 (미터 단위)
    );
}
