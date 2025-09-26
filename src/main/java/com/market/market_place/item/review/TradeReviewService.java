package com.market.market_place.item.review;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.item.status.Trade;
import com.market.market_place.item.status.TradeRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// 거래 리뷰 서비스 (비즈니스 로직)
@Service
@RequiredArgsConstructor
public class TradeReviewService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final TradeReviewRepository tradeReviewRepository;
    private final ItemRepository itemRepository;

    // 리뷰 작성
    @Transactional
    public TradeReviewResponse createReview(Long reviewerId, TradeReviewRequest dto) {
        // 1. 거래 및 회원 엔티티 조회
        Trade trade = tradeRepository.findById(dto.getTradeId())
                .orElseThrow(() -> new Exception404("거래를 찾을 수 없습니다."));
        Member reviewer = memberRepository.findById(reviewerId)
                .orElseThrow(() -> new Exception404("회원을 찾을 수 없습니다."));

        // 2. 후기 작성 권한 및 중복 여부 확인
        validateReviewer(trade, reviewer);

        // 3. TradeReview 엔티티 생성 및 저장
        TradeReview review = dto.toEntity(trade, reviewer);
        TradeReview savedReview = tradeReviewRepository.save(review);

        // 4. 아이템 평점 업데이트
        updateItemAverageRating(trade.getItem());

        return TradeReviewResponse.from(savedReview);
    }

    // 후기 작성 권한 검증 및 중복 체크
    private void validateReviewer(Trade trade, Member reviewer) {
        if (reviewer.getId().equals(trade.getBuyer().getId())) {
            if (trade.isBuyerReviewed()) {
                throw new Exception400("구매자 후기는 이미 작성되었습니다.");
            }
            trade.setBuyerReviewed(true);
        } else if (reviewer.getId().equals(trade.getItem().getMember().getId())) {
            if (trade.isSellerReviewed()) {
                throw new Exception400("판매자 후기는 이미 작성되었습니다.");
            }
            trade.setSellerReviewed(true);
        } else {
            throw new Exception403("후기를 작성할 권한이 없습니다.");
        }
    }

    // 아이템의 평균 평점 업데이트
    private void updateItemAverageRating(Item item) {
        Double averageRating = tradeReviewRepository.findAverageRatingByItemId(item.getId());
        if (averageRating != null) {
            item.setAverageRating(averageRating);

        }
    }


    // 리뷰 수정
    @Transactional
    public TradeReviewResponse updateReview(Long reviewId, TradeReviewRequest dto, Long reviewerId) {
        TradeReview review = tradeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception404("리뷰를 찾을 수 없습니다."));
        if (!review.getReviewer().getId().equals(reviewerId)) {
            throw new IllegalArgumentException("본인만 리뷰를 수정할 수 있습니다.");
        }
        review.update(dto.getContent(), (int)dto.getRating());

        // 아이템 평점 업데이트
        updateItemAverageRating(review.getTrade().getItem());

        return TradeReviewResponse.from(review);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, Long reviewerId) {
        TradeReview review = tradeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception404("리뷰를 찾을 수 없습니다."));
        if (!review.getReviewer().getId().equals(reviewerId)) {
            throw new IllegalArgumentException("본인만 리뷰를 삭제할 수 있습니다.");
        }

        // 삭제 전에 Trade의 정보를 업데이트
        Trade trade = review.getTrade();
        Member reviewer = review.getReviewer();

        // 리뷰 작성자가 구매자인지 판매자인지 확인하여 해당 플래그 업데이트
        if (reviewer.getId().equals(trade.getBuyer().getId())) {
            trade.setBuyerReviewed(false); // 구매자 리뷰 플래그를 false로 변경
        } else if (reviewer.getId().equals(trade.getItem().getMember().getId())) {
            trade.setSellerReviewed(false); // 판매자 리뷰 플래그를 false로 변경
        }

        Item item = review.getTrade().getItem(); // 삭제 전에 아이템 정보를 가져옴
        tradeReviewRepository.delete(review);

        // 아이템 평점 업데이트
        updateItemAverageRating(item);
    }

    // 내가 쓴 단일 리뷰 조회
    @Transactional
    public TradeReviewResponse getReviewById(Long reviewId, Long memberId) {
        TradeReview review = tradeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception404("리뷰를 찾을 수 없습니다."));
        if (!review.getReviewer().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인만 자신의 리뷰를 조회할 수 있습니다.");
        }
        return TradeReviewResponse.from(review);
    }

    // 특정 회원의 판매 완료 상품 리뷰 리스트 반환
    @Transactional
    public List<TradeReviewResponse> getSoldByMember(Long memberId) {
        List<TradeReview> reviews = tradeReviewRepository.findSoldReviewsBySellerId(memberId);
        return reviews.stream()
                .map(TradeReviewResponse::from)
                .toList();
    }

    // 판매자(memberId)의 판매 완료 상품 리뷰 최근 3개 반환
    @Transactional
    public List<TradeReviewResponse> getRecentSoldReviewsByMember(Long memberId) {
        List<TradeReview> reviews = tradeReviewRepository.findSoldReviewsBySellerId(memberId);
        return reviews.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(3)
                .map(TradeReviewResponse::from)
                .toList();
    }
}
