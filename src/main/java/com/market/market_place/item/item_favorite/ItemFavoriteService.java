package com.market.market_place.item.item_favorite;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemFavoriteService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ItemFavoriteRepository itemFavoriteRepository;

    public ItemFavoriteResponse toggleFavorite(Long itemId,Long sessionUserId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new Exception404("상품이 존재하지 않습니다."));
        Member member = memberRepository.findById(sessionUserId)
                .orElseThrow(() -> new Exception404("회원이 존재하지 않습니다."));

        Optional<ItemFavorite> ItemFavoriteOpt = itemFavoriteRepository.findByMemberIdAndItemId(member.getId(),item.getId());

        boolean liked;
        if (ItemFavoriteOpt.isPresent()) {
            itemFavoriteRepository.delete(ItemFavoriteOpt.get());
            liked = false;
        } else {
            ItemFavorite itemFavorite = new ItemFavorite();
            itemFavorite.setMember(member);
            itemFavorite.setItem(item);
            itemFavoriteRepository.save(itemFavorite);
            liked = true;
        }

        Long ItemFavoriteCount = itemFavoriteRepository.countByItemId(item.getId());

        return new ItemFavoriteResponse(item.getId(),liked,ItemFavoriteCount);
    }

    public ItemFavoriteResponse getFavoriteStatus(Long itemId,Long sessionUserId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new Exception404("상품이 존재하지 않습니다."));

        boolean liked = itemFavoriteRepository.findByMemberIdAndItemId(sessionUserId,itemId).isPresent();

        Long itemFavoriteCount = itemFavoriteRepository.countByItemId(itemId);

        return new ItemFavoriteResponse(itemId,liked,itemFavoriteCount);
    }
}
