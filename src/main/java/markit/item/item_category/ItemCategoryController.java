package markit.item.item_category;

import markit._core.auth.Auth;
import markit.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/item-categories")
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    @Auth(roles = {Role.USER, Role.ADMIN})
    @GetMapping
    public ResponseEntity<?> index() {
        List<ItemCategoryResponse.ItemCategoryListDTO> categiryList = itemCategoryService.findAll();
        return ResponseEntity.ok(categiryList);
    }

    @Auth(roles = {Role.ADMIN})
    @PostMapping
    public ResponseEntity<?> save(@RequestBody ItemCategoryRequest.SaveDTO dto) {
        ItemCategoryResponse.ItemCategorySaveDTO saved = itemCategoryService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}
