package com.market.market_place.item.item_favorite;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ItemFavoriteResponse toggleFavorite(Long itemId, Long memberId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new Exception404("상품이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("회원이 존재하지 않습니다."));

        Optional<ItemFavorite> itemFavoriteOpt =
                itemFavoriteRepository.findByMemberIdAndItemId(member.getId(), item.getId());

        boolean liked;
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
            } catch (DataIntegrityViolationException e) {
                liked = true;
            }
        }

        Long itemFavoriteCount = itemFavoriteRepository.countByItemId(item.getId());
        notificationService.sendPostLike(item.getMember().getId().toString(), item.getTitle());
        return new ItemFavoriteResponse(item.getId(), liked, itemFavoriteCount);
    }

    @Transactional(readOnly = true)
    public ItemFavoriteResponse getFavoriteStatus(Long itemId, Long sessionUserId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new Exception404("상품이 존재하지 않습니다."));

        boolean liked = itemFavoriteRepository.findByMemberIdAndItemId(sessionUserId, itemId).isPresent();

        Long itemFavoriteCount = itemFavoriteRepository.countByItemId(itemId);

        return new ItemFavoriteResponse(itemId, liked, itemFavoriteCount);
    }
}