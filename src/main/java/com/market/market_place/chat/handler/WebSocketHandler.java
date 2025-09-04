package com.market.market_place.chat.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.market_place._core._config.UploadConfig;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.FileUploadUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.chat._enum.MessageType;
import com.market.market_place.chat.chat_file.ChatFileRequestDTO;
import com.market.market_place.chat.chat_image.ChatImage;
import com.market.market_place.chat.chat_image.ChatImageRepository;
import com.market.market_place.chat.chat_image.ChatImageRequestDTO;
import com.market.market_place.chat.chat_message.*;
import com.market.market_place.chat.chat_room.ChatRoom;
import com.market.market_place.chat.chat_room.ChatRoomRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatMessageService chatMessageService;
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();




    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = UriComponentsBuilder.fromUri(session.getUri()).build().getQueryParams().getFirst("token");
        if (token != null && !token.isBlank()) {
            try {
                JwtUtil.SessionUser sessionUser = JwtUtil.verifyAndReturnSessionUser(token);
                session.getAttributes().put("userId", sessionUser.getId());
                sessions.put(sessionUser.getId(),session);
            } catch (JWTVerificationException e) {
                session.close(CloseStatus.BAD_DATA.withReason("Invalid JWT token."));
            }
        }
    }

    /**
     * 클라이언트가 보낸 메시지를 처리
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessageRequestDTO.Message msgDTO = objectMapper.readValue(payload, ChatMessageRequestDTO.Message.class);

        Long senderId = (Long) session.getAttributes().get("userId");
        if (senderId == null) {
            session.close(CloseStatus.BAD_DATA.withReason("User not authenticated."));
            return;
        }

        // 서비스 계층의 메서드를 호출하여 비즈니스 로직 처리
        ChatMessageResponseDTO.MessageDTO responseDTO = chatMessageService.saveAndProcessMessage(senderId, msgDTO);
        String response = objectMapper.writeValueAsString(responseDTO);

        // 메시지 전송 로직
        session.sendMessage(new TextMessage(response));
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
        System.out.println("세션 종료됨");
    }
}

