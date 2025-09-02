package com.market.market_place.community.community_topic;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.community.community_category.CommunityCategory;
import com.market.market_place.community.community_category.CommunityCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityTopicService {

    private final CommunityTopicRepository topicRepository;
    private final CommunityCategoryRepository categoryRepository;

    // 조회
    public List<CommunityTopicResponse.ListDTO> findAll() {
        return topicRepository.findAll().stream()
                .map(CommunityTopicResponse.ListDTO::new)
                .toList();
    }

    // 등록
    @Transactional
    public CommunityTopicResponse.TopicResponseDTO save(CommunityTopicRequest.SaveDTO saveDTO){
        CommunityCategory category = categoryRepository.findById(saveDTO.getCategoryId())
                .orElseThrow(() -> new Exception404("해당 카테고리가 없습니다"));

        CommunityTopic topic = saveDTO.toEntity(category);
        CommunityTopic savedTopic = topicRepository.save(topic);
        return new CommunityTopicResponse.TopicResponseDTO(savedTopic);
    }

    // 수정
    @Transactional
    public CommunityTopicResponse.TopicResponseDTO update(Long id, CommunityTopicRequest.UpdateDTO updateDTO) {
        CommunityTopic topic = topicRepository.findById(id).orElseThrow(() ->
                new Exception404("해당 세부 카테고리가 없습니다"));

        CommunityCategory category = null;
        if(updateDTO.getCategoryId() != null){
            category = categoryRepository.findById(updateDTO.getCategoryId())
                    .orElseThrow(() -> new Exception404("해당 카테고리가 없습니다"));
        }

        topic.update(updateDTO, category);
        return new CommunityTopicResponse.TopicResponseDTO(topic);
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        CommunityTopic  topic = topicRepository.findById(id).orElseThrow(() ->
                new Exception404("해당 세부 카테고리가 없습니다"));

        topicRepository.delete(topic);
    }
}
