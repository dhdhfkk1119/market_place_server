package markit.chat.chat_message;

import markit._core._config.UploadConfig;
import markit._core._exception.Exception401;
import markit._core._exception.Exception403;
import markit._core._exception.Exception404;
import markit._core._utils.FileUploadUtil;
import markit._core._utils.JwtUtil;
import markit.chat._enum.MessageType;
import markit.chat.chat_image.ChatImage;
import markit.chat.chat_image.ChatImageRepository;
import markit.chat.chat_image.ChatImageRequestDTO;
import markit.chat.chat_room.ChatRoom;
import markit.chat.chat_room.ChatRoomRepository;
import markit.item.core.Item;
import markit.item.core.ItemService;
import markit.members.domain.Member;
import markit.members.services.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class ChatMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatImageRepository chatImageRepository;
    private final FileUploadUtil fileUploadUtil;
    private final UploadConfig uploadConfig;
    private final MemberService memberService;
    private final ItemService itemService;

    // 메세지 저장 및 방생성 (있으면 기존 방에서)
    @Transactional
    public ChatMessageResponseDTO.MessageDTO saveAndProcessMessage(Long senderId, ChatMessageRequestDTO.Message msgDTO) {
        Member sender = memberService.findMember(senderId);
        Member receiver = memberService.findMember(msgDTO.getReceiveId());


        Item item =  itemService.findItemById(msgDTO.getItemId());


        if (msgDTO.getMessage() == null || msgDTO.getMessage().trim().isEmpty()) {
            throw new Exception401("메시지를 입력해주시기 바랍니다");
        }

        ChatRoom room = chatRoomRepository.findByUserIds(senderId, msgDTO.getReceiveId(),item.getId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                        .loginUser(sender)
                        .otherUser(receiver)
                        .item(item)
                        .lastReadMessageIdByLoginUser(null)
                        .lastReadMessageIdByOtherUser(null)
                        .build()));

        ChatMessage chatMessage = msgDTO.toEntity(sender, receiver, room,item);
        chatMessage.setMessageType(MessageType.TEXT);
        chatMessageRepository.save(chatMessage);

        if (msgDTO.getImages() != null && !msgDTO.getImages().isEmpty()) {
            ChatImageRequestDTO.ChatImageDTO chatImageDTO = new ChatImageRequestDTO.ChatImageDTO();
            for (String img : msgDTO.getImages()) {
                String imageList = null;
                try {
                    imageList = fileUploadUtil.uploadImage(img, uploadConfig.getChatDir());
                    ChatImage chatImage = chatImageDTO.toEntity(imageList, chatMessage);
                    chatImageRepository.save(chatImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            chatMessage.setMessageType(MessageType.IMAGE);
            chatMessageRepository.save(chatMessage); // 메시지 타입 업데이트

        }

        // 방금 보낸 사람은 MessageId 업데이트 나는 읽었음
        if (room.getLoginUser().getId().equals(senderId)) {
            room.setLastReadMessageIdByLoginUser(chatMessage.getId());
        } else {
            room.setLastReadMessageIdByOtherUser(chatMessage.getId());
        }

        room.setLastMessage(chatMessage);
        chatRoomRepository.save(room);

        List<ChatImage> images = chatImageRepository.findByChatMessage(chatMessage);
        return new ChatMessageResponseDTO.MessageDTO(chatMessage, images ,true);

    }

    // 메세지 보내기 및 방 생성하기
    @Transactional
    public ChatMessageResponseDTO.MessageDTO saveMessage(ChatMessageRequestDTO.Message dto, JwtUtil.SessionUser sessionUser) {
        Member sender = memberService.findMember(sessionUser.getId()); // 보는 유저 번호
        Member receive = memberService.findMember(dto.getReceiveId()); // 받는 유저 번호
        Item item =  itemService.findItemById(dto.getItemId());

        // 이미 방이 있는지 없으면 새로 생성 orElseGet -> 값이 없을때만 실행
        ChatRoom room = chatRoomRepository.findByUserIds(sessionUser.getId(), dto.getReceiveId(),item.getId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                        .loginUser(sender)
                        .otherUser(receive)
                        .build()));

        ChatMessage chatMessage = dto.toEntity(sender, receive, room,item);
        chatMessageRepository.save(chatMessage);
        return new ChatMessageResponseDTO.MessageDTO(chatMessage, Collections.emptyList(),true);
    }

    // 내가 속한 한 방에 메세지 내역을 전부 가져오기
    public Slice<ChatMessageResponseDTO.MessageDTO> getMessagesByRoom(Long roomId, Long currentUserId, Pageable pageable) {
        // 1. 현재 사용자를 기준으로 채팅방 정보 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new Exception404("채팅방을 찾을 수 없습니다."));

        // 2. 상대방의 lastReadMessageId 가져오기
        Long otherUserLastReadMessageId = null;
        if (chatRoom.getLoginUser().getId().equals(currentUserId)) {
            otherUserLastReadMessageId = chatRoom.getLastReadMessageIdByOtherUser();
        } else {
            otherUserLastReadMessageId = chatRoom.getLastReadMessageIdByLoginUser();
        }

        // 3. 메시지 목록 조회
        Slice<ChatMessage> messages = chatMessageRepository.findMessagesByChatRoomId(roomId, pageable);

        log.info("[Backend Log] DB에서 가져온 메시지 개수={}, roomId={}",
                messages.getNumberOfElements(), roomId);

        // 4. 메시지 목록을 DTO로 변환하며 isRead 값 계산
        Long finalOtherUserLastReadMessageId = otherUserLastReadMessageId;

        return messages.map(msg -> {
            List<ChatImage> images = Collections.emptyList();
            if (msg.getMessageType() == MessageType.IMAGE) {
                images = chatImageRepository.findByChatMessage(msg);
            }
            boolean isRead = false;
            if (msg.getSender().getId().equals(currentUserId)) {
                isRead = (finalOtherUserLastReadMessageId != null && msg.getId() <= finalOtherUserLastReadMessageId);
            }

            return new ChatMessageResponseDTO.MessageDTO(msg, images, isRead);
        });


    }
    @Transactional
    public void markMessagesAsRead(Long roomId, Long currentUserId) {
        // 1. 해당 채팅방의 가장 최신 메시지 ID를 찾습니다.
        ChatMessage latestMessage = chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(roomId)
                .orElse(null);

        // 만약 방에 메시지가 없다면, 읽을 메시지도 없으므로 바로 종료합니다.
        if (latestMessage == null) {
            return;
        }

        Long latestMessageId = latestMessage.getId();

        // 2. 현재 사용자가 채팅방에서 어떤 사용자인지(loginUser인지 otherUser인지) 확인합니다.
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new Exception404("채팅방을 찾을 수 없습니다."));

        // 3. 사용자의 역할에 맞는 lastReadMessageId를 업데이트합니다.
        if (chatRoom.getLoginUser().getId().equals(currentUserId)) {
            chatRoomRepository.updateLastReadMessageIdForLoginUser(roomId, currentUserId, latestMessageId);
        } else if (chatRoom.getOtherUser().getId().equals(currentUserId)) {
            chatRoomRepository.updateLastReadMessageIdForOtherUser(roomId, currentUserId, latestMessageId);
        } else {
            throw new Exception403("해당 채팅방의 멤버가 아닙니다.");
        }
    }

}
