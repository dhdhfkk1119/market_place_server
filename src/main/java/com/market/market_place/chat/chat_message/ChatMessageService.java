package com.market.market_place.chat.chat_message;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.chat.chat_room.ChatRoom;
import com.market.market_place.chat.chat_room.ChatRoomRepository;
import com.market.market_place.chat.chat_room.ChatRoomResponseDTO;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberProfile;
import com.market.market_place.members.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MemberService memberService;

    // 메세지 보내기 및 방 생성하기
    public ChatMessageResponseDTO.MessageDTO saveMessage(ChatMessageRequestDTO.Message dto, JwtUtil.SessionUser sessionUser){
        Member sender = memberService.findMember(sessionUser.getId()); // 보는 유저 번호
        Member receive = memberService.findMember(dto.getReceiveId()); // 받는 유저 번호

        // 이미 방이 있는지 없으면 새로 생성 orElseGet -> 값이 없을때만 실행 
        ChatRoom room = chatRoomRepository.findByUserIds(sessionUser.getId(),dto.getReceiveId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                                .userId1(sender)
                                .userId2(receive)
                        .build()));

        ChatMessage chatMessage = dto.toEntity(sender,receive,room);
        chatMessageRepository.save(chatMessage);
        return new ChatMessageResponseDTO.MessageDTO(chatMessage, Collections.emptyList());
    }

    // 내가 속한 한 방에 메세지 내역을 전부 가져오기
    public List<ChatMessageResponseDTO.MessageDTO> getMessagesByRoom(Long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findMessagesByChatRoomId(roomId);

        return messages.stream()
                .map(msg -> new ChatMessageResponseDTO.MessageDTO(msg, Collections.emptyList()))
                .collect(Collectors.toList());
    }

}
