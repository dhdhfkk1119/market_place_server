package com.market.market_place.item.notification;

import com.market.market_place.members.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(Member member, String message) {
        System.out.println("알림 전송 : " + member.getLoginId() + " 메세지" + message);
    }

}
