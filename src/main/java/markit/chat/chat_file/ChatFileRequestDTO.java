package markit.chat.chat_file;

import markit.chat.chat_message.ChatMessage;
import lombok.Data;

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
