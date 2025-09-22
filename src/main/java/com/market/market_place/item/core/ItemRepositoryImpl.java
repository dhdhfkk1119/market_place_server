package com.market.market_place.item.core;

import com.market.market_place.item.item_category.QItemCategory;
import com.market.market_place.item.item_tag.QItemTag;
import com.market.market_place.item.item_tag.QTag;
import static com.market.market_place.item.core.QItem.item;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanOperation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.locationtech.jts.geom.Point;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Item> findBySearchOption(Pageable pageable, ItemRequest.SearchDTO searchDTO) {
        QItem item = QItem.item;
        QItemCategory itemCategory = QItemCategory.itemCategory;
        QTag tag = QTag.tag;

        List<Item> items = queryFactory
                .select(item).distinct()
                .from(item)
                .leftJoin(item.itemCategory, itemCategory)
                .leftJoin(item.itemTags, QItemTag.itemTag)
                .leftJoin(QItemTag.itemTag.tag,tag)
                .where(
                        keywordContains(searchDTO.getKeyword()),
                        categoryEq(searchDTO.getItemCategoryId()),
                        withinDistance(searchDTO.getTradeLocation(), searchDTO.getDistanceInMeter()),
                        priceBetween(searchDTO.getMinPrice(), searchDTO.getMaxPrice(), searchDTO.getPriceRange()),
                        tagsIn(searchDTO.getTags(), tag)

                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(item.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(item.countDistinct())
                .from(item)
                .leftJoin(item.itemCategory, itemCategory)
                .leftJoin(item.itemTags, QItemTag.itemTag)
                .leftJoin(QItemTag.itemTag.tag,tag)
                .where(
                        keywordContains(searchDTO.getKeyword()),
                        categoryEq(searchDTO.getItemCategoryId()),
                        withinDistance(searchDTO.getTradeLocation(), searchDTO.getDistanceInMeter()),
                        priceBetween(searchDTO.getMinPrice(), searchDTO.getMaxPrice(), searchDTO.getPriceRange()),
                        tagsIn(searchDTO.getTags(), tag)
                )
                .fetchOne();

        return new PageImpl<>(items, pageable, total == null ? 0L : total);
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }
        return QItem.item.title.contains(keyword)
                .or(QItem.item.content.containsIgnoreCase(keyword));
    }

    private BooleanExpression categoryEq(Long itemCategoryId) {
        if (itemCategoryId == null) {
            return null;
        }
        QItem item = QItem.item;
        return item.itemCategory.id.eq(itemCategoryId);
    }

    private BooleanExpression withinDistance(Point center, Double distanceInMeter) {
        if (center == null || distanceInMeter <= 0) {
            return null;
        }

        return item.tradeLocation.distance(center).loe(distanceInMeter);
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

    private BooleanExpression tagsIn(List<String> tags, QTag tag) {
        if (tags == null || tags.isEmpty()) return null;
        List<String> normalized = tags.stream()
                .filter(Objects::nonNull)
                .map(t -> t.trim().toLowerCase())
                .filter(s -> !s.isBlank())
                .distinct()
                .toList();
        if (normalized.isEmpty()) return null;
        return tag.nameNormalized.in(normalized);
    }
}




