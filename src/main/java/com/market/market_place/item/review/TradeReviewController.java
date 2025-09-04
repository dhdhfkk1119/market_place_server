package com.market.market_place.item.review;


import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trade/review")
public class TradeReviewController {

    private final TradeReviewService tradeReviewService;

    @PostMapping("/{tradeId}/reviews/{reviewerId}")
    public ResponseEntity<?> createReview(
            @PathVariable Long tradeId,
            @PathVariable Long reviewerId,
            @Validated @RequestBody TradeReviewRequest reviewRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tradeReviewService.createReview(tradeId, reviewerId, reviewRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
