package com.market.market_place.chat.chat_message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    @Query("select m from ChatMessage m " +
            "join fetch m.sender s " +
            "join fetch s.memberProfile " +
            "join fetch m.receiver r " +
            "join fetch r.memberProfile " +
            "where m.chatRoom.id = :chatRoomId " +
            "order by m.createdAt DESC")
    Slice<ChatMessage> findMessagesByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);

}
