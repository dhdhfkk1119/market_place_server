package com.market.market_place.terms.controllers;

import com.market.market_place.terms.dtos.TermsDto;
import com.market.market_place.terms.services.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
public class TermsController {

    private final TermsService termsService;

    // 약관목록
    @GetMapping
    public ResponseEntity<List<TermsDto>> getTermsList() {
        List<TermsDto> terms = termsService.getTermsList();
        return ResponseEntity.ok(terms);
    }

    // 약관 상세조회 id
    @GetMapping("/{id}")
    public ResponseEntity<TermsDto> getTermById(@PathVariable Long id) {
        TermsDto term = termsService.getTermById(id);
        return ResponseEntity.ok(term);
    }
}
