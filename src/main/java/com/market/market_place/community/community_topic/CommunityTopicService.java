package com.market.market_place.community.community_topic;

import com.market.market_place._core._exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityTopicService {

    private final CommunityTopicRepository topicRepository;

    // 조회
    public List<CommunityTopicResponse.ListDTO> findAll() {
        return topicRepository.findAll().stream()
                .map(CommunityTopicResponse.ListDTO::new)
                .toList();
    }

    // 등록
    @Transactional
    public CommunityTopicResponse.TopicResponseDTO save(CommunityTopicRequest.SaveDTO saveDTO){
        CommunityTopic topic = saveDTO.toEntity();
        CommunityTopic savedTopic = topicRepository.save(topic);
        return new CommunityTopicResponse.TopicResponseDTO(savedTopic);
    }

    // 수정
    @Transactional
    public CommunityTopicResponse.TopicResponseDTO update(Long id, CommunityTopicRequest.UpdateDTO updateDTO) {
        CommunityTopic topic = topicRepository.findById(id).orElseThrow(() ->
                new Exception404("해당 세부 카테고리가 없습니다"));

        topic.update(updateDTO);
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
