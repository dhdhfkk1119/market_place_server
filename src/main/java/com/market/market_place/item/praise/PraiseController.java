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
    public ResponseEntity<PraiseResponse> addPraise(
            @RequestBody PraiseRequest praiseRequest,
            @Valid @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        PraiseResponse praiseResponse = praiseService.addPraise(sessionUser.getId(), praiseRequest);
        return ResponseEntity.ok(praiseResponse);


    }
}
