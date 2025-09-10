package com.market.market_place.chat.chat_room;

import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 내가 참여한 모든 방 조회
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping
    public ResponseEntity<?> getMyRooms(@RequestParam Long userId, Pageable pageable){
        Slice<ChatRoomResponseDTO.ChatRoomDTO> chatRoomDTO = chatRoomService.getMyChatRooms(userId,pageable);
        return ResponseEntity.ok(chatRoomDTO);
    }

    // 방에서 나가기 삭제하기
    @Auth(roles = {Role.ADMIN, Role.USER})
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId){
        chatRoomService.deleteRoom(roomId);
        return ResponseEntity.ok("정상적으로 방이 삭제되었습니다");
    }
    
}
