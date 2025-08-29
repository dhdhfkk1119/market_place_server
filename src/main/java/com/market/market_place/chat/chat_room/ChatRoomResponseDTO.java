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
        public ChatRoomDTO(ChatRoom chatRoom, ChatMessage lastMessage, Long loginUserId) {
            this.roomId = chatRoom.getId();
            this.userId1 = chatRoom.getUserId1().getId().equals(loginUserId)
                    ? chatRoom.getUserId2().getId()
                    : chatRoom.getUserId1().getId();
            this.user1Name = chatRoom.getUserId1().getMemberProfile().getName();
            this.user2Name = chatRoom.getUserId1().getMemberProfile().getName();

            if (lastMessage != null) {
                this.message = lastMessage.getMessage();
                this.createdAt = lastMessage.getTime();
            } else {
                this.message = ""; // 대화가 없을 경우 빈 문자열
                this.createdAt = ""; // or LocalDateTime.now().toString()
            }
        }
    }

}
