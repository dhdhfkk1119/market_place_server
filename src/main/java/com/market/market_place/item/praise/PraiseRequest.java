package com.market.market_place.item.praise;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PraiseRequest {
   private Long tradeId;
   private boolean isBuyer;
   private List<Long> praiseCategories;
}
