package com.market.market_place.chat.chat_message;

import com.market.market_place.chat.chat_room.ChatRoomResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageRequestDTO.Message dto){
        ChatMessageResponseDTO.MessageDTO chatRoomDTO = chatMessageService.saveMessage(dto);
        return ResponseEntity.ok(chatRoomDTO);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ChatMessageResponseDTO.MessageDTO>> getMessages(@PathVariable Long roomId) {
        List<ChatMessageResponseDTO.MessageDTO> messages = chatMessageService.getMessagesByRoom(roomId);
        return ResponseEntity.ok(messages);
    }
}
