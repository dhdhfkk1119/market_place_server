package com.market.market_place.item.status;

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
                .orElseThrow(() -> new RuntimeException("Item not found"));
        Member seller = memberRepository.findById(request.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        Member buyer = memberRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Trade trade = request.toEntity(item, seller, buyer);

        Trade savedTrade = tradeRepository.save(trade);

        return new TradeResponse(savedTrade);
    }
}