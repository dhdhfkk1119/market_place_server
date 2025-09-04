package com.market.market_place.community.community_topic;

import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/communityTopics")
public class CommunityTopicController {

    private final CommunityTopicService topicService;

    // 조회
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<CommunityTopicResponse.ListDTO> topics = topicService.findAll();
        return ResponseEntity.ok(topics);
    }

    // 등록
    @Auth(roles = Role.ADMIN)
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CommunityTopicRequest.SaveDTO saveDTO){
        CommunityTopicResponse.TopicResponseDTO savedTopic = topicService.save(saveDTO);
        return ResponseEntity.ok(savedTopic);
    }

    // 수정
    @Auth(roles = Role.ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id,
                                    @Valid @RequestBody CommunityTopicRequest.UpdateDTO updateDTO) {
        CommunityTopicResponse.TopicResponseDTO updateTopic = topicService.update(id, updateDTO);
        return ResponseEntity.ok(updateTopic);
    }

    // 삭제
    @Auth(roles = Role.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        topicService.delete(id);
        return ResponseEntity.ok("삭제 성공");
    }
}
