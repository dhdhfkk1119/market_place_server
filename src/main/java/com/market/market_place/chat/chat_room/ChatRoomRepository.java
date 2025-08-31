package com.market.market_place.chat.chat_room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    

    // 내가 들어가있는 모든 방의 정보를 가져오기
    @Query("select cr from ChatRoom cr where cr.userId1 = :userId or cr.userId2 = :userId")
    List<ChatRoom> findAllByUser(@Param("userId") Long userId);


    // 방 생성 및 조회 -> 1번 유저가 3번한테 보냄 방없으면 생성 역순도 똑같음 둘다 유저를 비교
    // 하나의 방만 생김
    @Query("select cr from ChatRoom cr where " +
            "(cr.userId1 = :user1 and cr.userId2 = :user2) " +
            "OR (cr.userId1 = :user2 and cr.userId2 = :user1)")
    Optional<ChatRoom> findByUserIds(@Param("user1") Long user1, @Param("user2") Long user2);
}
