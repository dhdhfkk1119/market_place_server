package com.market.market_place.moderation.sanction.item_sanction;

import com.market.market_place.item.item_report._enum.ProcessResult;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSanctionRequest {

    @NotNull
    private Long reportedMemberId;

    @NotNull
    private Long reportId;

    @NotNull
    private ProcessResult processResult;

    private String adminReason;

}
