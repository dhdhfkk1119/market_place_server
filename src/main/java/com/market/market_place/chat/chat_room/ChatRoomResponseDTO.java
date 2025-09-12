package com.market.market_place.chat.chat_room;

import com.market.market_place.chat.chat_message.ChatMessage;
import lombok.Builder;
import lombok.Data;

public class ChatRoomResponseDTO {
    @Data
    public static class ChatRoomDTO {
        private Long roomId;
        private Long loginId;
        private String loginUserName;
        private String otherUserName;
        private Long otherId;
        private String message;
        private String createdAt;

        @Builder
        public ChatRoomDTO(ChatRoom chatRoom, Long loginUserId) {
            this.roomId = chatRoom.getId();

            // 로그인한 사용자를 기준으로 상대방 ID 선택
            this.loginId = chatRoom.getLoginUser().getId().equals(loginUserId)
                    ? chatRoom.getOtherUser().getId()
                    : chatRoom.getLoginUser().getId();

            this.loginUserName = chatRoom.getLoginUser().getMemberProfile().getName();
            this.otherUserName = chatRoom.getOtherUser().getMemberProfile().getName(); // 여기 수정 필요

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
