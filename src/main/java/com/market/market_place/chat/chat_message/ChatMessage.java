package com.market.market_place.chat.chat_message;

import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.chat._enum.MessageType;
import com.market.market_place.chat.chat_file.ChatFile;
import com.market.market_place.chat.chat_image.ChatImage;
import com.market.market_place.chat.chat_room.ChatRoom;
import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender; // 보는 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver; // 받는 사용자


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom; // 채팅 방

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MessageType messageType = MessageType.TEXT; // 메세지 형식

    private String message;

    @OneToMany(mappedBy = "chatMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "chatMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatFile> files = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdAt;

    public String getTime(){
        return DateUtil.chatFormat(createdAt);
    }

}
