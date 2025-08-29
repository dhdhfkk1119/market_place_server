package com.market.market_place.chat.chat_image;

import com.market.market_place.chat.chat_message.ChatMessage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class ChatImageRequestDTO {

    @Data
    public static class ChatImageDTO {
        private Long chatMessageId;
        private String imageUrl;

        public ChatImage toEntity(String imageUrl, ChatMessage chatMessage){
            return ChatImage.builder()
                    .chatMessage(chatMessage)
                    .imageUrl(imageUrl)
                    .build();
        }
    }
}
