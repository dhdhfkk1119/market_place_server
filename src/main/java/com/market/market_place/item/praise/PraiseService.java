// PraiseService.java
package com.market.market_place.item.praise;

<<<<<<< HEAD
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.item.notification.NotificationService;
import com.market.market_place.item.praise_category.PraiseTopic;
import com.market.market_place.item.praise_category.PraiseTopicRepository;
=======
import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.praise_category.PraiseCategoryRepository;
>>>>>>> f-board
import com.market.market_place.item.status.Trade;
import com.market.market_place.item.status.TradeRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
<<<<<<< HEAD
import java.util.stream.Collectors;
=======
>>>>>>> f-board

@Service
@RequiredArgsConstructor
public class PraiseService {

    private final PraiseRepository praiseRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;
<<<<<<< HEAD
    private final NotificationService notificationService;
    private final PraiseTopicRepository praiseTopicRepository;
    private final PraiseHasTopicRepository praiseHasTopicRepository;

    @Transactional
    public PraiseResponse addPraise(JwtUtil.SessionUser sessionUser, PraiseRequest request) {

        Trade trade = tradeRepository.findById(request.getTradeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 거래입니다."));

        Member praiser = memberRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("칭찬한 사용자를 찾을 수 없습니다."));

        Member praisedMember = request.isBuyer() ? trade.getBuyer() : trade.getSeller();


        if (praiseRepository.existsByPraiserIdAndPraisedMemberIdAndTradeId(praiser.getId(), praisedMember.getId(), trade.getId())) {
            return PraiseResponse.builder()
                    .message("이미 해당 거래에 매너 칭찬을 하셨습니다.")
                    .isSuccess(false)
=======
    private final PraiseCategoryRepository praiseCategoryRepository;

    @Transactional
    public PraiseResponse addPraise(Long praiserId, PraiseRequest request) {
        // 중복 칭찬 여부 확인
        boolean alreadyPraised = praiseRepository.existsByPraiserIdAndTradeId(praiserId, request.getTradeId());
        if (alreadyPraised) {
            return PraiseResponse.builder()
                    .message("이미 해당 거래를 칭찬하셨습니다.")
                    .success(false)
>>>>>>> f-board
                    .build();
        }


<<<<<<< HEAD
        Praise newPraise = Praise.createPraise(trade, praiser, praisedMember);
        praiseRepository.save(newPraise);


<<<<<<< HEAD
        List<PraiseTopic> topics = praiseTopicRepository.findAllById(request.getPraiseCategories());
        List<PraiseHasTopic> praiseHasTopics = topics.stream()
                .map(topic -> PraiseHasTopic.builder()
                        .praise(newPraise)
                        .praiseTopic(topic)
                        .build())
                .collect(Collectors.toList());
        praiseHasTopicRepository.saveAll(praiseHasTopics);


        praisedMember.addMannerScore(0.1 * topics.size());
=======
        Member praiser = memberRepository.findById(praiserId)
                .orElseThrow(() -> new Exception404("칭찬한 사용자를 찾을 수 없습니다."));
        Member praisedMember = memberRepository.findById(request.getPraisedMemberId())
                .orElseThrow(() -> new Exception404("칭찬받은 사용자를 찾을 수 없습니다."));
        Trade trade = tradeRepository.findById(request.getTradeId())
                .orElseThrow(() -> new Exception404("거래를 찾을 수 없습니다."));


        String finalContent = request.getContent();
        if (finalContent == null || finalContent.trim().isEmpty()) {
            List<String> topicNames = request.getPraiseCategories() == null ? List.of() :
                    praiseCategoryRepository.findAllById(request.getPraiseCategories())
                            .stream()
                            .map(pc -> pc.getPraiseName())
                            .toList();

            finalContent = PraiseContentGenerator.generateContentFromTopic(topicNames);
        }

        // 저장
        Praise newPraise = Praise.builder()
                .praiser(praiser)
                .praisedMember(praisedMember)
                .trade(trade)
                .content(finalContent)
                .build();
        praiseRepository.save(newPraise);

        // 재거래율 증가
        int updatedRate = praisedMember.getRetransactionRate() + 1;
        praisedMember.setRetransactionRate(updatedRate);
>>>>>>> f-board
        memberRepository.save(praisedMember);
=======
//        int currentRate = praisedMember.getRetransactionRate();
//        int updatedRate = currentRate + 1;
//        praisedMember.setRetransactionRate(updatedRate);
//        memberRepository.save(praisedMember);
>>>>>>> 67d593895fd5aed8311e241d21b7b01517200260


        String notificationMessage = praiser.getLoginId() + " 님이 " + trade.getItem().getTitle() + " 거래에 대해 매너 칭찬을 남겼습니다.";
        notificationService.sendNotification(praisedMember, notificationMessage);

        return PraiseResponse.builder()
                .message("매너 칭찬이 완료되었습니다!")
<<<<<<< HEAD
<<<<<<< HEAD
                .updatedMannerScore(praisedMember.getMannerScore())
=======
>>>>>>> 67d593895fd5aed8311e241d21b7b01517200260
                .isSuccess(true)
                .praiseCategories(request.getPraiseCategories())
=======
                .updatedRetransactionRate(updatedRate)
                .success(true)
>>>>>>> f-board
                .build();
    }
}
