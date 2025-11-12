package markit.community.config;

import markit.community.community_category.CommunityCategory;
import markit.community.community_category.CommunityCategoryRepository;
import markit.community.community_topic.CommunityTopic;
import markit.community.community_topic.CommunityTopicRepository;
import markit.community.community_post.CommunityPost;
import markit.community.community_post.CommunityPostRepository;
import markit.community.community_comment.CommunityComment;
import markit.community.community_comment.CommunityCommentRepository;
import markit.members.domain.Member;
import markit.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.LinkedHashMap;

@Profile({"dev","local"})
@Component
@Order(2)
@RequiredArgsConstructor
public class CommunityDataInitializer implements CommandLineRunner {

    private final CommunityCategoryRepository categoryRepository;
    private final CommunityTopicRepository topicRepository;
    private final CommunityPostRepository postRepository;
    private final CommunityCommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // 1. 대분류 카테고리
        CommunityCategory cat1 = createCategoryIfAbsent("동네정보");
        CommunityCategory cat2 = createCategoryIfAbsent("이웃과 함께");
        CommunityCategory cat3 = createCategoryIfAbsent("소식");
        CommunityCategory cat4 = createCategoryIfAbsent("기타");

        // 2. 세부 토픽
        CommunityTopic t1 = createTopicIfAbsent(1L, cat1, "맛집");
        CommunityTopic t2 = createTopicIfAbsent(2L, cat1, "생활/편의");
        CommunityTopic t3 = createTopicIfAbsent(3L, cat1, "병원/약국");
        CommunityTopic t4 = createTopicIfAbsent(4L, cat1, "미용");

        CommunityTopic t5 = createTopicIfAbsent(5L, cat2, "반려동물");
        CommunityTopic t6 = createTopicIfAbsent(6L, cat2, "운동");
        CommunityTopic t7 = createTopicIfAbsent(7L, cat2, "취미");
        CommunityTopic t8 = createTopicIfAbsent(8L, cat2, "고민/사연");

        CommunityTopic t9 = createTopicIfAbsent(9L, cat3, "동네행사");
        CommunityTopic t10 = createTopicIfAbsent(10L, cat3, "분실/실종");
        CommunityTopic t11 = createTopicIfAbsent(11L, cat3, "동네사건사고");
        CommunityTopic t12 = createTopicIfAbsent(12L, cat3, "공공소식");

        // 3. 회원 로딩
        Map<Long, Member> users = loadMembers(4L, 5L); // member_id 4, 5

        // 4. 게시글 생성
        CommunityPost p1 = createPostIfAbsent(users.get(4L), t1, "우리 동네 맛집 추천해요", "어제 다녀온 고기집 진짜 맛있었어요! 추천드려요.", "서울 강남구", 1, 1);
        CommunityPost p2 = createPostIfAbsent(users.get(4L), t2, "편의점 신상 나왔네요", "우리 동네 CU에 신상 아이스크림 들어왔어요.", "서울 마포구", 1, 1);
        CommunityPost p3 = createPostIfAbsent(users.get(4L), t3, "근처 약국 추천 부탁드려요", "감기약 살 수 있는 약국 추천해주세요.", "서울 서대문구", 1, 1);
        CommunityPost p4 = createPostIfAbsent(users.get(4L), t4, "가성비 좋은 미용실 아시나요?", "머리 커트 잘하는 곳 찾고 있어요.", "서울 동작구", 1, 1);
        CommunityPost p5 = createPostIfAbsent(users.get(4L), t5, "강아지 산책 모임 해요", "주말에 반려견 산책 같이하실 분 계신가요?", "서울 송파구", 1, 1);
        CommunityPost p6 = createPostIfAbsent(users.get(5L), t6, "헬스장 추천 좀요", "PT 잘 봐주는 헬스장 있나요?", "서울 용산구", 1, 1);
        CommunityPost p7 = createPostIfAbsent(users.get(5L), t7, "취미로 그림 그리시는 분?", "취미로 수채화 그리는데 같이 해요.", "서울 성동구", 1, 1);
        CommunityPost p8 = createPostIfAbsent(users.get(5L), t8, "진로 고민 나눠요", "취업 준비 중인데 고민이 많네요.", "서울 종로구", 1, 1);
        CommunityPost p9 = createPostIfAbsent(users.get(5L), t9, "이번 주말 플리마켓 열린대요", "동네 공원에서 플리마켓 한다고 해요.", "서울 강서구", 1, 1);
        CommunityPost p10 = createPostIfAbsent(users.get(5L), t10, "분실물 찾아요", "어제 버스에서 가방을 잃어버렸습니다.", "서울 관악구", 1, 1);

