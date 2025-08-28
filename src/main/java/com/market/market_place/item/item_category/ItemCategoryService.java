package com.market.market_place.item.item_category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;

    public List<ItemCategoryResponse.ItemCategoryListDTO> findAll() {

        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();

        return itemCategoryList.stream()
                .map(ItemCategoryResponse.ItemCategoryListDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ItemCategoryResponse.ItemCategoryDetailDTO detail(Long id) {

        ItemCategory itemCategory = itemCategoryRepository.findById(id).orElse(null);

        return new ItemCategoryResponse.ItemCategoryDetailDTO(itemCategory);
    }

    @Transactional
    public ItemCategoryResponse.ItemCategorySaveDTO save(ItemCategoryRequest.SaveDTO saveDTO) {

        ItemCategory itemCategory = saveDTO.toEntity();
        ItemCategory saved = itemCategoryRepository.save(itemCategory);

        return new ItemCategoryResponse.ItemCategorySaveDTO(saved);
    }

    @Transactional
    public ItemCategoryResponse.ItemCategoryUpdateDTO update(ItemCategoryRequest.UpdateDTO updateDTO, Long id) {

        ItemCategory itemCategory = itemCategoryRepository.findById(id).orElse(null);

        itemCategory.update(updateDTO);

        return new ItemCategoryResponse.ItemCategoryUpdateDTO(itemCategory);
    }

    public void deleteById(Long id) {

        ItemCategory itemCategory = itemCategoryRepository.findById(id).orElseThrow(() ->
                new  IllegalArgumentException("삭제 하려는 카테고리가 존재하지 않습니다"));

        itemCategoryRepository.delete(itemCategory);
    }
}
