package com.market.market_place.email.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    @Bean
    private JavaMailSender javaMailSender() {
        return this.javaMailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        log.debug("이메일 발송 시도. 수신자: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
            log.info("이메일 발송 성공. 수신자: {}", to);
        } catch (Exception e) {
            // 메일 전송 실패 시, 스택 트레이스 전체를 기록하여 원인 파악을 용이하게 함
            log.error("이메일 발송 실패. 수신자: {}", to, e);
            // 실제 운영 환경에서는 이 예외를 좀 더 구체적으로 처리해야 합니다.
            throw new RuntimeException("메일 전송 중 오류가 발생했습니다.", e);
        }
    }
}
