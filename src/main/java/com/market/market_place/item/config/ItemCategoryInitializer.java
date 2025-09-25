package com.market.market_place.item.config;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_category.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Profile({"dev","local"})
@Component
@Order(2)
@RequiredArgsConstructor
public class ItemCategoryInitializer implements CommandLineRunner {

    private final ItemCategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) {
        List<String> categoriesToCreate = List.of(
                "디지털 기기",
                "가구/인테리어",
                "생활가전",
                "도서",
                "스포츠/레저",
                "취미/게임",
                "의류/패션",
                "반려동물 용품",
                "식품",
                "기타"
        );

        // 1. DB에서 이미 존재하는 카테고리 이름들을 한 번에 조회
        Set<String> existingCategoryNames = categoryRepository.findAll().stream()
                .map(ItemCategory::getName)
                .collect(Collectors.toSet());

        // 2. 존재하지 않는 카테고리만 필터링하여 새 엔티티 리스트 생성
        List<ItemCategory> newCategories = categoriesToCreate.stream()
                .filter(name -> !existingCategoryNames.contains(name))
                .map(name -> ItemCategory.builder().name(name).build())
                .toList();

        // 3. 새로 추가할 카테고리가 있다면 saveAll을 통해 일괄 저장
        if (!newCategories.isEmpty()) {
            categoryRepository.saveAll(newCategories);
        }
    }
}
