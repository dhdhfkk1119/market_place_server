package com.market.market_place.email.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender; // final 키워드로 불변성 보장

    @Async // 이 메서드가 별도의 스레드에서 비동기적으로 실행되도록 설정
    public void sendEmail(String to, String subject, String text) {
        log.debug("비동기 이메일 발송 시작. 수신자: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
            log.info("비동기 이메일 발송 성공. 수신자: {}", to);
        } catch (Exception e) {
            // 메일 전송 실패 시, 스택 트레이스 전체를 기록하여 원인 파악을 용이하게 함
            log.error("비동기 이메일 발송 실패. 수신자: {}", to, e);
            // 비동기 메서드에서의 예외는 @Async 예외 핸들러로 처리하는 것이 이상적입니다.
        }
    }
}
