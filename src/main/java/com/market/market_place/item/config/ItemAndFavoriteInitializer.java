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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
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
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        // 강남역 1번 출구
        Point gangnamStation = geometryFactory.createPoint(new Coordinate(127.0264, 37.4988));

        // 잠실역 1번 출구
        Point jamsilStation = geometryFactory.createPoint(new Coordinate(127.1011, 37.5138));

        // 마포역 1번 출구
        Point mapoStation = geometryFactory.createPoint(new Coordinate(126.9458, 37.5398));

        // 강서구청역 1번 출구 (*참고: '강서역'은 없어서 '강서구청역'으로 대체)
        Point gangseoGuOfficeStation = geometryFactory.createPoint(new Coordinate(126.8529, 37.5516));

        // 천호역 1번 출구
        Point cheonhoStation = geometryFactory.createPoint(new Coordinate(127.1245, 37.5383));

        // 노원역 1번 출구
        Point nowonStation = geometryFactory.createPoint(new Coordinate(127.0628, 37.6560));

        // 불광역 1번 출구
        Point bulgwangStation = geometryFactory.createPoint(new Coordinate(126.9298, 37.6103));

        // 교대역 1번 출구
        Point gyodaeStation = geometryFactory.createPoint(new Coordinate(127.0142, 37.4936));

        // 용산역 1번 출구
        Point yongsanStation = geometryFactory.createPoint(new Coordinate(126.9649, 37.5298));

        // 종각역 1번 출구
        Point jonggakStation = geometryFactory.createPoint(new Coordinate(126.9829, 37.5704));

        ItemCategory digital = getCategory("디지털 기기");
        ItemCategory furniture = getCategory("가구/인테리어");
        ItemCategory book = getCategory("도서");

        Map<String, Member> users = loadUsers(
                "user1", "user2", "user3", "user4", "user5",
                "user6", "user7", "user8", "user9", "user10",
                "user11", "user12", "user13"
        );

        createItemIfAbsent(users.get("user1"), digital, "삼성노트북", "삼성 최신형 노트북 판매합니다.", 5000L, gangnamStation, 3.5);
        createItemIfAbsent(users.get("user2"), digital, "LG노트북", "LG 그램 중고 노트북입니다.", 12000L, jamsilStation, 4.0);
        createItemIfAbsent(users.get("user3"), digital, "애플노트북", "맥북 프로 상태 양호합니다.", 18000L, mapoStation, 4.2);
        createItemIfAbsent(users.get("user4"), digital, "레노버노트북", "레노버 아이디어패드 팝니다.", 7000L, gangseoGuOfficeStation, 3.8);
        createItemIfAbsent(users.get("user5"), digital, "델노트북", "델 XPS 중고 노트북 판매", 20000L, cheonhoStation, 4.7);

        createItemIfAbsent(users.get("user6"), furniture, "사무용의자", "편안한 사무용 의자 판매", 3000L, nowonStation, 3.9);
        createItemIfAbsent(users.get("user7"), furniture, "원목의자", "인테리어에 좋은 원목 의자", 15000L, bulgwangStation, 4.5);
        createItemIfAbsent(users.get("user8"), furniture, "게이밍의자", "장시간 사용에 좋은 게이밍 의자", 8000L, gyodaeStation, 4.1);
        createItemIfAbsent(users.get("user9"), furniture, "식탁의자", "가정용 식탁 의자 세트 판매", 17000L, yongsanStation, 3.7);
        createItemIfAbsent(users.get("user10"), furniture, "디자인의자", "디자인 감각 있는 의자", 10000L, jonggakStation, 4.4);

        createItemIfAbsent(users.get("user1"), book, "자바책", "자바 프로그래밍 기초 교재", 2000L, gangnamStation, 4.0);
        createItemIfAbsent(users.get("user2"), book, "알고리즘책", "알고리즘 문제 해결 전략", 9000L, jamsilStation, 4.6);
        createItemIfAbsent(users.get("user3"), book, "데이터베이스책", "데이터베이스 개론 교재", 11000L, mapoStation, 3.8);
        createItemIfAbsent(users.get("user4"), book, "영어책", "토익 영어 문법 교재", 6000L, gangseoGuOfficeStation, 4.3);
        createItemIfAbsent(users.get("user5"), book, "머신러닝책", "머신러닝 입문서", 16000L, cheonhoStation, 4.9);

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
                                    Long price, Point location, Double avgRating) {
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
                .imageUrl("data:image/jpg;base64,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")
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