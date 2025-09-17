package com.market.market_place.item.praise;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.PraiseTopic.PraiseTopic;
import com.market.market_place.item.PraiseTopic.PraiseTopicRepository;
import com.market.market_place.item.praise_category.PraiseCategory;
import com.market.market_place.item.praise_category.PraiseCategoryRepository;
import com.market.market_place.item.status.Trade;
import com.market.market_place.item.status.TradeRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PraiseService {

    private final PraiseRepository praiseRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;
    private final PraiseCategoryRepository praiseCategoryRepository;
    private final PraiseTopicRepository praiseTopicRepository;

    @Transactional
    public PraiseResponse addPraise(Long praiserId, PraiseRequest request) {

        if (praiseRepository.existsByPraiserIdAndTradeId(praiserId, request.getTradeId())) {
            return PraiseResponse.builder()
                    .message("이미 해당 거래를 칭찬하셨습니다.")
                    .isSuccess(false)
                    .build();
        }

        Member praiser = findMemberById(praiserId, "칭찬한 사용자를 찾을 수 없습니다.");
        Member praisedMember = findMemberById(request.getPraisedMemberId(), "칭찬받은 사용자를 찾을 수 없습니다.");
        Trade trade = tradeRepository.findById(request.getTradeId())
                .orElseThrow(() -> new Exception404("거래를 찾을 수 없습니다."));

        String finalContent = generatePraiseContent(request);

        Praise newPraise = Praise.createPraise(
                trade, praiser, praisedMember, finalContent, request.isBuyer()
        );
        praiseRepository.save(newPraise);

        savePraiseTopics(request, newPraise);

        praisedMember.setRetransactionRate(praisedMember.getRetransactionRate() + 1);

        return PraiseResponse.builder()
                .message("매너 칭찬이 완료되었습니다!")
                .updatedRetransactionRate(praisedMember.getRetransactionRate())
                .updatedMannerScore(praisedMember.getMannerScore())
                .isSuccess(true)
                .build();
    }


    private Member findMemberById(Long id, String notFoundMessage) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new Exception404(notFoundMessage));
    }

    private String generatePraiseContent(PraiseRequest request) {
        if (request.hasCustomContent()) {
            return request.getContent();
        }

        List<Long> categoryIds = request.getPraiseCategories();
        if (categoryIds == null || categoryIds.isEmpty()) {
            return "좋은 거래였습니다!";
        }

        List<PraiseCategory> categories = praiseCategoryRepository.findAllById(categoryIds);
        List<String> topicNames = categories.stream()
                .map(PraiseCategory::getPraiseName)
                .toList();

        return PraiseContentGenerator.generateContentFromTopic(topicNames);
    }

    private void savePraiseTopics(PraiseRequest request, Praise praise) {
        List<Long> categoryIds = request.getPraiseCategories();
        if (categoryIds == null || categoryIds.isEmpty()) return;

        List<PraiseCategory> categories = praiseCategoryRepository.findAllById(categoryIds);
        for (PraiseCategory category : categories) {
            PraiseTopic topic = PraiseTopic.builder()
                    .praise(praise)
                    .praiseCategory(category)
                    .build();
            praiseTopicRepository.save(topic);

        }
    }
}