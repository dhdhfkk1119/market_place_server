package com.market.market_place.chat.chat_message;

import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.chat._enum.MessageType;
import com.market.market_place.chat.chat_room.ChatRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Table(name = "chat_message_tb")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sendId;
    private Long receiveId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MessageType messageType = MessageType.TEXT;

    private String message;

    @CreationTimestamp
    private Timestamp createdAt;

    public String getTime(){
        return DateUtil.chatFormat(createdAt);
    }

}
