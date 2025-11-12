package markit.item.config;

import markit.item.core.Item;
import markit.item.core.ItemRepository;
import markit.item.review.TradeReview;
import markit.item.review.TradeReviewRepository;
import markit.item.status.Trade;
import markit.item.status.TradeRepository;
import markit.item.status.TradeStatus;
import markit.members.domain.Member;
import markit.members.repositories.MemberRepository;
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
        
        Trade t1  = upsertTrade(i5,  user1,  user6,  true,  true,  TradeStatus.SOLD);
        Trade t2  = upsertTrade(i6,  user2,  user7,  true,  false, TradeStatus.SOLD);
        Trade t3  = upsertTrade(i7,  user3,  user8,  false, true,  TradeStatus.PENDING); // 진행중 케이스 예시
        Trade t4  = upsertTrade(i8,  user4,  user9,  true,  true,  TradeStatus.SOLD);
        Trade t5  = upsertTrade(i9,  user5,  user10, false, false, TradeStatus.PENDING); // 진행중 케이스 예시
        Trade t6  = upsertTrade(i10, user1,  user7,  true,  true,  TradeStatus.SOLD);
        Trade t7  = upsertTrade(i11, user2,  user8,  true,  true,  TradeStatus.SOLD);
        Trade t8  = upsertTrade(i12, user3,  user9,  false, true,  TradeStatus.PENDING); // 진행중 케이스 예시
        Trade t9  = upsertTrade(i13, user4,  user10, true,  false, TradeStatus.SOLD);
        Trade t10 = upsertTrade(i14, user5,  user6,  true,  true,  TradeStatus.SOLD);

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
                              boolean buyerReviewed, boolean sellerReviewed,
                              TradeStatus status) {

        // findByItem이 여러 거래를 반환할 수 있으므로, 여기서는 orElseGet을 사용하여 항상 새로 생성하도록 단순화
        // 초기화 스크립트는 멱등성을 보장하는 것이 좋지만, 테스트 데이터 생성 목적이므로 단순하게 구현
        return tradeRepository.findByItem(item).orElseGet(() -> {
            Trade newTrade = Trade.builder()
                    .item(item)
                    .buyer(buyer)
                    .status(status)
                    .buyerReviewed(buyerReviewed)
                    .sellerReviewed(sellerReviewed)
                    .build();

            // 거래 상태가 SOLD이면 완료 시간 설정
            if (status == TradeStatus.SOLD) {
                newTrade.completeTrade();
            }

            return tradeRepository.save(newTrade);
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
