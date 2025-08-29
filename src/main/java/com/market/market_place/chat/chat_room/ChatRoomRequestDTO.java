package com.market.market_place.chat.chat_room;

import com.market.market_place.chat.chat_message.ChatMessageRequestDTO;
import com.market.market_place.members.domain.Member;
import lombok.Data;

public class ChatRoomRequestDTO {
    @Data
    public static class ChatRoomDTO{
        private Long userId1;
        private Long userId2;

        public ChatRoom toEntity(Member userId1,Member userId2){
            return ChatRoom.builder()
                    .userId1(userId1)
                    .userId2(userId2)
                    .build();
        }
    }
}
