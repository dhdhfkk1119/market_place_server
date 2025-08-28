package com.market.market_place.chat.chat_message;

import com.market.market_place.chat._enum.MessageType;
import com.market.market_place.chat.chat_image.ChatImage;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class ChatMessageResponseDTO {
    @Data
    public static class MessageDTO {
        private Long messageId;
        private Long roomId;
        private Long senderId;
        private Long receiverId;
        private MessageType messageType;
        private String message;
        private List<String> imageUrls;
        private String createdAt;

        @Builder
        public MessageDTO(ChatMessage chatMessage, List<ChatImage> chatImage){
            this.messageId = chatMessage.getId();
            this.roomId = chatMessage.getChatRoom().getId();
            this.senderId = chatMessage.getSendId();
            this.receiverId = chatMessage.getReceiveId();
            this.messageType = chatMessage.getMessageType();
            this.message = chatMessage.getMessage();
            this.imageUrls = chatImage.stream().map(ChatImage::getImageUrl).collect(Collectors.toList());
            this.createdAt = chatMessage.getTime();
        }

    }
}
