package com.market.market_place.item.praise;

import com.market.market_place.item.status.TradeRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PraiseService {

    private final PraiseRepository praiseRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;

    @Transactional
    public PraiseResponse addPraise(Long praiserId, PraiseRequest request) {
        QPraise qPraise = QPraise.praise;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPraise.praiser.id.eq(praiserId));
        builder.and(qPraise.trade.id.eq(request.getTradeId()));

        if (praiseRepository.exists(builder)) {
            return PraiseResponse.builder()
                    .message("이미 해당 거래를 칭찬하셨습니다.")
                    .isSuccess(false)
                    .build();
        }



        Member praiser = memberRepository.findById(praiserId)
                .orElseThrow(() -> new IllegalArgumentException("칭찬한 사용자를 찾을 수 없습니다."));

        Member praisedMember = memberRepository.findById(request.getPraisedMemberId())
                .orElseThrow(() -> new IllegalArgumentException("칭찬받은 사용자를 찾을 수 없습니다."));


        Praise newPraise = Praise.builder()
                .praiser(praiser)
                .praisedMember(praisedMember)
                .trade(tradeRepository.findById(request.getTradeId())
                        .orElseThrow(() -> new IllegalArgumentException("거래를 찾을 수 없습니다.")))
                .build();

        praiseRepository.save(newPraise);


        int currentRate = praisedMember.getRetransactionRate();
        int updatedRate = currentRate + 1;
        praisedMember.setRetransactionRate(updatedRate);
        memberRepository.save(praisedMember);

        return PraiseResponse.builder()
                .message("매너 칭찬이 완료되었습니다!")
                .updatedRetransactionRate(updatedRate)
                .isSuccess(true)
                .build();
    }
}