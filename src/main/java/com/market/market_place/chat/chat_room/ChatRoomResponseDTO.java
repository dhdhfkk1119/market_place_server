package com.market.market_place.chat.chat_room;

import com.market.market_place.chat.chat_message.ChatMessage;
import lombok.Builder;
import lombok.Data;

public class ChatRoomResponseDTO {
    @Data
    public static class ChatRoomDTO {
        private Long roomId;
        private Long userId1;
        private String user1Name;
        private String user2Name;
        private Long userId2;
        private String message;
        private String createdAt;

        @Builder
        public ChatRoomDTO(ChatRoom chatRoom, Long loginUserId) {
            this.roomId = chatRoom.getId();

            // 로그인한 사용자를 기준으로 상대방 ID 선택
            this.userId1 = chatRoom.getUserId1().getId().equals(loginUserId)
                    ? chatRoom.getUserId2().getId()
                    : chatRoom.getUserId1().getId();

            this.user1Name = chatRoom.getUserId1().getMemberProfile().getName();
            this.user2Name = chatRoom.getUserId2().getMemberProfile().getName(); // 여기 수정 필요

            ChatMessage lastMessage = chatRoom.getLastMessage();
            if (lastMessage != null) {
                this.message = lastMessage.getMessage();
                this.createdAt = lastMessage.getTime();
            } else {
                this.message = "";
                this.createdAt = "";
            }
        }

    }

}
