package com.market.market_place.chat.chat_message;

import com.market.market_place._core._config.UploadConfig;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.FileUploadUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.chat._enum.MessageType;
import com.market.market_place.chat.chat_image.ChatImage;
import com.market.market_place.chat.chat_image.ChatImageRepository;
import com.market.market_place.chat.chat_image.ChatImageRequestDTO;
import com.market.market_place.chat.chat_room.ChatRoom;
import com.market.market_place.chat.chat_room.ChatRoomRepository;
import com.market.market_place.chat.chat_room.ChatRoomResponseDTO;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberProfile;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.members.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatImageRepository chatImageRepository;
    private final FileUploadUtil fileUploadUtil;
    private final UploadConfig uploadConfig;
    private final MemberRepository memberRepository;

    // 메세지 보내기 및 방 생성하기
    public ChatMessageResponseDTO.MessageDTO saveAndProcessMessage(Long senderId, ChatMessageRequestDTO.Message msgDTO) {

        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new Exception404("해당 유저가 없습니다"));
        Member receiver = memberRepository.findById(msgDTO.getReceiveId())
                .orElseThrow(() -> new Exception404("해당 유저가 없습니다"));


        ChatRoom room = chatRoomRepository.findByUserIds(senderId, msgDTO.getReceiveId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                        .userId1(sender)
                        .userId2(receiver)
                        .build()));


        ChatMessage chatMessage = msgDTO.toEntity(sender, receiver, room);
        chatMessage.setMessageType(MessageType.TEXT);
        chatMessageRepository.save(chatMessage);


        if (msgDTO.getImages() != null && !msgDTO.getImages().isEmpty()) {
            ChatImageRequestDTO.ChatImageDTO chatImageDTO = new ChatImageRequestDTO.ChatImageDTO();
            for (String img : msgDTO.getImages()) {
                String imageList = null;
                try {
                    imageList = fileUploadUtil.uploadImage(img, uploadConfig.getChatDir());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ChatImage chatImage = chatImageDTO.toEntity(imageList, chatMessage);
                chatImageRepository.save(chatImage);
            }
            chatMessage.setMessageType(MessageType.IMAGE);
            chatMessageRepository.save(chatMessage); // 메시지 타입 업데이트
        }


        List<ChatImage> images = chatImageRepository.findByChatMessage(chatMessage);
        return new ChatMessageResponseDTO.MessageDTO(chatMessage, images);
    }

    // 내가 속한 한 방에 메세지 내역을 전부 가져오기
    public List<ChatMessageResponseDTO.MessageDTO> getMessagesByRoom(Long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findMessagesByChatRoomId(roomId);

        return messages.stream()
                .map(msg -> new ChatMessageResponseDTO.MessageDTO(msg, Collections.emptyList()))
                .collect(Collectors.toList());
    }

}
