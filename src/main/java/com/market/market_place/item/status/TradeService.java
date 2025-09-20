package com.market.market_place.item.status;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TradeResponse createTrade(TradeRequest request) {
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다"));
        Member seller = memberRepository.findById(request.getSellerId())
                .orElseThrow(() -> new RuntimeException("판매자를 찾을 수 없습니다"));
        Member buyer = memberRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new RuntimeException("구매자를 찾을 수 없습니다"));

        Trade trade = request.toEntity(item, seller, buyer);

        Trade savedTrade = tradeRepository.save(trade);

        return new TradeResponse(savedTrade);
    }

    // 내 구매내역 조회
    @Transactional(readOnly = true)
    public List<TradeResponse.MyTradeListItemDTO> getMyPurchases(Long buyerId) {
        List<Trade> trades = tradeRepository.findByBuyerId(buyerId);
        return trades.stream()
                .map(TradeResponse.MyTradeListItemDTO::fromPurchase)
                .toList();
    }
}