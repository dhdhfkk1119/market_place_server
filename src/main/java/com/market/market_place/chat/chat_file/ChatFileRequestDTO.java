package com.market.market_place.chat.chat_file;

import com.market.market_place.chat.chat_message.ChatMessage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class ChatFileRequestDTO {

    @Data
    public static class ChatFileDTO{
        private Long chatMessageId;
        private String uploadFile;

        public ChatFile toEntity(ChatMessage chatMessage,String uploadFileUrl){
            return ChatFile.builder()
                    .chatMessage(chatMessage)
                    .uploadFileUrl(uploadFileUrl)
                    .build();
        }
    }
}
