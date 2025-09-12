package com.market.market_place.item.praise;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.praise_category.PraiseCategoryRepository;
import com.market.market_place.item.status.Trade;
import com.market.market_place.item.status.TradeRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PraiseService {

    private final PraiseRepository praiseRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;
    private final PraiseCategoryRepository praiseCategoryRepository;

    @Transactional
    public PraiseResponse addPraise(Long praiserId, PraiseRequest request) {
        // 중복 칭찬 여부 확인
        boolean alreadyPraised = praiseRepository.existsByPraiserIdAndTradeId(praiserId, request.getTradeId());
        if (alreadyPraised) {
            return PraiseResponse.builder()
                    .message("이미 해당 거래를 칭찬하셨습니다.")
                    .success(false)
                    .build();
        }


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
        memberRepository.save(praisedMember);

        return PraiseResponse.builder()
                .message("매너 칭찬이 완료되었습니다!")
                .updatedRetransactionRate(updatedRate)
                .success(true)
                .build();
    }
}
