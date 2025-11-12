package markit.chat.chat_image;

import markit.chat.chat_message.ChatMessage;
import lombok.Data;

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
