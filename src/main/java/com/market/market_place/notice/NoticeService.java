package com.market.market_place.notice;

import com.market.market_place._core._exception.Exception403;
import com.market.market_place.members.domain.Role;
import com.market.market_place.notice.dto.NoticeRequest;
import com.market.market_place.notice.dto.NoticeResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;


    public NoticeResponse createNotice(NoticeRequest request) {
        Notice savedNotice = noticeRepository.save(request.toEntity());
        return NoticeResponse.fromEntity(savedNotice);
    }

    public Page<NoticeResponse> getAllNotices(Pageable pageable) {
        return noticeRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(NoticeResponse::fromEntity);
    }

    public NoticeResponse getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notice not found with id: " + id));
        return NoticeResponse.fromEntity(notice);
    }

    public NoticeResponse updateNotice(Long id, Notice updatedNotice, Role role) {
        if (role != Role.ADMIN) {
            throw new Exception403("권한이 없습니다.");
        }

        Notice existingNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notice not found with id: " + id));

        existingNotice.setTitle(updatedNotice.getTitle());
        existingNotice.setContent(updatedNotice.getContent());

        Notice savedNotice = noticeRepository.save(existingNotice);
        return NoticeResponse.fromEntity(savedNotice);
    }

    public void deleteNotice(Long id, Role role) {
        if (role != Role.ADMIN) {
            throw new Exception403("권한이 없습니다.");
        }

        if (!noticeRepository.existsById(id)) {
            throw new EntityNotFoundException("Notice not found with id: " + id);
        }
        noticeRepository.deleteById(id);
    }
}
