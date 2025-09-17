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

@Profile({"dev","local"})
@Component
@Order(2)
@RequiredArgsConstructor
public class ItemCategoryInitializer implements CommandLineRunner {

    private final ItemCategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) {
        List<String> names = List.of(
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

        for (String name : names) {
            if (categoryRepository.findByName(name).isEmpty()) {
                categoryRepository.save(ItemCategory.builder()
                        .name(name)
                        .build());
            }
        }
    }
}
