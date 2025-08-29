package com.market.market_place.notice;

import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Member;
import com.market.market_place.notice.dto.NoticeRequest;
import com.market.market_place.notice.dto.NoticeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Auth(roles = Member.MemberRole.ADMIN)
    @PostMapping
    public ResponseEntity<NoticeResponse> createNotice(@RequestBody NoticeRequest request) {
        NoticeResponse response = noticeService.createNotice(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Auth(roles = {Member.MemberRole.USER, Member.MemberRole.ADMIN})
    @GetMapping
    public ResponseEntity<Page<NoticeResponse>> getAllNotices(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<NoticeResponse> responsePage = noticeService.getAllNotices(pageable);
        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    @Auth(roles = {Member.MemberRole.USER, Member.MemberRole.ADMIN})
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> getNoticeById(@PathVariable Long id) {
        NoticeResponse response = noticeService.getNoticeById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Auth(roles = Member.MemberRole.ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponse> updateNotice(@PathVariable Long id, @RequestBody NoticeRequest request) {
        NoticeResponse response = noticeService.updateNotice(id, request.toEntity());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Auth(roles = Member.MemberRole.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}