package Software.Portal.web.controller;

import Software.Portal.web.dto.CategoryDto;
import Software.Portal.web.service.CategoryService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/Category")
@CrossOrigin
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    CommonResponse saveCategory (@RequestBody CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping("/allCategories/{commonStatus}")
    CommonResponse getAllCategories(@PathVariable String commonStatus) {
        return categoryService.getAll(commonStatus);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/delete")
    CommonResponse  deleteCategory (@RequestParam String categoryId,@RequestParam String commonStatus) {
     return categoryService.updateStatus(categoryId,commonStatus);
    }

    @GetMapping("/getByIdCat/{categoryId}")
    CommonResponse getByIdCategory (@PathVariable String categoryId) {
        return categoryService.getById(categoryId);
}

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getByIdCatName")
    CommonResponse getByIdCategoryName (@RequestParam String categoryName) {
        return categoryService.getByName(categoryName);
    }


    @GetMapping("/filtered")
    public CommonResponse getFilteredCategory(
            @RequestParam(required = false) String commonStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return categoryService.getFilteredCategory(commonStatus, page, size);
    }

}
