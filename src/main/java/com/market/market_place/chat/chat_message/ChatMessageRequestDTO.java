package com.market.market_place.chat.chat_message;

import com.market.market_place.chat._enum.MessageType;
import com.market.market_place.chat.chat_room.ChatRoom;
import lombok.Data;

import java.util.List;

public class ChatMessageRequestDTO {
    @Data
    public static class Message{
        private Long sendId;
        private Long receiveId;
        private Long roomId;
        private MessageType messageType;
        private List<String> Images;
        private String message;

        public ChatMessage toEntity(ChatRoom chatRoom){
            return ChatMessage.builder()
                    .sendId(sendId)
                    .receiveId(receiveId)
                    .chatRoom(chatRoom)
                    .messageType(messageType)
                    .message(message)
                    .build();
        }
    }
}
