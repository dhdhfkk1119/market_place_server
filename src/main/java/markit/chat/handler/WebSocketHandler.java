//package markit.chat.handler;
//
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import markit._core._config.UploadConfig;
//import markit._core._exception.Exception404;
//import markit._core._utils.FileUploadUtil;
//import markit._core._utils.JwtUtil;
//import markit.chat._enum.MessageType;
//import markit.chat.chat_file.ChatFileRequestDTO;
//import markit.chat.chat_image.ChatImage;
//import markit.chat.chat_image.ChatImageRepository;
//import markit.chat.chat_image.ChatImageRequestDTO;
//import markit.chat.chat_message.*;
//import markit.chat.chat_room.ChatRoom;
//import markit.chat.chat_room.ChatRoomRepository;
//import markit.members.domain.Member;
//import markit.members.repositories.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//@RequiredArgsConstructor
//public class WebSocketHandler extends TextWebSocketHandler {
//
//    private final ObjectMapper objectMapper;
//    private final ChatMessageService chatMessageService;
//    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
//
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        String token = UriComponentsBuilder.fromUri(session.getUri()).build().getQueryParams().getFirst("token");
//        if (token != null && !token.isBlank()) {
//            try {
//                JwtUtil.SessionUser sessionUser = JwtUtil.verifyAndReturnSessionUser(token);
//                session.getAttributes().put("userId", sessionUser.getId());
//                sessions.put(sessionUser.getId(),session);
//            } catch (JWTVerificationException e) {
//                session.close(CloseStatus.BAD_DATA.withReason("Invalid JWT token."));
//            }
//        }
//    }
//
//    /**
//     * 클라이언트가 보낸 메시지를 처리
//     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        ChatMessageRequestDTO.Message msgDTO = objectMapper.readValue(payload, ChatMessageRequestDTO.Message.class);
//
//        Long senderId = (Long) session.getAttributes().get("userId");
//        if (senderId == null) {
//            session.close(CloseStatus.BAD_DATA.withReason("User not authenticated."));
//            return;
//        }
//
//        // 서비스 계층의 메서드를 호출하여 비즈니스 로직 처리
//        ChatMessageResponseDTO.MessageDTO responseDTO = chatMessageService.saveAndProcessMessage(senderId, msgDTO);
//        String response = objectMapper.writeValueAsString(responseDTO);
//
//        // 메시지 전송 로직
//        session.sendMessage(new TextMessage(response));
//        WebSocketSession receiverSession = sessions.get(msgDTO.getReceiveId());
//        if (receiverSession != null && receiverSession.isOpen()) {
//            receiverSession.sendMessage(new TextMessage(response));
//        }
//    }
//
//    /**
//     * 연결 종료 시 세션 정리
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.values().remove(session);
//        System.out.println("세션 종료됨");
//    }
//}
//
