package com.market.market_place.item.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> findBySearchOption(Pageable pageable, ItemRequest.SearchDTO searchDTO);
}
