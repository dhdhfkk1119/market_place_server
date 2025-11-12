package markit.chat.chat_message;

import markit.chat._enum.MessageType;
import markit.chat.chat_image.ChatImage;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class ChatMessageResponseDTO {
    @Data
    public static class MessageDTO {
        private Long messageId;
        private Long roomId;
        private Long senderId;
        private Long receiverId;
        private String senderName;
        private String receiverName;
        private MessageType messageType;
        private String message;
        private List<String> imageUrls;
        private String createdAt;
        private Long itemId;
        private boolean isRead; // 상대방이 읽었는지 체크

        @Builder
        public MessageDTO(ChatMessage chatMessage, List<ChatImage> chatImage,boolean isRead){
            this.messageId = chatMessage.getId();
            this.roomId = chatMessage.getChatRoom().getId();
            this.senderId = chatMessage.getSender().getId();
            this.receiverId = chatMessage.getReceiver().getId();
            this.messageType = chatMessage.getMessageType();
            this.senderName = chatMessage.getSender().getMemberProfile().getName();
            this.receiverName = chatMessage.getReceiver().getMemberProfile().getName();
            this.message = chatMessage.getMessage();
            this.imageUrls = chatImage.stream().map(ChatImage::getImageUrl).collect(Collectors.toList());
            this.createdAt = chatMessage.getTime();
            this.itemId = chatMessage.getItem().getId();
            this.isRead = isRead;
        }
    }
}
