package com.market.market_place.community.community_category;

import com.market.market_place._core._exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityCategoryService {

    private final CommunityCategoryRepository categoryRepository;

    public List<CommunityCategoryResponse.ListDTO> findAll() {

        return categoryRepository.findAll().stream()
                .map(CommunityCategoryResponse.ListDTO::new)
                .toList();
    }

    @Transactional
    public CommunityCategoryResponse.CategoryResponseDTO save(CommunityCategoryRequest.SaveDTO saveDTO){

        CommunityCategory category = saveDTO.toEntity();
        CommunityCategory savedCategory = categoryRepository.save(category);
        return new CommunityCategoryResponse.CategoryResponseDTO(savedCategory);
    }

    @Transactional
    public CommunityCategoryResponse.CategoryResponseDTO update(Long id, CommunityCategoryRequest.UpdateDTO updateDTO) {

        CommunityCategory category = categoryRepository.findById(id).orElseThrow(() ->
                new Exception404("해당 카테고리가 없습니다"));

        category.update(updateDTO);
        return new CommunityCategoryResponse.CategoryResponseDTO(category);
    }

    @Transactional
    public void delete(Long id) {
        CommunityCategory category = categoryRepository.findById(id).orElseThrow(() ->
                new Exception404("해당 카테고리가 없습니다"));

        categoryRepository.delete(category);
    }
}
