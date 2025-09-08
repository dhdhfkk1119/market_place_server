package com.market.market_place.community.community_post;

import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.community.community_post_image.CommunityPostImage;
import com.market.market_place.community.community_post_image.CommunityPostImageRepository;
import com.market.market_place.community.community_topic.CommunityTopic;
import com.market.market_place.community.community_topic.CommunityTopicRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public List<CommunityPostResponse.ListDTO> findAllPosts(Pageable pageable) {
        Page<CommunityPost> posts = postRepository.findAllWithTopic(pageable);
        return posts.stream().map(CommunityPostResponse.ListDTO::new)
                .collect(Collectors.toList());
    }

    // 상세보기
    @Transactional
    public CommunityPostResponse.DetailDTO detail(Long id) {
        CommunityPost post = postRepository.findByIdWithComments(id).orElseThrow(() ->
                new Exception404("게시글이 없습니다"));
        post.increaseViewCount();
        postRepository.save(post);
        return new CommunityPostResponse.DetailDTO(post);
    }

    // 작성
    @Transactional
    public CommunityPostResponse.ResponseDTO save(CommunityPostRequest.SaveDTO saveDTO,
                                                  JwtUtil.SessionUser sessionUser) {
        Member member = memberRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다"));

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

    // 수정
    @Transactional
    public CommunityPostResponse.ResponseDTO update(Long id, CommunityPostRequest.UpdateDTO updateDTO,
                                                    JwtUtil.SessionUser sessionUser) {
        CommunityPost post = postRepository.findById(id).orElseThrow(() ->
                new Exception404("해당 개시글이 없습니다"));
        if (!post.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 수정할 수 있습니다");
        }

        CommunityTopic topic = topicRepository.findById(updateDTO.getTopicId())
                .orElseThrow(() -> new Exception404("세부 카테고리가 없습니다"));

        post.setTopic(topic);
        post.update(updateDTO);

        // 이미지 삭제
        if (updateDTO.getDeleteImages() != null) {
            for (String imageUrl : updateDTO.getDeleteImages()) {
                CommunityPostImage image = imageRepository.findByPostAndImageUrl(post, imageUrl)
                        .orElseThrow(() -> new Exception404("삭제할 이미지가 존재하지 않습니다"));
                post.getImages().remove(image);
                imageRepository.delete(image);
            }
        }

        // 이미지 추가
        if (updateDTO.getAddImages() != null) {
            for (String imageUrl : updateDTO.getAddImages()) {
                CommunityPostImage image = new CommunityPostImage();
                image.setPost(post);
                image.setImageUrl(imageUrl);
                imageRepository.save(image);
                post.getImages().add(image);
            }
        }
        return new CommunityPostResponse.ResponseDTO(post);
    }

    // 삭제
    @Transactional
    public void delete(Long id, JwtUtil.SessionUser sessionUser){
        CommunityPost post = postRepository.findById(id)
                .orElseThrow(() -> new Exception404("삭제하려는 게시글이 없습니다"));
        if(!post.isOwner(sessionUser.getId())){
            throw new Exception403("본인이 작성한 게시글만 삭제할 수 있습니다");
        }
        postRepository.delete(post);
    }

    // 검색
    public Page<CommunityPostResponse.ListDTO> search(CommunityPostRequest.SearchDTO searchDTO, Pageable pageable){
        return postRepository.search(searchDTO.getKeyword(),searchDTO.getCategories(),
                        searchDTO.getSortType(),pageable).map(CommunityPostResponse.ListDTO::new);

    }
}

