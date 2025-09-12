package com.market.market_place.moderation.sanction.item_sanction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSanctionResponse {
    private Long sanctionId;
    private Long memberId;
    private String type;
    private int acceptedCountAtCreation;
    private String reason;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private boolean active;

    public static ItemSanctionResponse none(Long memberId) {
        return ItemSanctionResponse.builder()
                .memberId(memberId)
                .type("NONE")
                .active(false)
                .build();
    }

    public static ItemSanctionResponse from(ItemSanction sanction) {
        return ItemSanctionResponse.builder()
                .sanctionId(sanction.getId())
                .memberId(sanction.getMember().getId())
                .type(sanction.getType().name())
                .acceptedCountAtCreation(sanction.getAcceptedCountAtCreation())
                .reason(sanction.getReason())
                .startAt(sanction.getStartAt())
                .endAt(sanction.getEndAt())
                .active(sanction.isActive())
                .build();
    }
}
