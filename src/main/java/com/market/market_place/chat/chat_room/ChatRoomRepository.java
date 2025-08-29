package com.market.market_place.chat.chat_room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    // 유저 1 , 유저 2 가 들어있는 방을 찾아서 보여줌
    @Query("select cr from ChatRoom cr where cr.userId1 = :userId1 and cr.userId2 = :userId2")
    Optional<ChatRoom> findByUserId1AndUserId2(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    // 내가 들어가있는 모든 방의 정보를 가져오기
    @Query("select cr from ChatRoom cr where cr.userId1 = :userId or cr.userId2 = :userId")
    List<ChatRoom> findAllByUser(@Param("userId") Long userId);


    @Query("SELECT cr FROM ChatRoom cr WHERE " +
            "(cr.userId1 = :user1 AND cr.userId2 = :user2) " +
            "OR (cr.userId1 = :user2 AND cr.userId2 = :user1)")
    Optional<ChatRoom> findByUserIds(@Param("user1") Long user1, @Param("user2") Long user2);
}
