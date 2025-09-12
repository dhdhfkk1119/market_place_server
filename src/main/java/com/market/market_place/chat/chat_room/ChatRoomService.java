package com.market.market_place.chat.chat_room;

import com.market.market_place.chat.chat_message.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 내가 참여한 방 목록 + 마지막 메시지
    public Slice<ChatRoomResponseDTO.ChatRoomDTO> getMyChatRooms(Long userId, Pageable pageable) {
        Slice<ChatRoom> chatRooms = chatRoomRepository.findAllByUser(userId, pageable);

        return chatRooms.map(cr -> {
            return ChatRoomResponseDTO.ChatRoomDTO.builder()
                    .chatRoom(cr)
                    .currentUserId(userId)
                    .build();

        });
    }



    // 방 나가기 -> 채팅방 삭제
    @Transactional
    public void deleteRoom(Long roomId){
        chatRoomRepository.deleteById(roomId);
    }
}
