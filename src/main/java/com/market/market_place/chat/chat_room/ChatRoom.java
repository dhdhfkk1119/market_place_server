package com.market.market_place.chat.chat_room;

import com.market.market_place._core._utils.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

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

    private Long userId1;
    private Long userId2;

    @CreationTimestamp
    private Timestamp createdAt;

    public String getTime(){
        return DateUtil.chatFormat(createdAt);
    }

}
