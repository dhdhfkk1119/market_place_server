package com.market.market_place.item.core;

import com.market.market_place.item.item_category.QItemCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements
        ItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Item> findBySearchOption(Pageable pageable, ItemRequest.SearchDTO searchDTO) {
        QItem item = QItem.item;
        QItemCategory itemCategory = QItemCategory.itemCategory;


        List<Item> items = queryFactory
                .select(item).distinct()
                .from(item)
                .leftJoin(item.itemCategory, itemCategory)
                .where(
                        keywordContains(searchDTO.getKeyword()),
                        tagsIn(searchDTO.getTags())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(item.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(item.countDistinct())
                .from(item)
                .leftJoin(item.itemCategory, itemCategory)
                .where(
                        keywordContains(searchDTO.getKeyword()),
                        tagsIn(searchDTO.getTags())
                )
                .fetchOne();

        return new PageImpl<>(items, pageable, total);
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null ||
                keyword.trim().isEmpty()) {
            return null;
        }
        return QItem.item.title.contains(keyword)
                .or(QItem.item.content.contains(keyword));
    }

    private BooleanExpression tagsIn(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return QItemCategory.itemCategory.name.in(tags);
    }
}
