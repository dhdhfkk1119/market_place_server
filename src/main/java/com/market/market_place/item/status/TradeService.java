package com.market.market_place.item.status;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TradeResponse createTrade(Long itemId, JwtUtil.SessionUser sessionUser) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다"));

        Member buyer = memberRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("구매자를 찾을 수 없습니다"));


        tradeRepository.findByItem(item).ifPresent(t -> {
            if (t.getStatus() == TradeStatus.SOLD) {
                throw new RuntimeException("이미 거래가 완료 된 상품입니다");
            }
        });

        Trade trade = Trade.of(item, buyer);
        item.setStatus(TradeStatus.SOLD); // 판매 상태로 변경
        trade.setCompletedAt(Timestamp.valueOf(LocalDateTime.now())); // 현재 시간 저장
        trade.setStatus(TradeStatus.SOLD);
        itemRepository.save(item);
        Trade savedTrade = tradeRepository.save(trade);

        return new TradeResponse(savedTrade);
    }

    // 내 구매내역 조회
    @Transactional(readOnly = true)
    public Page<TradeResponse.MyTradeListItemDTO> getMyPurchases(Long buyerId, Pageable pageable) {
        Page<Trade> trades = tradeRepository.findByBuyerId(buyerId, pageable);
        return trades.map(TradeResponse.MyTradeListItemDTO::fromPurchase);
    }
}