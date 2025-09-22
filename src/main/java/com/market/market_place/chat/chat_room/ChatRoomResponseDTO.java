package com.market.market_place.chat.chat_room;

import com.market.market_place.chat.chat_message.ChatMessage;
import com.market.market_place.item.core.Item;
import com.market.market_place.members.domain.Member;
import lombok.Builder;
import lombok.Data;

public class ChatRoomResponseDTO {
    @Data
    public static class ChatRoomDTO {
        private Long roomId;
        private Long otherUserId;
        private String otherUserName;
        private String lastMessage;
        private String lastMessageCreatedAt;
        private Long itemId;
        private int unreadMessageCount; // 읽지않은 메제시 수

        @Builder
        public ChatRoomDTO(ChatRoom chatRoom, Long currentUserId, int unreadCount) {
            Member otherUser = chatRoom.getLoginUser().getId().equals(currentUserId)
                    ? chatRoom.getOtherUser()
                    : chatRoom.getLoginUser();

            this.roomId = chatRoom.getId();
            this.otherUserId = otherUser.getId();
            this.otherUserName = otherUser.getMemberProfile().getName();

            ChatMessage lastMessage = chatRoom.getLastMessage();
            if (lastMessage != null) {
                this.lastMessage = lastMessage.getMessage();
                this.lastMessageCreatedAt = lastMessage.getTime();
            } else {
                this.lastMessage = "";
                this.lastMessageCreatedAt = "";
            }
            this.unreadMessageCount = unreadCount;

            this.itemId = chatRoom.getItem().getId();
        }

    }

}
