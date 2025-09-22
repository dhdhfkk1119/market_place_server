package com.market.market_place.chat.chat_room;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    

    // 내가 들어가있는 모든 방의 정보를 가져오기
    @Query("select cr from ChatRoom cr " +
            "left join fetch cr.lastMessage " +
            "where cr.loginUser.id = :userId or cr.otherUser.id = :userId " +
            "order by cr.lastMessage.createdAt desc")
    Slice<ChatRoom> findAllByUser(@Param("userId") Long userId, Pageable pageable);


    // 방 생성 및 조회 -> 1번 유저가 3번한테 보냄 방없으면 생성 역순도 똑같음 둘다 유저를 비교
    // 하나의 방만 생김
    @Query("select cr from ChatRoom cr where " +
            "((cr.loginUser.id = :senderId and cr.otherUser.id = :receiverId) " +
            "OR (cr.loginUser.id = :receiverId and cr.otherUser.id = :senderId)) " +
            "AND cr.item.id = :itemId")
    Optional<ChatRoom> findByUserIds(@Param("senderId") Long senderId,
                                     @Param("receiverId") Long receiverId,
                                     @Param("itemId") Long itemId);

    @Modifying
    @Transactional
    @Query("UPDATE ChatRoom cr SET " +
            "cr.lastReadMessageIdByLoginUser = :latestMessageId " +
            "WHERE cr.id = :roomId AND cr.loginUser.id = :currentUserId")
    int updateLastReadMessageIdForLoginUser(@Param("roomId") Long roomId,
                                            @Param("currentUserId") Long currentUserId,
                                            @Param("latestMessageId") Long latestMessageId);

    @Modifying
    @Transactional
    @Query("UPDATE ChatRoom cr SET " +
            "cr.lastReadMessageIdByOtherUser = :latestMessageId " +
            "WHERE cr.id = :roomId AND cr.otherUser.id = :currentUserId")
    int updateLastReadMessageIdForOtherUser(@Param("roomId") Long roomId,
                                            @Param("currentUserId") Long currentUserId,
                                            @Param("latestMessageId") Long latestMessageId);

}
