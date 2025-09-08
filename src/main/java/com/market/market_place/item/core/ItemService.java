package com.market.market_place.item.core;

import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_category.ItemCategoryRepository;
import com.market.market_place.item.item_image.ItemImage;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final MemberRepository memberRepository;


    public ItemResponse.ItemDetailDTO findById(Long id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 게시물을 찾을 수 없습니다"));

        return new ItemResponse.ItemDetailDTO(item);
    }


    public Page<ItemResponse.ItemListDTO> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable)
                .map(ItemResponse.ItemListDTO::from);
    }

    public List<ItemResponse.ItemListDTO> search(ItemRequest.SearchDTO searchDTO) {
        return itemRepository.search(searchDTO.getKeyword(),
                searchDTO.getTags()).stream()
                .map(ItemResponse.ItemListDTO::from)
                .collect(Collectors.toList());
    }

    public ItemResponse.ItemSaveDTO save(Long id,ItemRequest.ItemSaveDTO dto) {

        Member seller = memberRepository.findById(id)
                .orElseThrow(() -> new Exception404("회원이 존재하지 않습니다"));

        ItemCategory category = itemCategoryRepository.findById(dto.getItemCategoryId())
                .orElseThrow(() -> new Exception404("카테고리를 찾을 수 없습니다"));

        Item item = dto.toEntity(category);
        item.setMember(seller);

        if (dto.getBase64Images() != null && !dto.getBase64Images().isEmpty()) {
            for (String data : dto.getBase64Images()) {
                if (data != null && !data.isBlank()) {
                    item.addImage(ItemImage.of(data));
                }
            }
        }

        Item saved = itemRepository.save(item);
        return new ItemResponse.ItemSaveDTO(saved);
    }

    public ItemResponse.ItemUpdateDTO update(Long id,Long sessionUserId,ItemRequest.ItemUpdateDTO dto) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new Exception404("상품이 존재하지 않습니다."));

        if (!Objects.equals(item.getMember().getId(),sessionUserId)){
            throw new Exception403("수정 권한이 없습니다.");
        }

        if (dto.getTitle() != null) {
            item.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            item.setContent(dto.getContent());
        }
        if (dto.getPrice() != null) {
            item.setPrice(dto.getPrice());
        }

        if (dto.getTradeLocation() != null) {
            item.setTradeLocation(dto.getTradeLocation());
        }



        return new ItemResponse.ItemUpdateDTO(item);
    }

    public void delete(Long id, Long sessionUserId) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new Exception404("상품이 존재하지 않습니다."));

        if (!Objects.equals(item.getMember().getId(),sessionUserId)) {
            throw new Exception403("삭제 권한이 없습니다.");
        }

        itemRepository.delete(item);
    }

}