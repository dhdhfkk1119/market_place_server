package com.market.market_place.item.status;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TradeResponse createTrade(TradeRequest request) {
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new Exception404("아이템을 찾을 수 없습니다."));

        Member seller = memberRepository.findById(request.getSellerId())
                .orElseThrow(() -> new Exception404("판매자를 찾을 수 없습니다."));

        Member buyer = memberRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new Exception404("구매자를 찾을 수 없습니다."));


        if (item.getStatus() != ItemStatus.ON_SALE) {
            throw new IllegalArgumentException("판매 중인 아이템이 아닙니다.");
        }

        item.setStatus(ItemStatus.SOLD);
        itemRepository.save(item);

        Trade trade = Trade.builder()
                .item(item)
                .seller(seller)
                .buyer(buyer)
                .buyerReviewed(false)
                .sellerReviewed(false)
                .build();

        Trade savedTrade = tradeRepository.save(trade);

        return new TradeResponse(savedTrade);
    }
}