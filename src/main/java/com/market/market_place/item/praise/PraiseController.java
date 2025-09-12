package com.market.market_place.item.praise;


import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
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
<<<<<<< HEAD
    public ResponseEntity<PraiseResponse> addPraise(
            @RequestBody PraiseRequest praiseRequest,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        PraiseResponse praiseResponse = praiseService.addPraise(sessionUser, praiseRequest);
        return ResponseEntity.ok(praiseResponse);
=======
    public ResponseEntity<?> addPraise(@RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
                                       @Valid @RequestBody PraiseRequest request) {
        System.out.println("sessionUser: " + sessionUser);
        Long praiserId = sessionUser.getId();
        PraiseResponse response = praiseService.addPraise(praiserId, request);
        return ResponseEntity.ok(response);
>>>>>>> f-board
    }


}
