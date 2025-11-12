package markit.chat.chat_room;

import markit._core._utils.JwtUtil;
import markit._core.auth.Auth;
import markit.chat.chat_message.ChatMessageRequestDTO;
import markit.chat.chat_message.ChatMessageResponseDTO;
import markit.chat.chat_message.ChatMessageService;
import markit.members.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/rooms")
@Slf4j
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    // 내가 참여한 모든 방 조회
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping
    public ResponseEntity<?> getMyRooms(@RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser,
                                        Pageable pageable){
        log.info("채팅방 목록 요청: userId = {}, pageable = {}", sessionUser.getId(), pageable);

        Slice<ChatRoomResponseDTO.ChatRoomDTO> chatRoomDTO = chatRoomService.getMyChatRooms(sessionUser.getId(),pageable);
        return ResponseEntity.ok(chatRoomDTO);
    }

    // 방에서 나가기 삭제하기
    @Auth(roles = {Role.ADMIN, Role.USER})
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId){
        chatRoomService.deleteRoom(roomId);
        return ResponseEntity.ok("정상적으로 방이 삭제되었습니다");
    }


    @Auth(roles = {Role.ADMIN, Role.USER})
    @PostMapping("/create")
    public ResponseEntity<?> createRoomWithMessage(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @RequestBody ChatMessageRequestDTO.Message msgDTO) {

        ChatMessageResponseDTO.MessageDTO response = chatMessageService.saveAndProcessMessage(sessionUser.getId(), msgDTO);
        return ResponseEntity.ok(response);
    }


}
