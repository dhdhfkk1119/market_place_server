package com.market.market_place.item.core;

import com.market.market_place.item.item_category.QItemCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Item> findBySearchOption(Pageable pageable, ItemSearchRequest searchDTO) {
        QItem item = QItem.item;
        QItemCategory itemCategory = QItemCategory.itemCategory;


        List<Item> items = queryFactory
                .select(item).distinct()
                .from(item)
                .leftJoin(item.itemCategory, itemCategory)
                .where(
                        keywordContains(searchDTO.getKeyword()),
                        categoryEq(searchDTO.getItemCategoryId()),
                        locationContains(searchDTO.getTradeLocation()),
                        priceBetween(searchDTO.getMinPrice(), searchDTO.getMaxPrice(), searchDTO.getPriceRange())
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
                        categoryEq(searchDTO.getItemCategoryId()),
                        locationContains(searchDTO.getTradeLocation()),
                        priceBetween(searchDTO.getMinPrice(), searchDTO.getMaxPrice(), searchDTO.getPriceRange())
                )
                .fetchOne();

        return new PageImpl<>(items, pageable, total == null ? 0L : total);
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }
        return QItem.item.title.contains(keyword)
                .or(QItem.item.content.contains(keyword));
    }

    private BooleanExpression categoryEq(Long itemCategoryId) {
        if (itemCategoryId == null) {
            return null;
        }
        QItem item = QItem.item;
        return item.itemCategory.id.eq(itemCategoryId);
    }

    private BooleanExpression locationContains(String tradeLocation) {
        if (tradeLocation == null || tradeLocation.trim().isEmpty()) {
            return null;
        }
        QItem item = QItem.item;
        return item.tradeLocation.containsIgnoreCase(tradeLocation);
    }

    private BooleanExpression priceBetween(Long minPrice, Long maxPrice, String priceRange) {
        QItem item = QItem.item;

        if (minPrice != null || maxPrice != null) {
            if (minPrice != null && maxPrice != null) {
                return item.price.between(minPrice, maxPrice);
            }
            if (minPrice != null) {
                return item.price.goe(minPrice);
            }
            return item.price.loe(maxPrice);
        }

        if (priceRange == null || priceRange.isBlank()) {
            return null;
        }

        String pr = priceRange.replaceAll("\\s+", "");
        try {
            if (pr.endsWith("+")) {
                long min = Long.parseLong(pr.substring(0, pr.length() - 1));
                return item.price.goe(min);
            }
            if (pr.contains("-")) {
                String[] parts = pr.split("-", 2);
                long min = Long.parseLong(parts[0]);
                long max = Long.parseLong(parts[1]);
                return item.price.between(min, max);
            }
        } catch (NumberFormatException ignored) {

        }
        return null;
    }
}




