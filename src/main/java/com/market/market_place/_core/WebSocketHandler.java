package com.market.market_place._core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.market_place.chat.chat_message.ChatMessage;
import com.market.market_place.chat.chat_message.ChatMessageRepository;
import com.market.market_place.chat.chat_message.ChatMessageRequestDTO;
import com.market.market_place.chat.chat_message.ChatMessageResponseDTO;
import com.market.market_place.chat.chat_room.ChatRoom;
import com.market.market_place.chat.chat_room.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        // 메세지 처리 로직
        session.sendMessage(new TextMessage("서버에서 보내는 응답:" + payload));
        ChatMessageRequestDTO.Message msgDTO = objectMapper.readValue(payload,ChatMessageRequestDTO.Message.class);

        // 방생성
        ChatRoom room = chatRoomRepository.findByUserId1AndUserId2(msgDTO.getSendId(),msgDTO.getReceiveId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                                .userId1(msgDTO.getSendId())
                                .userId2(msgDTO.getReceiveId())
                        .build()));
        
        // 메세지 저장
        ChatMessage chatMessage = msgDTO.toEntity(room);
        chatMessageRepository.save(chatMessage);

        // 응답 DTO
        ChatMessageResponseDTO.MessageDTO DTO = new ChatMessageResponseDTO.MessageDTO(chatMessage, Collections.emptyList());

        // 5. JSON 변환해서 클라잉너트에게 응답
        String response = objectMapper.writeValueAsString(DTO);
        session.sendMessage(new TextMessage(response));
    }

}
