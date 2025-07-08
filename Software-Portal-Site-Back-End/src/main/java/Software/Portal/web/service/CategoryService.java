package Software.Portal.web.service;

import Software.Portal.web.dto.CategoryDto;
import Software.Portal.web.entity.Category;
import Software.Portal.web.utill.CommonResponse;

public interface

CategoryService {
    CommonResponse save(CategoryDto categoryDto);

    CommonResponse updateStatus(String categoryId,String commonStatus);

    CommonResponse getAll(String commonStatus);

    Category findByCategoryId(String categoryId);

    CategoryDto findById(Long categoryId);

    CommonResponse getById(String categoryId);

    CommonResponse getFilteredCategory(String commonStatus, int page, int size);

    CommonResponse getByName(String categoryName);
}
