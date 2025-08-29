package com.market.market_place.chat.chat_room;

import com.market.market_place._core._utils.DateUtil;
import com.market.market_place.chat.chat_message.ChatMessage;
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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_room_tb")
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId1")
    private Member userId1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId2")
    private Member userId2;

    @CreationTimestamp
    private Timestamp createdAt;


    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public String getTime(){
        return DateUtil.chatFormat(createdAt);
    }

}
