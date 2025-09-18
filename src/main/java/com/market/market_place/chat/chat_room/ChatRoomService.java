package com.market.market_place.chat.chat_room;

import com.market.market_place._core._config.UploadConfig;
import com.market.market_place._core._utils.FileUploadUtil;
import com.market.market_place.chat.chat_image.ChatImageRepository;
import com.market.market_place.chat.chat_message.ChatMessage;
import com.market.market_place.chat.chat_message.ChatMessageRepository;
import com.market.market_place.chat.chat_message.ChatMessageRequestDTO;
import com.market.market_place.chat.chat_message.ChatMessageService;
import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemService;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.market.market_place.item.core.QItem.item;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final ItemService itemService;
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
