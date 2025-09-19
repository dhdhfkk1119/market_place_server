package com.market.market_place.item.config;

import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_category.ItemCategoryRepository;
import com.market.market_place.item.item_favorite.ItemFavorite;
import com.market.market_place.item.item_favorite.ItemFavoriteRepository;
import com.market.market_place.item.item_image.ItemImage;
import com.market.market_place.item.status.TradeStatus;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Profile({"dev", "local"})
@Component
@Order(3)
@RequiredArgsConstructor
public class ItemAndFavoriteInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemFavoriteRepository itemFavoriteRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void run(String... args) {
        ItemCategory digital = getCategory("디지털 기기");
        ItemCategory furniture = getCategory("가구/인테리어");
        ItemCategory book = getCategory("도서");

        Map<String, Member> users = loadUsers(
                "user1", "user2", "user3", "user4", "user5",
                "user6", "user7", "user8", "user9", "user10",
                "user11", "user12", "user13"
        );

        createItemIfAbsent(users.get("user1"), digital, "삼성노트북", "삼성 최신형 노트북 판매합니다.", 5000L, "강남역", 3.5);
        createItemIfAbsent(users.get("user2"), digital, "LG노트북", "LG 그램 중고 노트북입니다.", 12000L, "잠실역", 4.0);
        createItemIfAbsent(users.get("user3"), digital, "애플노트북", "맥북 프로 상태 양호합니다.", 18000L, "마포역", 4.2);
        createItemIfAbsent(users.get("user4"), digital, "레노버노트북", "레노버 아이디어패드 팝니다.", 7000L, "강서역", 3.8);
        createItemIfAbsent(users.get("user5"), digital, "델노트북", "델 XPS 중고 노트북 판매", 20000L, "천호역", 4.7);

        createItemIfAbsent(users.get("user6"), furniture, "사무용의자", "편안한 사무용 의자 판매", 3000L, "노원역", 3.9);
        createItemIfAbsent(users.get("user7"), furniture, "원목의자", "인테리어에 좋은 원목 의자", 15000L, "불광역", 4.5);
        createItemIfAbsent(users.get("user8"), furniture, "게이밍의자", "장시간 사용에 좋은 게이밍 의자", 8000L, "교대역", 4.1);
        createItemIfAbsent(users.get("user9"), furniture, "식탁의자", "가정용 식탁 의자 세트 판매", 17000L, "용산역", 3.7);
        createItemIfAbsent(users.get("user10"), furniture, "디자인의자", "디자인 감각 있는 의자", 10000L, "종각역", 4.4);

        createItemIfAbsent(users.get("user1"), book, "자바책", "자바 프로그래밍 기초 교재", 2000L, "강남역", 4.0);
        createItemIfAbsent(users.get("user2"), book, "알고리즘책", "알고리즘 문제 해결 전략", 9000L, "잠실역", 4.6);
        createItemIfAbsent(users.get("user3"), book, "데이터베이스책", "데이터베이스 개론 교재", 11000L, "마포역", 3.8);
        createItemIfAbsent(users.get("user4"), book, "영어책", "토익 영어 문법 교재", 6000L, "강서역", 4.3);
        createItemIfAbsent(users.get("user5"), book, "머신러닝책", "머신러닝 입문서", 16000L, "천호역", 4.9);

        List<String> titles = List.of(
                "삼성노트북", "LG노트북", "애플노트북", "레노버노트북", "델노트북",
                "사무용의자", "원목의자", "게이밍의자", "식탁의자", "디자인의자",
                "자바책", "알고리즘책", "데이터베이스책", "영어책", "머신러닝책"
        );
        List<String> likerIds = List.of(
                "user1", "user2", "user3", "user4", "user5",
                "user6", "user7", "user8", "user9", "user10",
                "user11", "user12", "user13", "user1", "user2"
        );

        for (int i = 0; i < titles.size(); i++) {
            final String title = titles.get(i);     // effectively final
            final String likerId = likerIds.get(i); // effectively final

            itemRepository.findByTitle(title).ifPresent(item -> {
                Member liker = users.get(likerId);
                if (liker != null) {
                    createFavoriteIfAbsent(item, liker);
                }
            });
        }
    }

    private ItemCategory getCategory(String name) {
        return itemCategoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalStateException("카테고리 없음: " + name));
    }

    private Map<String, Member> loadUsers(String... loginIds) {
        Map<String, Member> map = new LinkedHashMap<>();
        for (String id : loginIds) {
            memberRepository.findByLoginId(id).ifPresent(m -> map.put(id, m));
        }
        return map;
    }

    private void createItemIfAbsent(Member seller, ItemCategory category, String title, String content,
                                    Long price, String location, Double avgRating) {
        if (seller == null) return;
        Optional<Item> existing = itemRepository.findByTitle(title);
        if (existing.isPresent()) return;

        Item item = Item.builder()
                .member(seller)
                .itemCategory(category)
                .title(title)
                .content(content)
                .price(price)
                .status(TradeStatus.ON_SALE)
                .tradeLocation(location)
                .averageRating(avgRating)
                .build();

        ItemImage image = ItemImage.builder()
                        .imageUrl("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8Xw8AAn8B9V2HAAAAAElFTkSuQmCC")
                .build();
        item.addImage(image);

        itemRepository.save(item);
    }

    private void createFavoriteIfAbsent(Item item, Member member) {
        boolean exists = itemFavoriteRepository.existsByItemAndMember(item, member);
        if (!exists) {
            itemFavoriteRepository.save(
                    ItemFavorite.builder()
                            .item(item)
                            .member(member)
                            .build()
            );
        }
    }
}