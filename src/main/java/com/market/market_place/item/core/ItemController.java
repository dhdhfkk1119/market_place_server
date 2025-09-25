package com.market.market_place.item.core;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import com.market.market_place.item.review.TradeReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    // 상품 상세 조회
    @Auth(roles = {Role.ADMIN,Role.USER})
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse.ItemDetailDTO> detail(@PathVariable Long id,
                                                             @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        ItemResponse.ItemDetailDTO item = itemService.findById(id,sessionUser);
        return ResponseEntity.ok(item);
    }

    // 내 상품 목록 조회
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/sales")
    public ResponseEntity<Page<ItemResponse.MySalesListItemDTO>> getMySales(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @PageableDefault(size = 10,sort = "createdAt") Pageable pageable
    ) {

        Page<ItemResponse.MySalesListItemDTO> mySales = itemService.getMySales(sessionUser.getId(),pageable);
        log.info("서버에 접근해 했습니다 : {} ,",mySales);
        return ResponseEntity.ok(mySales);
    }

    // 프로필 하단에서 판매자(memberId)의 판매 완료 상품 리뷰 리스트를 조회
    @GetMapping("/sales/sold/{memberId}")
    public ResponseEntity<List<TradeReviewResponse>> getSoldByMember(@PathVariable Long memberId) {
        List<TradeReviewResponse> reviews = itemService.getSoldByMember(memberId);
        return ResponseEntity.ok(reviews);
    }

    // 판매자(memberId)의 판매 완료 상품 리뷰 최근 3개 조회
    @GetMapping("/sales/sold/{memberId}/recent")
    public ResponseEntity<List<TradeReviewResponse>> getRecentSoldReviewsByMember(@PathVariable Long memberId) {
        List<TradeReviewResponse> reviews = itemService.getRecentSoldReviewsByMember(memberId);
        return ResponseEntity.ok(reviews);
    }

    // 상품 목록 조회
//    @GetMapping("/")
//    public ResponseEntity<Page<ItemResponse.ItemListDTO>> list(
//            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
//            Pageable pageable) {
//        Page<ItemResponse.ItemListDTO> body = itemService.findAll(pageable);
//
//        return ResponseEntity.ok(body);
//    }

    @GetMapping("/nearby")
    public ResponseEntity<?> nearbyList(ItemRequest.SearchByLocationDTO location) {
        return ResponseEntity.ok().body(itemService.findAllByLocation(location));
    }

    // 상품 등록
    @Auth(roles = {Role.ADMIN, Role.USER})
    @PostMapping
    public ResponseEntity<?> save(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @RequestBody ItemRequest.ItemSaveDTO dto) {
        ItemResponse.ItemSaveDTO body = itemService.save(sessionUser.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // 상품 수정
    @Auth(roles = {Role.ADMIN, Role.USER})
    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponse.ItemUpdateDTO> update(
            @PathVariable Long id,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @RequestBody ItemRequest.ItemUpdateDTO dto) {
        ItemResponse.ItemUpdateDTO body =
                itemService.update(id, sessionUser.getId(), dto);
        return ResponseEntity.ok(body);
    }

    // 상품 삭제
    @Auth(roles = {Role.ADMIN, Role.USER})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        itemService.delete(id, sessionUser.getId());
        return ResponseEntity.noContent().build();
    }

    // 상품 검색
    @GetMapping
    public ResponseEntity<Page<ItemResponse.ItemListDTO>> getItems(ItemRequest.SearchDTO searchRequest) {
        Page<ItemResponse.ItemListDTO> items = itemService.getItems(searchRequest);
        return ResponseEntity.ok(items);
    }
}