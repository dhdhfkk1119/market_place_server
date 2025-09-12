package com.market.market_place.item.review;

import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.item.status.Trade;
import com.market.market_place.item.status.TradeRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
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
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException("거래를 찾을 수 없습니다."));
        Member reviewer = memberRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Member praisedMember = reviewer.equals(trade.getBuyer()) ? trade.getSeller() : trade.getBuyer();
        validateReviewer(trade, reviewer);
        TradeReview tradeReview = TradeReview.createReview(trade, reviewer, dto.getContent(), dto.getScore());
        praisedMember.addReviewScore(dto.getScore());
        trade.markAsReviewed(reviewer);


        tradeReviewRepository.save(tradeReview);
        memberRepository.save(praisedMember);
        tradeRepository.save(trade);
    }

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


    private void updateItemAverageRating(Item item) {
        Double averageRating = tradeRepository.findAverageRatingByItemId(item.getId());
        if (averageRating != null) {
            item.setAverageRating(averageRating);
            itemRepository.save(item);
        }
    }
}