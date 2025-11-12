package markit.item.item_favorite;

import markit.item.core.Item;
import markit.members.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemFavoriteRepository extends JpaRepository<ItemFavorite, Long> {
    Optional<ItemFavorite> findByMemberIdAndItemId(Long memberId, Long itemId);

    Long countByItemId(Long itemId);

    boolean existsByItemAndMember(Item item, Member member);
    Page<ItemFavorite> findByMemberId(Long memberId, Pageable pageable);
}
