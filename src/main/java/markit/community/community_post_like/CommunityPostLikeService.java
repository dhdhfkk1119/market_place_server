package markit.community.community_post_like;

import markit._core._exception.Exception404;
import markit._core._utils.JwtUtil;
import markit.community.community_post.CommunityPost;
import markit.community.community_post.CommunityPostRepository;
import markit.members.domain.Member;
import markit.members.repositories.MemberRepository;
import markit.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommunityPostLikeService {

    private final CommunityPostLikeRepository likeRepository;
    private final CommunityPostRepository postRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    @Transactional
    public CommunityPostLikeResponse.ResponseDTO toggleLike(Long postId, JwtUtil.SessionUser sessionUser){

        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        Member member = memberRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다"));

        Optional<CommunityPostLike> postLike = likeRepository.findByPostIdAndMemberId(postId, sessionUser.getId());

        boolean liked;
        if(postLike.isPresent()){
            likeRepository.delete(postLike.get());
            post.updateLikeCount(post.getLikeCount() -1);
            liked = false;
        } else {
            CommunityPostLike like = CommunityPostLike.builder()
                    .post(post)
                    .member(member)
                    .build();
            likeRepository.save(like);
            post.updateLikeCount(post.getLikeCount() + 1);
            liked = true;
        }
        notificationService.sendPostLike(post.getMember().getId().toString(), post.getTitle());

        return new CommunityPostLikeResponse.ResponseDTO(liked, (long) post.getLikeCount());
    }

    public Long getLikeCount(Long postId){
        return postRepository.findById(postId)
                .map(post -> (long)post.getLikeCount())
                .orElse(0L);
    }
}
