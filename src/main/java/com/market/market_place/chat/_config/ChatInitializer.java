package com.market.market_place.chat._config;

import com.market.market_place.chat.chat_message.ChatMessage;
import com.market.market_place.chat.chat_message.ChatMessageRepository;
import com.market.market_place.chat.chat_room.ChatRoom;
import com.market.market_place.chat.chat_room.ChatRoomRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.Role;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Profile({"dev", "local"})
@Component
@Order(2) // MemberInitializer(1) 다음에 실행
@RequiredArgsConstructor
@Slf4j
public class ChatInitializer implements CommandLineRunner {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. 관리자 계정 조회
        Optional<Member> adminOpt = memberRepository.findByLoginId("admin");
        if (adminOpt.isEmpty()) {
            log.warn("관리자(admin) 계정을 찾을 수 없어 채팅방 초기화를 건너뜁니다.");
            return;
        }
        Member admin = adminOpt.get();

        // 2. 관리자를 제외한 모든 일반 유저 조회
        List<Member> users = memberRepository.findAll().stream()
                .filter(member -> member.getRole() == Role.USER)
                .toList();

        if (users.isEmpty()) {
            log.info("초기화할 일반 사용자가 없어 채팅방 초기화를 건너뜁니다.");
            return;
        }

        log.info("관리자와 {}명의 일반 사용자 간의 채팅방 초기화를 시작합니다...", users.size());

        // 3. 각 유저와 관리자 간의 채팅방 생성
        for (Member user : users) {
            createChatRoomWithAdmin(admin, user);
        }
    }

    private void createChatRoomWithAdmin(Member admin, Member user) {
        // 이미 채팅방이 있는지 확인
        if (chatRoomRepository.findByUserIds(admin.getId(), user.getId()).isPresent()) {
            return; // 이미 존재하면 건너뛰기
        }

        // 1. 채팅방 생성 및 저장
        ChatRoom newChatRoom = ChatRoom.builder()
                .loginUser(admin) // 편의상 관리자를 loginUser로 고정
                .otherUser(user)
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);
        log.info("  - 사용자 '{}'와 '{}' 간의 채팅방 생성 완료 (ID: {})", admin.getLoginId(), user.getLoginId(), savedChatRoom.getId());

        // 2. 관리자의 첫 메시지만 생성
        ChatMessage adminMessage = ChatMessage.builder()
                .chatRoom(savedChatRoom)
                .sender(admin)
                .receiver(user)
                .message("안녕하세요, " + user.getMemberProfile().getName() + "님! 마켓잇 관리자입니다. 무엇이든 물어보세요.")
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(adminMessage);

        // 3. 채팅방의 마지막 메시지 업데이트
        savedChatRoom.setLastMessage(savedMessage);
        chatRoomRepository.save(savedChatRoom);
    }
}
