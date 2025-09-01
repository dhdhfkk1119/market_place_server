package com.market.market_place.chat.chat_file;

import com.market.market_place._core._config.UploadConfig;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.FileUploadUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.chat._enum.MessageType;
import com.market.market_place.chat.chat_image.ChatImageRepository;
import com.market.market_place.chat.chat_message.ChatMessage;
import com.market.market_place.chat.chat_message.ChatMessageRepository;
import com.market.market_place.chat.chat_message.ChatMessageRequestDTO;
import com.market.market_place.chat.chat_room.ChatRoom;
import com.market.market_place.chat.chat_room.ChatRoomRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.members.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ChatFileService {

    private final ChatMessageRepository chatMessageRepository;
    private final FileUploadUtil fileUploadUtil;
    private final MemberService memberService;
    private final ChatRoomRepository chatRoomRepository;
    private final UploadConfig uploadConfig;

    // 파일 업로드 처리
    public void fileUpload(Long roomId,ChatMessageRequestDTO.Message msgDTO, JwtUtil.SessionUser sessionUser) {

        Long receiverId = msgDTO.getReceiveId(); // 받는 사용자 번호

        Member sender = memberService.findMember(sessionUser.getId());
        Member receiver = memberService.findMember(msgDTO.getReceiveId());

        msgDTO.setRoomId(roomId);

        // 방 생성 or 조회
        ChatRoom room = chatRoomRepository.findByUserIds(sessionUser.getId(),receiverId)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                        .userId1(sender)
                        .userId2(receiver)
                        .build()));


        ChatMessage chatMessage = msgDTO.toEntity(sender,receiver,room);

        MultipartFile file = msgDTO.getUploadFile();

        if (file != null && !file.isEmpty()) {

            ChatFileRequestDTO.ChatFileDTO chatDTO = new ChatFileRequestDTO.ChatFileDTO();

            // 파일 저장 (ex: /uploads/chat/..)
            String chatUploadUrl = null;
            try {
                chatUploadUrl = fileUploadUtil.uploadFile(file, uploadConfig.getChatFileDir());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // 파일 엔티티 변환
            chatDTO.toEntity(chatMessage, chatUploadUrl);

            // 메시지 타입 지정
            chatMessage.setMessageType(MessageType.FILE);

            chatMessageRepository.save(chatMessage);
        } else {
            // 텍스트 메시지 저장 로직
            chatMessage.setMessageType(MessageType.TEXT);
            chatMessageRepository.save(chatMessage);
        }
    }

}
