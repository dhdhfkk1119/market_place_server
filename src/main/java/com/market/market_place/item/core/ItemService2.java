package com.market.market_place.item.core;

import com.market.market_place._core._utils.JwtUtil;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService2 {

    private final ItemRepository2 itemRepository2;

    @Transactional(readOnly = true)
    public Page<ItemResponse.ItemListDTO> getItems(ItemSearchRequest searchRequest, JwtUtil.SessionUser sessionUser) {

        QItem item = QItem.item;
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어 (제목 + 내용 모두 검색 가능)
        if (searchRequest.getKeyword() != null && !searchRequest.getKeyword().isEmpty()) {
            builder.and(item.title.containsIgnoreCase(searchRequest.getKeyword())
                    .or(item.content.containsIgnoreCase(searchRequest.getKeyword())));
        }

        // 가격 범위 (0~최대)
        if (searchRequest.getMinPrice() != null) {
            builder.and(item.price.goe(searchRequest.getMinPrice()));
        }
        if (searchRequest.getMaxPrice() != null) {
            builder.and(item.price.loe(searchRequest.getMaxPrice()));
        }

        // 카테고리
        if (searchRequest.getItemCategoryId() != null) {
            builder.and(item.itemCategory.id.eq(searchRequest.getItemCategoryId()));
        }

        // 위치
        if (searchRequest.getTradeLocation() != null && !searchRequest.getTradeLocation().isEmpty()) {
            builder.and(item.tradeLocation.eq(searchRequest.getTradeLocation()));
        }

        // 정렬 + 페이지네이션
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if ("latest".equals(searchRequest.getSortBy())) {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        } else if ("popular".equals(searchRequest.getSortBy())) {
            sort = Sort.by(Sort.Direction.DESC, "averageRating");
        }
        if ("asc".equalsIgnoreCase(searchRequest.getSortOrder())) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);

        // 엔티티 조회
        Page<Item> itemPage = itemRepository2.findAll(builder, pageable);


        return itemPage.map(ItemResponse.ItemListDTO::from);
    }
}