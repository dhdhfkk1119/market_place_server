package com.market.market_place.chat.chat_message;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.chat.chat_room.ChatRoomResponseDTO;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate; // 메시지 브로커에 메시지를 전송하는 역할

    // 메세지 보내기
    @Auth(roles = {Role.ADMIN, Role.USER})
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageRequestDTO.Message dto,
                                         @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        ChatMessageResponseDTO.MessageDTO chatRoomDTO = chatMessageService.saveMessage(dto,sessionUser);
        return ResponseEntity.ok(chatRoomDTO);
    }

    // 내가 속해있는 방에 메세지를 전부 가져오기
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ChatMessageResponseDTO.MessageDTO>> getMessages(@PathVariable Long roomId,
                                                                               @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser) {
        List<ChatMessageResponseDTO.MessageDTO> messages = chatMessageService.getMessagesByRoom(roomId);
        return ResponseEntity.ok(messages);
    }



    // 클라이언트가 /app/chat/sendMessage 로 메시지를 보낼 때 이 메서드가 처리
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatMessageRequestDTO.Message msgDTO, SimpMessageHeaderAccessor headerAccessor) {
        // 세션에서 사용자 ID 가져오기 (JwtHandshakeInterceptor에서 처리된 속성)
        Long senderId = (Long) headerAccessor.getSessionAttributes().get("userId");

        // 비즈니스 로직 처리 (서비스 계층)
        ChatMessageResponseDTO.MessageDTO responseDTO = chatMessageService.saveAndProcessMessage(senderId, msgDTO);

        // 메시지 브로커를 통해 메시지 전송
        // 1:1 메시지는 /queue/chat/message.{receiverId} 와 같은 개인 큐를 사용하는 것이 일반적
        // 상대방에게 메시지 전송
        messagingTemplate.convertAndSendToUser(
                String.valueOf(msgDTO.getReceiveId()), // 수신자 ID
                "/queue/chat/message", // 메시지 목적지
                responseDTO // 보낼 메시지
        );

        // 발신자에게도 메시지 전송
        messagingTemplate.convertAndSendToUser(
                String.valueOf(senderId),
                "/queue/chat/message",
                responseDTO
        );
    }
}
