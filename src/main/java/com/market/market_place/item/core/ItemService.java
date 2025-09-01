package com.market.market_place.item.core;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_category.ItemCategoryRepository;
import com.market.market_place.item.item_image.ItemImageRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberAddress;
import com.market.market_place.members.repositories.MemberAddressRepository;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final MemberRepository memberRepository;

    public ItemResponse.ItemSaveDTO save(Long id,ItemRequest.SaveDTO saveDTO) {

        Member seller = memberRepository.findById(id)
                .orElseThrow(() -> new Exception404("회원이 존재하지 않습니다"));

        ItemCategory category = itemCategoryRepository.findById(saveDTO.getItemCategoryId())
                .orElseThrow(() -> new Exception404("카테고리를 찾을 수 없습니다"));

        MemberAddress address = memberAddressRepository.findById(saveDTO.getMemberAddressId())
                .orElseThrow(() -> new Exception404("주소가 존재하지 않습니다."));

        Item item = saveDTO.toEntity(category,address);
        item.setMember(seller);

        Item saved = itemRepository.save(item);

        return new ItemResponse.ItemSaveDTO(saved);
    }

}
