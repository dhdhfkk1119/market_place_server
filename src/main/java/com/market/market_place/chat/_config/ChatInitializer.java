package com.market.market_place.chat._config;

import com.market.market_place.chat.chat_message.ChatMessage;
import com.market.market_place.chat.chat_message.ChatMessageRepository;
import com.market.market_place.chat.chat_room.ChatRoom;
import com.market.market_place.chat.chat_room.ChatRoomRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Profile({"dev", "local"})
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatInitializer implements CommandLineRunner {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository; // 메시지 리포지토리 주입

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 사용자 ID 1과 2 사이의 채팅방이 없는 경우에만 생성
        if (chatRoomRepository.findByUserIds(1L, 2L).isEmpty()) {
            Optional<Member> user1Opt = memberRepository.findById(1L);
            Optional<Member> user2Opt = memberRepository.findById(2L);

            if (user1Opt.isPresent() && user2Opt.isPresent()) {
                Member user1 = user1Opt.get(); // 관리자 (ID: 1)
                Member user2 = user2Opt.get(); // 테스트유저 (ID: 2)

                // 1. 채팅방 생성 및 저장
                ChatRoom newChatRoom = ChatRoom.builder()
                        .loginUser(user1)
                        .otherUser(user2)
                        .build();
                ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);
                log.info("초기 데이터: 사용자 '{}'와 '{}' 간의 채팅방 생성 완료", user1.getLoginId(), user2.getLoginId());

                // 2. 초기 대화 내용 생성
                ChatMessage message1 = ChatMessage.builder()
                        .chatRoom(savedChatRoom)
                        .sender(user1)
                        .receiver(user2)
                        .message("마킷! 관리자입니다. 무엇을 도와드릴까요?")
                        .build();

                ChatMessage message2 = ChatMessage.builder()
                        .chatRoom(savedChatRoom)
                        .sender(user2)
                        .receiver(user1)
                        .message("네 궁금한 게 있어서요.")
                        .build();

                List<ChatMessage> messages = chatMessageRepository.saveAll(List.of(message1, message2));
                log.info("초기 데이터: 채팅방에 초기 메시지 2건 생성 완료");

                // 3. 채팅방의 마지막 메시지 업데이트
                savedChatRoom.setLastMessage(messages.get(messages.size() - 1));
                chatRoomRepository.save(savedChatRoom); // 변경 사항 저장
                log.info("초기 데이터: 채팅방의 마지막 메시지 정보 업데이트 완료");

            } else {
                log.warn("초기 데이터: 채팅방을 생성하기 위한 사용자(ID 1 또는 2)를 찾을 수 없습니다.");
            }
        }
    }
}
