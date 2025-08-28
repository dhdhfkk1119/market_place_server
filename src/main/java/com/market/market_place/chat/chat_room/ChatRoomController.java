package com.market.market_place.chat.chat_room;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 내가 참여한 모든 방 조회
    @GetMapping
    public ResponseEntity<?> getMyRooms(@RequestParam Long userId){
        List<ChatRoomResponseDTO.ChatRoomDTO> chatRoomDTO = chatRoomService.getMyChatRooms(userId);
        return ResponseEntity.ok(chatRoomDTO);
    }

    // 특정 상대방과의 방 가져오기/생성
    @PostMapping("/get-or-create")
    public ChatRoom getOrCreateRoom(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return chatRoomService.getOrCreateRoom(senderId, receiverId);
    }


}
