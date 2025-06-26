package Software.Portal.web.service;

import Software.Portal.web.dto.SubCategoryDto;
import Software.Portal.web.entity.SubCategory;
import Software.Portal.web.utill.CommonResponse;

public interface SubCategoryService {
    CommonResponse save(SubCategoryDto subCategoryDto);

    CommonResponse update(SubCategoryDto subCategoryDto);

    CommonResponse updateStatus(String subCategoryId, String commonStatus);

    SubCategory findBySubCategoryId(String subCategoryId);

    SubCategoryDto findById(Long subCategoryId);

    CommonResponse getAllSub(String commonStatus);
}
