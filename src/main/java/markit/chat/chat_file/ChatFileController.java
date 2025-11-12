package markit.chat.chat_file;

import markit._core._utils.JwtUtil;
import markit._core.auth.Auth;
import markit.chat.chat_message.ChatMessageRequestDTO;
import markit.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatFileController {

    private final ChatFileService chatFileService;

    @Auth(roles = {Role.ADMIN, Role.USER})
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
