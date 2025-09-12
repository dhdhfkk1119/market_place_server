package com.market.market_place.chat.chat_message;

import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Slf4j
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
    public ResponseEntity<Slice<ChatMessageResponseDTO.MessageDTO>> getMessages(@PathVariable Long roomId,
                                                                               @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser,
                                                                               Pageable pageable) {
        Slice<ChatMessageResponseDTO.MessageDTO> messages = chatMessageService.getMessagesByRoom(roomId,pageable);
        return ResponseEntity.ok(messages);
    }



    // 클라이언트가 /app/chat/sendMessage 로 메시지를 보낼 때 이 메서드가 처리
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatMessageRequestDTO.Message msgDTO,
                            SimpMessageHeaderAccessor headerAccessor) {


        log.info("[Backend Log] STOMP message received.");

        Long senderId = (Long) headerAccessor.getSessionAttributes().get("userId");
        log.info("[Backend Log] Sender ID from session: {}", senderId);

        if (senderId == null) {
            // 세션에서 userId 못 찾았을 때 → Interceptor에서 제대로 안 넣어줬다는 뜻
            throw new Exception401("세션에서 사용자 정보를 찾을 수 없습니다 (JWT 인증 실패 가능)");
        }
        log.info("[Backend Log] Processing message for receiver: {}, message: '{}'",
                msgDTO.getReceiveId(), msgDTO.getMessage());


        try {
            // 비즈니스 로직 처리 (서비스 계층)
            ChatMessageResponseDTO.MessageDTO responseDTO =
                    chatMessageService.saveAndProcessMessage(senderId, msgDTO);

            log.info("[Backend Log] Message saved. Sending back to sender and receiver.");


            // 메시지 브로커를 통해 메시지 전송
            // 상대방에게 메시지 전송
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(msgDTO.getReceiveId()),
                    "/queue/chat/message",
                    responseDTO
            );

            // 발신자에게도 메시지 전송
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(senderId),
                    "/queue/chat/message",
                    responseDTO
            );

        } catch (Exception e) {
            // 원인 로그를 찍어야 정확한 문제 파악 가능
            log.error("[Backend Log] Error during message processing: {}", e.getMessage(), e);
            throw new RuntimeException("메시지 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

}
