package com.market.market_place.community.community_category;

import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/communityCategories")
public class CommunityCategoryController {

    private final CommunityCategoryService categoryService;

    // 조회
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<CommunityCategoryResponse.ListDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    // 등록
    @Auth(roles = Role.ADMIN)
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CommunityCategoryRequest.SaveDTO saveDTO) {
        CommunityCategoryResponse.CategoryResponseDTO savedCategory = categoryService.save(saveDTO);
        return ResponseEntity.ok(savedCategory);
    }

    // 수정
    @Auth(roles = Role.ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id,
                                    @Valid @RequestBody CommunityCategoryRequest.UpdateDTO updateDTO){
        CommunityCategoryResponse.CategoryResponseDTO updateCategory = categoryService.update(id, updateDTO);
        return ResponseEntity.ok(updateCategory);
    }

    // 삭제
    @Auth(roles = Role.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id){
        categoryService.delete(id);
        return ResponseEntity.ok("삭제 성공");
    }
}
