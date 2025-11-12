package markit.chat.chat_room;

import markit.members.domain.Member;
import lombok.Data;

public class ChatRoomRequestDTO {
    @Data
    public static class ChatRoomDTO{
        private Long loginUser;
        private Long otherUser;
        private Long itemId;

        public ChatRoom toEntity(Member userId1,Member userId2){
            return ChatRoom.builder()
                    .loginUser(userId1)
                    .otherUser(userId2)
                    .build();
        }
    }
}
