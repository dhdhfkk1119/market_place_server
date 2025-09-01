package com.market.market_place.chat.chat_file;

import com.market.market_place._core._config.UploadConfig;
import com.market.market_place._core._utils.FileUploadUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.chat._enum.MessageType;
import com.market.market_place.chat.chat_message.ChatMessage;
import com.market.market_place.chat.chat_message.ChatMessageRepository;
import com.market.market_place.chat.chat_message.ChatMessageRequestDTO;
import com.market.market_place.members.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatFileController {

    private final ChatFileService chatFileService;

    @Auth(roles = {Member.MemberRole.ADMIN, Member.MemberRole.USER})
    @PostMapping("/{roomId}/upload")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long roomId,
            @ModelAttribute ChatMessageRequestDTO.Message msgDTO,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser) {

        msgDTO.setUploadFile(file); // DTO에 파일 주입

        chatFileService.fileUpload(roomId,msgDTO,sessionUser);

        return ResponseEntity.ok("파일 업로드 성공");
    }

}
