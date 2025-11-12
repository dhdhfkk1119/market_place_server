package markit.chat.chat_message;

import markit.chat.chat_room.ChatRoom;
import markit.item.core.Item;
import markit.members.domain.Member;
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
        private MultipartFile uploadFile;
        private String message;
        private Long itemId;

        public ChatMessage toEntity(Member sender, Member receiver, ChatRoom chatRoom, Item item){
            return ChatMessage.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .chatRoom(chatRoom)
                    .message(message)
                    .item(item)
                    .build();
        }
    }
}
