package com.market.market_place.terms.controllers;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place.terms.dtos.TermsDto;
import com.market.market_place.terms.services.TermsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Terms API", description = "약관 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
public class TermsController {

    private final TermsService termsService;

    @Operation(summary = "전체 약관 목록 조회", description = "회원가입 시 필요한 모든 약관의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<TermsDto>>> getTermsList() {
        List<TermsDto> terms = termsService.getTermsList();
        return ResponseEntity.ok(ApiUtil.success(terms));
    }

    @Operation(summary = "특정 약관 상세 조회", description = "ID로 특정 약관의 상세 내용을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiUtil.ApiResult<TermsDto>> getTermById(@PathVariable Long id) {
        TermsDto term = termsService.getTermById(id);
        return ResponseEntity.ok(ApiUtil.success(term));
    }
}
