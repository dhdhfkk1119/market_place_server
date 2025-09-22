package com.market.market_place.chat.chat_message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    @Query("select m from ChatMessage m " +
            "join fetch m.sender s " +
            "join fetch s.memberProfile " +
            "join fetch m.receiver r " +
            "join fetch r.memberProfile " +
            "where m.chatRoom.id = :chatRoomId " +
            "order by m.createdAt ASC")
    Slice<ChatMessage> findMessagesByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);


    // 마지막 읽음 메시지 ID 이후의 메시지 개수를 세는 쿼리
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.chatRoom.id = :roomId AND m.sender.id = :senderId AND m.id > :lastReadMessageId")
    int countUnreadMessages(@Param("roomId") Long roomId, @Param("senderId") Long senderId, @Param("lastReadMessageId") Long lastReadMessageId);

    // lastReadMessageId가 null일 경우, 상대방이 보낸 모든 메시지 개수를 셉니다.
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.chatRoom.id = :roomId AND m.sender.id = :senderId")
    int countAllMessagesByRoomAndSender(@Param("roomId") Long roomId, @Param("senderId") Long senderId);

    Optional<ChatMessage> findFirstByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

}
