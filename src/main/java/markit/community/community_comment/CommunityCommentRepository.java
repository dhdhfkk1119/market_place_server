package markit.community.community_comment;

import markit.community.community_post.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

    @Query("SELECT cc FROM CommunityComment cc JOIN FETCH cc.member m JOIN FETCH m.memberProfile WHERE cc.post.id = :postId")
    List<CommunityComment> findByPostId(@Param("postId") Long postId);

    boolean existsByPostAndContent(CommunityPost post, String content);

}
