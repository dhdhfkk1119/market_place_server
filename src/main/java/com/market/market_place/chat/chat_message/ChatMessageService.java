package com.market.market_place.chat.chat_message;

import com.market.market_place.chat.chat_room.ChatRoom;
import com.market.market_place.chat.chat_room.ChatRoomRepository;
import com.market.market_place.chat.chat_room.ChatRoomResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 메세지 보내기 및 방 생성하기
//    public ChatMessageResponseDTO.MessageDTO saveMessage(ChatMessageRequestDTO.Message dto){
//        ChatRoom room = chatRoomRepository.findByUserIds(dto.getSendId(),dto.getReceiveId())
//                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
//                                .userId1(dto.getSendId())
//                                .userId2(dto.getReceiveId())
//                        .build()));
//        ChatMessage chatMessage = dto.toEntity(room);
//        chatMessageRepository.save(chatMessage);
//        return new ChatMessageResponseDTO.MessageDTO(chatMessage, Collections.emptyList());
//    }

    // 내가 속한 한 방에 메세지 내역을 전부 가져오기
    public List<ChatMessageResponseDTO.MessageDTO> getMessagesByRoom(Long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findMessagesByChatRoomId(roomId);

        return messages.stream()
                .map(msg -> new ChatMessageResponseDTO.MessageDTO(msg, Collections.emptyList()))
                .collect(Collectors.toList());
    }

}
