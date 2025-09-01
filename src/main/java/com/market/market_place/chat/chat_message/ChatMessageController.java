package com.market.market_place.chat.chat_message;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.chat.chat_room.ChatRoomResponseDTO;
import com.market.market_place.members.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // 메세지 보내기
    @Auth(roles = {Member.MemberRole.USER, Member.MemberRole.ADMIN})
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageRequestDTO.Message dto,
                                         @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        ChatMessageResponseDTO.MessageDTO chatRoomDTO = chatMessageService.saveMessage(dto,sessionUser);
        return ResponseEntity.ok(chatRoomDTO);
    }

    // 내가 속해있는 방에 메세지를 전부 가져오기
    @Auth(roles = {Member.MemberRole.USER, Member.MemberRole.ADMIN})
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ChatMessageResponseDTO.MessageDTO>> getMessages(@PathVariable Long roomId,
                                                                               @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser) {
        List<ChatMessageResponseDTO.MessageDTO> messages = chatMessageService.getMessagesByRoom(roomId);
        return ResponseEntity.ok(messages);
    }
}
