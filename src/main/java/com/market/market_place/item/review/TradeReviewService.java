package com.market.market_place.item.review;

import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.item.status.Trade;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.item.status.Trade;
import com.market.market_place.item.status.TradeRepository;
import com.market.market_place.item.review.TradeReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeReviewService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final TradeReviewRepository tradeReviewRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void createReview(Long tradeId, Long reviewerId, TradeReviewRequest dto) {
        // 1. 거래 및 회원 엔티티 조회
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException("거래를 찾을 수 없습니다."));
        Member reviewer = memberRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 2. 후기 작성 권한 및 중복 여부 확인
        validateReviewer(trade, reviewer);

        // 3. TradeReview 엔티티 생성 및 저장
        TradeReview review = TradeReview.builder()
                .trade(trade)
                .reviewer(reviewer)
                .content(dto.getContent())
                .rating(dto.getRating())
                .build();
        tradeReviewRepository.save(review);

        // 4. 아이템 평점 업데이트
        updateItemAverageRating(trade.getItem());
    }

    // 후기 작성 권한 검증 및 중복 체크
    private void validateReviewer(Trade trade, Member reviewer) {
        if (reviewer.getId().equals(trade.getBuyer().getId())) {
            if (trade.isBuyerReviewed()) {
                throw new IllegalStateException("구매자 후기는 이미 작성되었습니다.");
            }
            trade.setBuyerReviewed(true);
        } else if (reviewer.getId().equals(trade.getSeller().getId())) {
            if (trade.isSellerReviewed()) {
                throw new IllegalStateException("판매자 후기는 이미 작성되었습니다.");
            }
            trade.setSellerReviewed(true);
        } else {
            throw new IllegalArgumentException("후기를 작성할 권한이 없습니다.");
        }
    }

    // 아이템의 평균 평점 업데이트
    private void updateItemAverageRating(Item item) {
        Double averageRating = tradeReviewRepository.findAverageRatingByTradeId(item.getId());
        if (averageRating != null) {
            item.setAverageRating(averageRating);
            itemRepository.save(item);
        }
    }
}