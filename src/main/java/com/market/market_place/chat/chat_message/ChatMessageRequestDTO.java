package com.market.market_place.chat.chat_message;

import com.market.market_place.chat._enum.MessageType;
import com.market.market_place.chat.chat_room.ChatRoom;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageRequestDTO {
    @Data
    public static class Message{
        private Long sendId;
        private Long receiveId;
        private Long roomId;
        private List<String> images = new ArrayList<>();
        private MessageType messageType;
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