        // 5. 댓글 생성
        createCommentIfAbsent(p1, users.get(5L), "저도 거기 가봤는데 맛있더라구요!", 1);
        createCommentIfAbsent(p1, users.get(5L), "오호 저도 가봐야겠네요~", 1);

        createCommentIfAbsent(p2, users.get(5L), "저도 그 아이스크림 먹어봤어요!", 1);
        createCommentIfAbsent(p2, users.get(3L), "편의점 신상 정보 감사합니다 ^^", 1);

        createCommentIfAbsent(p3, users.get(5L), "근처 ○○약국 좋아요", 1);
        createCommentIfAbsent(p3, users.get(5L), "△△약국도 친절하더라구요", 1);

        createCommentIfAbsent(p4, users.get(5L), "저는 ○○헤어 추천드려요!", 1);
        createCommentIfAbsent(p4, users.get(5L), "저렴하고 잘하는 곳 있어요~ 쪽지 드릴게요.", 1);

        createCommentIfAbsent(p5, users.get(5L), "강아지랑 같이 산책하고 싶어요!", 1);
        createCommentIfAbsent(p5, users.get(5L), "저희 강아지도 참가해도 될까요?", 1);

        createCommentIfAbsent(p7, users.get(5L), "저도 그림 같이 그려보고 싶어요~", 1);
        createCommentIfAbsent(p7, users.get(5L), "좋아요! 수채화 모임 저도 참여할래요", 1);

        createCommentIfAbsent(p8, users.get(5L), "취업 준비 힘내세요! 저도 비슷한 상황이에요", 1);
        createCommentIfAbsent(p8, users.get(5L), "조언 필요하면 DM 주세요", 1);

        createCommentIfAbsent(p9, users.get(5L), "플리마켓 저도 가볼래요!", 1);
        createCommentIfAbsent(p9, users.get(5L), "주말에 가족과 함께 가야겠네요", 1);

        createCommentIfAbsent(p10, users.get(5L), "분실물 빨리 찾으시길 바래요", 1);
        createCommentIfAbsent(p10, users.get(5L), "혹시 CCTV 확인해보셨나요?", 1);
    }

    private CommunityCategory createCategoryIfAbsent(String name) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> categoryRepository.save(CommunityCategory.builder().name(name).build()));
    }

    private CommunityTopic createTopicIfAbsent(Long id, CommunityCategory category, String name) {
        return topicRepository.findByName(name)
                .orElseGet(() -> topicRepository.save(CommunityTopic.builder().id(id).category(category).name(name).build()));
    }

    private CommunityPost createPostIfAbsent(Member author, CommunityTopic topic, String title, String content, String location, int likeCount, int viewCount) {
        if (author == null) return null;
        return postRepository.findByTitle(title)
                .orElseGet(() -> postRepository.save(CommunityPost.builder()
                        .member(author)
                        .topic(topic)
                        .title(title)
                        .content(content)
                        .location(location)
                        .likeCount(likeCount)
                        .viewCount(viewCount)
                        .build()));

    }

    private CommunityComment createCommentIfAbsent(CommunityPost post, Member author, String content, int likeCount) {
        if (post == null || author == null) return null;
        return commentRepository.existsByPostAndContent(post, content)
                ? null
                : commentRepository.save(CommunityComment.builder()
                .post(post)
                .member(author)
                .content(content)
                .likeCount(likeCount)
                .build());

    }

    private Map<Long, Member> loadMembers(Long... ids) {
        Map<Long, Member> map = new LinkedHashMap<>();
        for (Long id : ids) {
            memberRepository.findById(id).ifPresent(m -> map.put(id, m));
        }
        return map;
    }
}
