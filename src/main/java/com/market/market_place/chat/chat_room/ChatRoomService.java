package com.market.market_place.chat.chat_room;

import com.market.market_place.chat.chat_message.ChatMessage;
import com.market.market_place.chat.chat_message.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 내가 참여한 방 목록 + 마지막 메시지
    public List<ChatRoomResponseDTO.ChatRoomDTO> getMyChatRooms(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser(userId);

        return chatRooms.stream().map(cr -> {
            List<ChatMessage> messages = chatMessageRepository.findMessagesByChatRoomId(cr.getId());
            ChatMessage lastMessage = messages.isEmpty() ? null : messages.get(0);

            return ChatRoomResponseDTO.ChatRoomDTO.builder()
                    .chatRoom(cr)
                    .lastMessage(lastMessage)
                    .loginUserId(userId)
                    .build();

        }).toList();
    }

    @Transactional
    public ChatRoom getOrCreateRoom(Long senderId, Long receiverId) {
        return chatRoomRepository.findByUserIds(senderId, receiverId)
                .orElseGet(() -> chatRoomRepository.save(
                        ChatRoom.builder()
                                .userId1(senderId)
                                .userId2(receiverId)
                                .build()
                ));
    }
}
