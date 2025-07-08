package Software.Portal.web.controller;

import Software.Portal.web.dto.SubCategoryDto;
import Software.Portal.web.service.SubCategoryService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/subCategory")
@CrossOrigin
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @Autowired
    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping("/save")
    CommonResponse saveSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        return subCategoryService.save(subCategoryDto);
    }
    @PutMapping("/update")
    CommonResponse updateSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        return subCategoryService.update(subCategoryDto);
    }
    @PutMapping("/updateStatus/{subCategoryId}/{commonStatus}")
    CommonResponse updateSubCategoryStatus(@PathVariable String subCategoryId, @PathVariable String commonStatus) {
        return subCategoryService.updateStatus(subCategoryId,commonStatus);
    }
    @GetMapping("/getAllSub/{commonStatus}")
    CommonResponse getAllSubCategory(@PathVariable String commonStatus) {
        return subCategoryService.getAllSub(commonStatus);
    }
}
