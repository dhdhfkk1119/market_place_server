package com.market.market_place.moderation.sanction.community_sanction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunitySanctionResponse {

    private Long id;
    private Long memberId;
    private Long reportId;
    private int sanctionCount;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

}
