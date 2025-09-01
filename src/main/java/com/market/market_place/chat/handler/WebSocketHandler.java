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
import com.market.market_place.chat.chat_message.ChatMessage;
import com.market.market_place.chat.chat_message.ChatMessageRepository;
import com.market.market_place.chat.chat_message.ChatMessageRequestDTO;
import com.market.market_place.chat.chat_message.ChatMessageResponseDTO;
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
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatImageRepository chatImageRepository;
    private final FileUploadUtil fileUploadUtil;
    private final UploadConfig uploadConfig;
    private final MemberRepository memberRepository;

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

        Long senderId = (Long) session.getAttributes().get("userId"); // 보내는 사용자 번호
         
        if (senderId == null) {
            session.close(CloseStatus.BAD_DATA.withReason("User not authenticated."));
            return;
        }

        Long receiverId = msgDTO.getReceiveId(); // 받는 사용자 번호

        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new Exception404("해당 유저가 없습니다"));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new Exception404("해당 유저가 없습니다"));

        // 방 생성 or 조회
        ChatRoom room = chatRoomRepository.findByUserIds(senderId,receiverId)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                        .userId1(sender)
                        .userId2(receiver)
                        .build()));


        // 메시지 저장
        ChatMessage chatMessage = msgDTO.toEntity(sender,receiver,room);
        chatMessage.setMessageType(MessageType.TEXT);
        chatMessageRepository.save(chatMessage);

        if(msgDTO.getImages() != null && !msgDTO.getImages().isEmpty()) {
            ChatImageRequestDTO.ChatImageDTO chatImageDTO = new ChatImageRequestDTO.ChatImageDTO();
            List<String> images = msgDTO.getImages();
            for(String img : images){
                String imageList = fileUploadUtil.uploadImage(img,uploadConfig.getChatDir());
                ChatImage chatImage = chatImageDTO.toEntity(imageList,chatMessage);
                chatImageRepository.save(chatImage);
            }
            chatMessage.setMessageType(MessageType.IMAGE);
            chatMessageRepository.save(chatMessage);
        }


        // 응답 DTO 생성
        List<ChatImage> images = chatImageRepository.findByChatMessage(chatMessage);
        ChatMessageResponseDTO.MessageDTO responseDTO =
                new ChatMessageResponseDTO.MessageDTO(chatMessage, images);
        String response = objectMapper.writeValueAsString(responseDTO);

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

