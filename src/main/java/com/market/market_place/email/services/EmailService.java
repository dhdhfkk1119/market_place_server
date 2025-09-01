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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
            log.info("메일 전송 완료: {}", to);
        } catch (Exception e) {
            log.error("메일 전송 실패: {}", e.getMessage());
            // 프로덕션에서는 여기서 예외 처리를 더 견고하게 해야 합니다.
            // 예를 들어, 메일 서버 접속 실패 시 재시도 로직이나 알림 등을 추가할 수 있습니다.
        }
    }
}
