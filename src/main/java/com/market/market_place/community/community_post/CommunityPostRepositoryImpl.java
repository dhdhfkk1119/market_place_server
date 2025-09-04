package com.market.market_place.community.community_post;

import com.market.market_place.community.community_category.QCommunityCategory;
import com.market.market_place.community.community_topic.QCommunityTopic;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class CommunityPostRepositoryImpl implements CommunityPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QCommunityPost post = QCommunityPost.communityPost;
    private final QCommunityCategory category = QCommunityCategory.communityCategory;
    private final QCommunityTopic topic = QCommunityTopic.communityTopic;

    @Override
    public Page<CommunityPost> findBySearchOption(Pageable pageable, CommunityPostRequest.SearchDTO searchDTO,
                                                  CommunityPostSortType sortType) {

        // 데이터 조회 쿼리
        List<CommunityPost> posts = queryFactory
                .select(post).distinct()
                .from(post)
                .leftJoin(post.topic, topic)
                .where(
                        // 검색 조건들을 동적으로 조합
                        keywordContains(searchDTO.getKeyword()),
                        categoriesIn(searchDTO.getCategories())
                )
                .offset(pageable.getOffset()) // 데이터 가져올 시작점
                .limit(pageable.getPageSize()) // 가져올 페이지 갯수
                .orderBy(sortOrder(sortType))
                .fetch();

        // 전체 카운트 쿼리(페이지네이션용)
        Long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.topic, topic)
                .where(
                        keywordContains(searchDTO.getKeyword()),
                        categoriesIn(searchDTO.getCategories())
                )
                .fetchOne();
        return new PageImpl<>(posts, pageable, total);
    }

    // 키워드(제목, 내용) 검색 조건
    private BooleanExpression keywordContains(String keyword){
        if(keyword == null || keyword.trim().isEmpty()) {
            return null; // 키워드가 없으면 조건 무시
        }
        return post.title.contains(keyword)
                .or(post.content.contains(keyword));
    }

    // 카테고리 검색 조건
    private BooleanExpression categoriesIn(List<String> categories){
        if(categories == null || categories.isEmpty()){
            return null; // 카테고리가 없으면 조건 무시
        }
        return topic.name.in(categories);
    }

    // 인기순, 조회순, 최신순 정렬 조건
    private OrderSpecifier<?> sortOrder(CommunityPostSortType sortType){
        if(sortType == null){
            return post.createdAt.desc(); // 기본값 = 최신순
        }
        return switch (sortType){
            case LATEST -> post.createdAt.desc();
            case LIKES -> post.likeCount.desc();
            case VIEWS -> post.viewCount.desc();
        };
    }
}