package com.market.market_place.item.item_report;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.core.Item;
import com.market.market_place.item.core.ItemRepository;
import com.market.market_place.item.core.ItemResponse;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemReportService {

    private final ItemReportRepository itemReportRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public ItemReportResponse.ItemReportSaveDTO save(Long itemId,Long reporterId,ItemReportRequest.ItemReportSaveDTO dto) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new Exception404("해당 게시물을 찾을 수 없습니다"));
        Member reporter = memberRepository.findById(reporterId)
                .orElseThrow(() -> new Exception404("해당 유저를 찾을 수 없습니다."));


        ItemReport itemReport = dto.toEntity(item,reporter);
        ItemReport saved = itemReportRepository.save(itemReport);

         return new ItemReportResponse.ItemReportSaveDTO(saved);
    }



}
