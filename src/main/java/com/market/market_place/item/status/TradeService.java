package com.market.market_place.item.status;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception404;
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

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TradeResponse createTrade(Long itemId, JwtUtil.SessionUser sessionUser) {

        // 1. 엔티티 조회 (Pessimistic Lock으로 동시성 문제 방지)
        Item item = itemRepository.findByIdWithLock(itemId)
                .orElseThrow(() -> new Exception404("상품을 찾을 수 없습니다."));

        Member buyer = memberRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception404("구매자를 찾을 수 없습니다."));

        // 2. 거래 유효성 검사
        if (item.getStatus() != TradeStatus.ON_SALE) {
            throw new Exception400("현재 거래할 수 없는 상품입니다.");
        }

        if (item.getMember().getId().equals(buyer.getId())) {
            throw new Exception400("자신의 상품은 구매할 수 없습니다.");
        }

        // 3. 상품 및 거래 상태 변경
        item.setStatus(TradeStatus.SOLD);
        itemRepository.save(item); // 변경된 상태를 명시적으로 저장 (가독성 향상)
        Trade trade = Trade.of(item, buyer);
        trade.completeTrade(); // 거래 완료 처리
        Trade savedTrade = tradeRepository.save(trade);

        // 4. 응답 반환
        return new TradeResponse(savedTrade);
    }

    // 내 구매내역 조회
    @Transactional(readOnly = true)
    public Page<TradeResponse.PurchaseListItemDTO> getMyPurchases(Long buyerId, Pageable pageable) {
        Page<Trade> trades = tradeRepository.findByBuyerId(buyerId, pageable);
        return trades.map(TradeResponse.PurchaseListItemDTO::fromPurchase);
    }
}