package com.market.market_place.member_reviews;

import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewResponse.ResponseDTO save(ReviewRequest.SaveDTO saveDTO, JwtUtil.SessionUser sessionUser) {
        Member reviewer = memberRepository.findById(sessionUser.getId())
                                          .orElseThrow(() -> new Exception404("리뷰 작성자를 찾을 수 없습니다"));
        Member reviewed = memberRepository.findById(saveDTO.getReviewedId())
                                          .orElseThrow(() -> new Exception404("리뷰 대상자를 찾을 수 없습니다"));

        // 자기 자신에게는 리뷰 작성 불가
        if (reviewer.getId()
                    .equals(reviewed.getId())) {
            throw new Exception403("자기 자신에게는 리뷰를 작성할 수 없습니다");
        }

        // rating 유효성 검사
        if (saveDTO.getRating() == null || saveDTO.getRating() < 1 || saveDTO.getRating() > 5) {
            throw new Exception403("rating은 1부터 5 사이의 값이어야 합니다");
        }

        if (reviewRepository.existsByReviewerAndReviewed(reviewer, reviewed)) {
            throw new Exception403("이미 리뷰를 작성하셨습니다");
        }

        Review review = Review.builder()
                              .reviewer(reviewer)
                              .reviewed(reviewed)
                              .rating(saveDTO.getRating())
                              .comment(saveDTO.getComment())
                              .build();
        try {
            reviewRepository.save(review);
        } catch (DataIntegrityViolationException e) {
            // DB 레벨 unique 제약 위반(동시성) 처리
            throw new Exception403("이미 리뷰를 작성하셨습니다");
        }
        return new ReviewResponse.ResponseDTO(review);
    }

    public List<ReviewResponse.ResponseDTO> findByReviewedId(Long memberId) {
        return reviewRepository.findByReviewedId(memberId)
                               .stream()
                               .map(ReviewResponse.ResponseDTO::new)
                               .collect(Collectors.toList());
    }

    /**
     * 특정 멤버에 대한 리뷰 리스트를 반환
     * viewerId가 작성한 리뷰가 있다면 리스트의 첫 번째에 배치
     * 나머지 리뷰는 최신순으로 정렬
     */
    public List<ReviewResponse.ResponseDTO> findByReviewedIdWithViewerFirst(Long memberId, Long viewerId) {
        List<Review> reviews = reviewRepository.findByReviewedId(memberId);
        List<ReviewResponse.ResponseDTO> result = reviews.stream()
                                                         .map(ReviewResponse.ResponseDTO::new)
                                                         .collect(Collectors.toList());
        if (viewerId == null) return result;
        // viewer가 작성한 리뷰를 맨 위로 이동
        int idx = -1;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i)
                      .getReviewerId()
                      .equals(viewerId)) {
                idx = i;
                break;
            }
        }
        if (idx > 0) {
            ReviewResponse.ResponseDTO viewerReview = result.remove(idx);
            result.add(0, viewerReview);
        }
        return result;
    }

    @Transactional
    public ReviewResponse.ResponseDTO update(Long id, ReviewRequest.UpdateDTO updateDTO, JwtUtil.SessionUser sessionUser) {
        Review review = reviewRepository.findById(id)
                                        .orElseThrow(() -> new Exception404("리뷰를 찾을 수 없습니다"));
        if (!review.getReviewer()
                   .getId()
                   .equals(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 리뷰만 수정할 수 있습니다");
        }
        if (updateDTO.getRating() != null && (updateDTO.getRating() < 1 || updateDTO.getRating() > 5)) {
            throw new Exception403("rating은 1부터 5 사이의 값이어야 합니다");
        }
        if (updateDTO.getRating() != null) review.setRating(updateDTO.getRating());
        if (updateDTO.getComment() != null) review.setComment(updateDTO.getComment());
        return new ReviewResponse.ResponseDTO(review);
    }

    @Transactional
    public void delete(Long id, JwtUtil.SessionUser sessionUser) {
        Review review = reviewRepository.findById(id)
                                        .orElseThrow(() -> new Exception404("리뷰를 찾을 수 없습니다"));
        if (!review.getReviewer()
                   .getId()
                   .equals(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 리뷰만 삭제할 수 있습니다");
        }
        reviewRepository.delete(review);
    }
}
