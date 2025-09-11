// PraiseService.java
package com.market.market_place.item.praise;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.item.notification.NotificationService;
import com.market.market_place.item.praise_category.PraiseTopic;
import com.market.market_place.item.praise_category.PraiseTopicRepository;
import com.market.market_place.item.status.Trade;
import com.market.market_place.item.status.TradeRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PraiseService {

    private final PraiseRepository praiseRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;
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
                    .build();
        }


        Praise newPraise = Praise.createPraise(trade, praiser, praisedMember);
        praiseRepository.save(newPraise);


        List<PraiseTopic> topics = praiseTopicRepository.findAllById(request.getPraiseCategories());
        List<PraiseHasTopic> praiseHasTopics = topics.stream()
                .map(topic -> PraiseHasTopic.builder()
                        .praise(newPraise)
                        .praiseTopic(topic)
                        .build())
                .collect(Collectors.toList());
        praiseHasTopicRepository.saveAll(praiseHasTopics);


        praisedMember.addMannerScore(0.1 * topics.size());
        memberRepository.save(praisedMember);


        String notificationMessage = praiser.getLoginId() + " 님이 " + trade.getItem().getTitle() + " 거래에 대해 매너 칭찬을 남겼습니다.";
        notificationService.sendNotification(praisedMember, notificationMessage);

        return PraiseResponse.builder()
                .message("매너 칭찬이 완료되었습니다!")
                .updatedMannerScore(praisedMember.getMannerScore())
                .isSuccess(true)
                .praiseCategories(request.getPraiseCategories())
                .build();
    }
}