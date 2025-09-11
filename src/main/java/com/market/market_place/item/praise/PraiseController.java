package com.market.market_place.item.praise;


import com.market.market_place._core._utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/praise")
public class PraiseController {
    private final PraiseService praiseService;

    @PostMapping
    public ResponseEntity<PraiseResponse> addPraise(
            @RequestBody PraiseRequest praiseRequest,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        PraiseResponse praiseResponse = praiseService.addPraise(sessionUser, praiseRequest);
        return ResponseEntity.ok(praiseResponse);
    }

}
