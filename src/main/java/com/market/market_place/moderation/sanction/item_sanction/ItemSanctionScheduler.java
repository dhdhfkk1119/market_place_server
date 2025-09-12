package com.market.market_place.moderation.sanction.item_sanction;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemSanctionScheduler {

    private final ItemSanctionRepository itemSanctionRepository;
    private final ItemSanctionService itemSanctionService;

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void autoReleaseExpired() {
        LocalDateTime now = LocalDateTime.now();
        List<ItemSanction> expired = itemSanctionRepository.findByActiveTrueAndEndAtBefore(now);

        for (int i = 0; i < expired.size(); i++) {
            ItemSanction itemSanction = expired.get(i);
            itemSanctionService.releaseIfExpired(itemSanction.getId());
        }
    }
}
