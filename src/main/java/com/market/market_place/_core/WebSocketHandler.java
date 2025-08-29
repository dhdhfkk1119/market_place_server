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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatImage

    // 연결된 사용자 세션을 저장할 맵 (userId → session)
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * 웹소켓 연결이 수립되었을 때
     * (나중에 JWT에서 userId 추출해서 sessions.put(userId, session) 하면 됨)
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 일단 테스트용: URL query ?userId=1 이런 식으로 전달받는다고 가정
        String query = session.getUri().getQuery(); // "userId=1"
        Long userId = Long.parseLong(query.split("=")[1]);
        sessions.put(userId, session);

        System.out.println("✅ 사용자 " + userId + " 연결됨");
    }

    /**
     * 클라이언트가 보낸 메시지를 처리
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessageRequestDTO.Message msgDTO = objectMapper.readValue(payload, ChatMessageRequestDTO.Message.class);

        // 방 생성 or 조회
        ChatRoom room = chatRoomRepository.findByUserId1AndUserId2(msgDTO.getSendId(), msgDTO.getReceiveId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                        .userId1(msgDTO.getSendId())
                        .userId2(msgDTO.getReceiveId())
                        .build()));
        if(!msgDTO.getImages().isEmpty()) {

        }

        // 메시지 저장
        ChatMessage chatMessage = msgDTO.toEntity(room);
        chatMessageRepository.save(chatMessage);

        // 응답 DTO 생성
        ChatMessageResponseDTO.MessageDTO responseDTO = new ChatMessageResponseDTO.MessageDTO(chatMessage, Collections.emptyList());
        String response = objectMapper.writeValueAsString(responseDTO);

        // 1. 보낸 본인에게 전송
        session.sendMessage(new TextMessage(response));

        // 2. 상대방 세션이 있으면 전송
        WebSocketSession receiverSession = sessions.get(msgDTO.getReceiveId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(response));
        }
    }

    /**
     * 연결 종료 시 세션 정리
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.values().remove(session);
        System.out.println("❎ 세션 종료됨");
    }
}

