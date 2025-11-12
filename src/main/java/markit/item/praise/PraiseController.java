package markit.item.praise;


import markit._core._utils.JwtUtil;
import markit._core.auth.Auth;
import markit.members.domain.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/praise")
public class PraiseController {
    private final PraiseService praiseService;

    @Auth(roles = Role.USER)
    @PostMapping
    public ResponseEntity<PraiseResponse> addPraise(
            @RequestBody PraiseRequest praiseRequest,
            @Valid @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        PraiseResponse praiseResponse = praiseService.addPraise(sessionUser.getId(), praiseRequest);
        return ResponseEntity.ok(praiseResponse);


    }
}
