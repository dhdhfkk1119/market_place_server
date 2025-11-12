package markit.chat.chat_room;

import lombok.RequiredArgsConstructor;
import markit.chat.chat_message.ChatMessageRepository;
import markit.item.core.ItemService;
import markit.members.services.MemberService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final ItemService itemService;
    private final ChatMessageRepository chatMessageRepository;


    // 내가 참여한 방 목록 + 마지막 메시지
    public Slice<ChatRoomResponseDTO.ChatRoomDTO> getMyChatRooms(
            Long userId,
            Pageable pageable
    ) {
        Slice<ChatRoom> chatRooms = chatRoomRepository.findAllByUser(
                userId,
                pageable
        );

        return chatRooms.map(cr -> {
            // 1. 현재 사용자를 기준으로 상대방과 lastReadMessageId를 확인
            Long lastReadMessageId;
            Long otherUserId;
            if (cr.getLoginUser()
                    .getId()
                    .equals(userId)) {
                lastReadMessageId = cr.getLastReadMessageIdByLoginUser();
                otherUserId = cr.getOtherUser()
                        .getId();
            } else {
                lastReadMessageId = cr.getLastReadMessageIdByOtherUser();
                otherUserId = cr.getLoginUser()
                        .getId();
            }

            // 2. 안 읽은 메시지 개수 계산
            // 상대방이 보낸 메시지 중 마지막 읽음 메시지 ID 이후의 메시지 수를 셉니다.
            int unreadCount = 0;
            if (lastReadMessageId != null) {
                unreadCount = chatMessageRepository.countUnreadMessages(
                        cr.getId(),
                        otherUserId,
                        lastReadMessageId
                );
            } else {
                // lastReadMessageId가 null이면, 상대방이 보낸 모든 메시지가 안 읽은 메시지입니다.
                unreadCount = chatMessageRepository.countAllMessagesByRoomAndSender(
                        cr.getId(),
                        otherUserId
                );
            }

            return ChatRoomResponseDTO.ChatRoomDTO.builder()
                    .chatRoom(cr)
                    .currentUserId(userId)
                    .unreadCount(unreadCount) // DTO에 안 읽은 메시지 개수 추가
                    .build();
        });
    }


    // 방 나가기 -> 채팅방 삭제
    @Transactional
    public void deleteRoom(Long roomId) {
        chatRoomRepository.deleteById(roomId);
    }
}
