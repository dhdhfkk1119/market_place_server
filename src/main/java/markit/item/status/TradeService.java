package markit.item.status;

import markit._core._exception.Exception400;
import markit._core._exception.Exception404;
import markit._core._utils.JwtUtil;
import markit.item.core.Item;
import markit.item.core.ItemRepository;
import markit.item.review.TradeReview;
import markit.item.review.TradeReviewRepository;
import markit.members.domain.Member;
import markit.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final TradeReviewRepository tradeReviewRepository;

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

        List<Long> tradeIds = trades.getContent().stream()
                .map(Trade::getId)
                .collect(Collectors.toList());

        List<TradeReview> buyerReviews = tradeIds.isEmpty()
                ? List.of()
                : tradeReviewRepository.findBuyerReviewsForTrades(tradeIds, buyerId);

        Map<Long, TradeReview> tradeIdToBuyerReview = buyerReviews.stream()
                .collect(Collectors.toMap(r -> r.getTrade().getId(), r -> r, (a, b) -> a));

        return trades.map(trade -> TradeResponse.PurchaseListItemDTO
                .fromPurchaseWithBuyerReview(trade, tradeIdToBuyerReview.get(trade.getId())));
    }

    // 단건 카드 조회 (구매자 본인만)
    @Transactional(readOnly = true)
    public TradeResponse.PurchaseListItemDTO getMyPurchaseCard(Long tradeId, Long buyerId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new Exception404("거래를 찾을 수 없습니다."));

        if (!trade.getBuyer().getId().equals(buyerId)) {
            throw new Exception400("본인의 구매내역만 조회할 수 있습니다.");
        }

        TradeReview buyerReview = tradeReviewRepository
                .findBuyerReviewForTrade(tradeId, buyerId)
                .orElse(null);

        return TradeResponse.PurchaseListItemDTO.fromPurchaseWithBuyerReview(trade, buyerReview);
    }
}