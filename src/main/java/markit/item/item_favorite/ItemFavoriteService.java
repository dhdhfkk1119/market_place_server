package markit.item.item_favorite;

import markit._core._exception.Exception404;
import markit.item.core.Item;
import markit.item.core.ItemRepository;
import markit.members.domain.Member;
import markit.members.repositories.MemberRepository;
import markit.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemFavoriteService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ItemFavoriteRepository itemFavoriteRepository;
    private final NotificationService notificationService;

    @Transactional
    public ItemFavoriteResponse.StatusDTO toggleFavorite(Long itemId, Long memberId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new Exception404("상품이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("회원이 존재하지 않습니다."));

        Optional<ItemFavorite> itemFavoriteOpt =
                itemFavoriteRepository.findByMemberIdAndItemId(member.getId(), item.getId());

        boolean liked;
        boolean created = false;
        if (itemFavoriteOpt.isPresent()) {
            itemFavoriteRepository.delete(itemFavoriteOpt.get());
            liked = false;
        } else {
            ItemFavorite itemFavorite = new ItemFavorite();
            itemFavorite.setMember(member);
            itemFavorite.setItem(item);
            try {
                itemFavoriteRepository.save(itemFavorite);
                liked = true;
                created = true;
            } catch (DataIntegrityViolationException e) {
                liked = true;
            }
        }

        Long itemFavoriteCount = itemFavoriteRepository.countByItemId(item.getId());
        notificationService.sendPostLike(item.getMember().getId().toString(), item.getTitle());

        return new ItemFavoriteResponse.StatusDTO(item.getId(), liked, itemFavoriteCount);
    }

    @Transactional(readOnly = true)
    public ItemFavoriteResponse.StatusDTO getFavoriteStatus(Long itemId, Long sessionUserId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new Exception404("상품이 존재하지 않습니다."));

        boolean liked = itemFavoriteRepository.findByMemberIdAndItemId(sessionUserId, itemId).isPresent();

        Long itemFavoriteCount = itemFavoriteRepository.countByItemId(itemId);

        return new ItemFavoriteResponse.StatusDTO(itemId, liked, itemFavoriteCount);
    }

    @Transactional(readOnly = true)
    public Page<ItemFavoriteResponse.FavoriteItemDTO> getMyFavoriteItems(Long memberId, Pageable pageable) {

        Page<ItemFavorite> itemFavorites = itemFavoriteRepository.findByMemberId(memberId, pageable);

        return itemFavorites.map(favorite -> {
            Item item = favorite.getItem();
            Long favoriteCount = itemFavoriteRepository.countByItemId(item.getId());
            return new ItemFavoriteResponse.FavoriteItemDTO(
                    item.getId(),
                    item.getTitle(),
                    item.getThumbnailUrl(),
                    item.getPrice(),
                    // item.getTradeLocation(),
                    favoriteCount
            );
        });
    }

    @Transactional
    public void setPrimaryImage(Item item, Long imageId) {
        item.getImages().forEach(itemImage -> itemImage.setPrimary(itemImage.getId().equals(imageId)));
    }
}