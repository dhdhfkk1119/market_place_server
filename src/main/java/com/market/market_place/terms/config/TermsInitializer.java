package com.market.market_place.terms.config;

import com.market.market_place.terms.domain.Terms;
import com.market.market_place.terms.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 개발(dev) 또는 로컬(local) 환경에서만 실행되도록 프로필 설정
@Profile({"dev", "local"})
@Component
@RequiredArgsConstructor
public class TermsInitializer implements CommandLineRunner {

    private final TermsRepository termsRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 약관 데이터가 없으면 기본값 생성
        if (termsRepository.count() == 0) {
            termsRepository.saveAll(List.of(
                    Terms.builder().title("서비스 이용약관").content("서비스 이용약관 내용입니다...").isRequired(true).build(),
                    Terms.builder().title("개인정보 수집 및 이용 동의").content("개인정보 수집 및 이용 동의 내용입니다...").isRequired(true).build(),
                    Terms.builder().title("개인정보 제3자 정보제공 동의").content("개인정보 제3자 정보제공 동의 내용입니다...").isRequired(true).build(),
                    Terms.builder().title("위치기반 서비스 이용약관 동의").content("위치기반 서비스 이용약관 동의 내용입니다...").isRequired(true).build()
            ));
        }
    }
}
