package com.market.market_place.chat.chat_image;

import com.market.market_place.chat.chat_message.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatImageRepository extends JpaRepository<ChatImage,Long> {
    @Query("select ci from  ChatImage ci where ci.chatMessage = :chatMessage")
    List<ChatImage> findByChatMessage(@Param("chatMessage")ChatMessage chatMessage);
}
