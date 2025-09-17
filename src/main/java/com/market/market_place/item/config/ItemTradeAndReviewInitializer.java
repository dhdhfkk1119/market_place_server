package com.market.market_place.item.config;

import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.item.review.TradeReview;
import com.market.market_place.item.review.TradeReviewRepository;
import com.market.market_place.item.status.Trade;
import com.market.market_place.item.status.TradeRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile({"dev","local"})
@Component
@Order(4)
@RequiredArgsConstructor
public class ItemTradeAndReviewInitializer implements CommandLineRunner {

    private final TradeRepository tradeRepository;
    private final TradeReviewRepository tradeReviewRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // 아이템: 5~14번에 해당하는 제목(위 이니셜라이저가 만든 것과 동일하게 맞춤)
        Item i5  = getItem("델노트북");
        Item i6  = getItem("사무용의자");
        Item i7  = getItem("원목의자");
        Item i8  = getItem("게이밍의자");
        Item i9  = getItem("식탁의자");
        Item i10 = getItem("디자인의자");
        Item i11 = getItem("자바책");
        Item i12 = getItem("알고리즘책");
        Item i13 = getItem("데이터베이스책");
        Item i14 = getItem("영어책");

        Member user1  = getMember("user1");
        Member user2  = getMember("user2");
        Member user3  = getMember("user3");
        Member user4  = getMember("user4");
        Member user5  = getMember("user5");
        Member user6  = getMember("user6");
        Member user7  = getMember("user7");
        Member user8  = getMember("user8");
        Member user9  = getMember("user9");
        Member user10 = getMember("user10");

        // ===== 거래 더미 (보내주신 SQL과 동일 순서/의미) =====
        Trade t1  = upsertTrade(i5,  user1,  user6,  true,  true);
        Trade t2  = upsertTrade(i6,  user2,  user7,  true,  false);
        Trade t3  = upsertTrade(i7,  user3,  user8,  false, true);
        Trade t4  = upsertTrade(i8,  user4,  user9,  true,  true);
        Trade t5  = upsertTrade(i9,  user5,  user10, false, false);
        Trade t6  = upsertTrade(i10, user1,  user7,  true,  true);
        Trade t7  = upsertTrade(i11, user2,  user8,  true,  true);
        Trade t8  = upsertTrade(i12, user3,  user9,  false, true);
        Trade t9  = upsertTrade(i13, user4,  user10, true,  false);
        Trade t10 = upsertTrade(i14, user5,  user6,  true,  true);

        // ===== 거래 리뷰 더미 (보내주신 SQL 그대로) =====
        upsertReview(t1,  user6,  "좋은 거래였습니다. 감사합니다!", 5.0);
        upsertReview(t1,  user1,  "구매자분이 친절했습니다.",        4.8);
        upsertReview(t2,  user7,  "상품 상태가 설명과 같아요.",      4.5);
        upsertReview(t2,  user2,  "빠른 결제 감사합니다.",            5.0);
        upsertReview(t3,  user8,  "사진보다 상태가 별로였어요.",      3.0);
        upsertReview(t4,  user9,  "좋은 거래였어요.",                4.7);
        upsertReview(t4,  user4,  "구매자분 응답이 빨랐습니다.",      5.0);
        upsertReview(t5,  user10, "배송이 조금 늦었어요.",            3.5);
        upsertReview(t6,  user7,  "아주 만족스러운 거래였습니다.",    5.0);
        upsertReview(t6,  user1,  "연락이 빨라서 좋았습니다.",        4.9);
        upsertReview(t7,  user8,  "상품이 설명보다 더 좋았어요.",     5.0);
        upsertReview(t8,  user9,  "연락이 잘 안 되어 아쉬웠습니다.",  3.2);
        upsertReview(t8,  user3,  "결제는 빨랐습니다.",               4.0);
        upsertReview(t9,  user10, "판매자분이 친절했어요.",           4.8);
        upsertReview(t10, user5,  "시간 약속을 잘 지키셨어요.",       5.0);
    }

    private Item getItem(String title) {
        return itemRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalStateException("아이템 없음: " + title));
    }

    private Member getMember(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("멤버 없음: " + loginId));
    }

    private Trade upsertTrade(Item item, Member seller, Member buyer,
                              boolean buyerReviewed, boolean sellerReviewed) {
        return tradeRepository.findByItem(item).orElseGet(() -> {
            Trade t = Trade.builder()
                    .item(item)
                    .seller(seller)
                    .buyer(buyer)
                    .buyerReviewed(buyerReviewed)
                    .sellerReviewed(sellerReviewed)
                    .build();
            return tradeRepository.save(t);
        });
    }

    private void upsertReview(Trade trade, Member reviewer, String content, double rating) {
        boolean exists = tradeReviewRepository.existsByTradeAndReviewer(trade, reviewer);
        if (!exists) {
            tradeReviewRepository.save(TradeReview.builder()
                    .trade(trade)
                    .reviewer(reviewer)
                    .content(content)
                    .rating(rating)
                    .build());
        }
    }
}