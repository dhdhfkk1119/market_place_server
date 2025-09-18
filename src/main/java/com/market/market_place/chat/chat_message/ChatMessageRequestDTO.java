package com.market.market_place.chat.chat_message;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.chat._enum.MessageType;
import com.market.market_place.chat.chat_room.ChatRoom;
import com.market.market_place.item.core.Item;
import com.market.market_place.members.domain.Member;
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
