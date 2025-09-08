package com.market.market_place.community.community_post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityPostRepositoryCustom {
    Page<CommunityPost> findBySearchOption(Pageable pageable, CommunityPostRequest.SearchDTO searchDTO,
                                           CommunityPostSortType sortType);
}