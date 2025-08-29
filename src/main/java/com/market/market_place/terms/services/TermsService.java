package com.market.market_place.terms.services;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place.members.domain.Member;
import com.market.market_place.terms.domain.MemberAgreedTerms;
import com.market.market_place.terms.domain.Terms;
import com.market.market_place.terms.dtos.AgreeTermsRequestDto;
import com.market.market_place.terms.dtos.TermsDto;
import com.market.market_place.terms.repository.MemberAgreedTermsRepository;
import com.market.market_place.terms.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;
    private final MemberAgreedTermsRepository memberAgreedTermsRepository;

    /**
     * 모든 약관 목록을 조회합니다.
     * @return 약관 DTO 리스트
     */
    public List<TermsDto> getTermsList() {
        return termsRepository.findAll().stream()
                .map(TermsDto::new)
                .collect(Collectors.toList());
    }

    /**
     * ID로 특정 약관의 상세 정보를 조회합니다.
     * @param termId 조회할 약관의 ID
     * @return 약관 상세 정보를 담은 DTO
     */
    public TermsDto getTermById(Long termId) {
        Terms terms = termsRepository.findById(termId)
                .orElseThrow(() -> new Exception404("해당 약관을 찾을 수 없습니다. ID: " + termId));
        return new TermsDto(terms);
    }

    /**
     * 사용자의 약관 동의를 처리하고, 필수 약관에 모두 동의했는지 검증합니다.
     * @param request 동의한 약관 ID 목록
     * @param member 약관에 동의한 회원 엔티티
     */
    @Transactional
    public void agreeTerms(AgreeTermsRequestDto request, Member member) {
        Set<Long> agreedTermIds = request.getAgreedTermIds();

        // 1. 모든 필수 약관을 조회
        List<Terms> requiredTerms = termsRepository.findAll().stream()
                .filter(Terms::isRequired)
                .toList();

        // 2. 사용자가 모든 필수 약관에 동의했는지 확인
        for (Terms term : requiredTerms) {
            if (!agreedTermIds.contains(term.getId())) {
                throw new Exception400(term.getTitle() + " 약관에 동의해야 합니다.");
            }
        }

        // 3. 동의 내역 저장
        List<MemberAgreedTerms> agreedTermsToSave = agreedTermIds.stream()
                .map(termId -> {
                    Terms term = termsRepository.findById(termId)
                            .orElseThrow(() -> new Exception400("존재하지 않는 약관입니다: " + termId));
                    return MemberAgreedTerms.builder()
                            .member(member)
                            .term(term)
                            .build();
                })
                .collect(Collectors.toList());

        memberAgreedTermsRepository.saveAll(agreedTermsToSave);
    }
}
