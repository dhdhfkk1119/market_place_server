package com.market.market_place.item.praise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PraiseRequest {

    @NotNull(message = "칭찬받은 사용자 ID는 필수입니다.")
    private Long praisedMemberId;

    @NotNull(message = "거래 ID는 필수입니다.")
    private Long tradeId;

    private List<Long> praiseCategories;

    private String content;
}
