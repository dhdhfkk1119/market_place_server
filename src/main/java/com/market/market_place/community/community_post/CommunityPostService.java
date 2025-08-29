package com.market.market_place.community.community_post;

import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.community.community_comment.CommunityComment;
import com.market.market_place.community.community_post_image.CommunityPostImage;
import com.market.market_place.community.community_post_image.CommunityPostImageRepository;
import com.market.market_place.community.community_topic.CommunityTopic;
import com.market.market_place.community.community_topic.CommunityTopicRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.members.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostService {

    private final CommunityPostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommunityTopicRepository topicRepository;
    private final CommunityPostImageRepository imageRepository;

    // 전체 조회
    public List<CommunityPostResponse.ListDTO> findAllPosts() {
        List<CommunityPost> posts = postRepository.findAll();
        return posts.stream().map(CommunityPostResponse.ListDTO::new)
                .collect(Collectors.toList());
    }

    // 상세보기
    public CommunityPostResponse.DetailDTO detail (Long id){
        CommunityPost post = postRepository.findById(id).orElseThrow(() ->
                new Exception404("게시글이 없습니다"));
        postRepository.findByIdWithImages(id);
        postRepository.findByIdWithComments(id);
        post.increaseViewCount();
        return new CommunityPostResponse.DetailDTO(post);
    }

    // 작성
    @Transactional
    public CommunityPostResponse.ResponseDTO save(CommunityPostRequest.SaveDTO saveDTO,
                                                  JwtUtil.SessionUser sessionUser) {
        Member member = memberRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception401("사용자를 찾을 수 없습니다"));

        CommunityTopic topic = topicRepository.findById(saveDTO.getTopicId())
                .orElseThrow(() -> new Exception404("세부 카테고리가 존재하지 않습니다"));

        CommunityPost communityPost = saveDTO.toEntity(member, topic);
        CommunityPost savedPost = postRepository.save(communityPost);

        if (saveDTO.getImages() != null) {
            for (String imageUrl : saveDTO.getImages()) {
                CommunityPostImage image = new CommunityPostImage();
                image.setPost(savedPost);
                image.setImageUrl(imageUrl);
                imageRepository.save(image);
            }
        }
        return new CommunityPostResponse.ResponseDTO(savedPost);
    }

//    // 수정
//    @Transactional
//    public CommunityPostResponse.ResponseDTO update(Long id, CommunityPostRequest.UpdateDTO updateDTO,
//                                                    JwtUtil.SessionUser sessionUser){
//        CommunityPost post = postRepository.findById(id).orElseThrow(() ->
//                new Exception404("해당 개시글이 없습니다"));
//        if(!post.isOwner(sessionUser.getId())){
//            throw new Exception403("본인이 작성한 게시글만 수정할 수 있습니다");
//        }
//
//    }

    // 삭제




}
