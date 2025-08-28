package com.market.market_place.terms.controllers;

import com.market.market_place.terms.domain.Terms;
import com.market.market_place.terms.dtos.TermsDto;
import com.market.market_place.terms.repository.TermsRepository;
import com.market.market_place.terms.services.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
public class TermsController {

    private final TermsService termsService;
    private final TermsRepository termsRepository; // For data initialization

    /**
     * 모든 약관 목록을 조회합니다.
     * @return 약관 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<List<TermsDto>> getTermsList() {
        List<TermsDto> terms = termsService.getTermsList();
        return ResponseEntity.ok(terms);
    }

    /**
     * 테스트용 초기 약관 데이터를 생성합니다.
     * (프로덕션 환경에서는 사용하지 않아야 합니다.)
     */
    @PostMapping("/init")
    public ResponseEntity<String> initTermsData() {
        termsRepository.saveAll(List.of(
                Terms.builder().title("서비스 이용약관").content("서비스 이용약관 내용입니다...").isRequired(true).build(),
                Terms.builder().title("개인정보 수집 및 이용 동의").content("개인정보 수집 및 이용 동의 내용입니다...").isRequired(true).build(),
                Terms.builder().title("마케팅 정보 수신 동의").content("마케팅 정보 수신 동의 내용입니다...").isRequired(false).build()
        ));
        return ResponseEntity.ok("초기 약관 데이터가 생성되었습니다.");
    }
}
