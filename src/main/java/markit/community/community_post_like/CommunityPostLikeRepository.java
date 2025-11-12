package markit.community.community_post_like;

import markit.community.community_post.CommunityPost;
import markit.members.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityPostLikeRepository extends JpaRepository<CommunityPostLike, Long> {

    Optional<CommunityPostLike> findByPostIdAndMemberId(Long postId, Long memberId);

    boolean existsByPostAndMember(CommunityPost post, Member member);




}
