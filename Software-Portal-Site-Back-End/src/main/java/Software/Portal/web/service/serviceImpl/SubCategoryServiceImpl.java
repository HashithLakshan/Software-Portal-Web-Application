package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.dto.SubCategoryDto;
import Software.Portal.web.entity.SubCategory;
import Software.Portal.web.repository.SubCategoryRepository;
import Software.Portal.web.service.CategoryService;
import Software.Portal.web.service.SubCategoryService;
import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.CommonValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    private final CategoryService categoryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryServiceImpl.class);


    @Autowired
    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository, CategoryService categoryService) {
        this.subCategoryRepository = subCategoryRepository;


    this.categoryService = categoryService;
}

    @Override
    public CommonResponse save(SubCategoryDto subCategoryDto) {
        CommonResponse commonResponse = new CommonResponse();
        SubCategory subCategory;
        try{
            List<String> validationList = this.subCategoryValidation(subCategoryDto);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            subCategory = subCategoryDtoIntoCategory(subCategoryDto);
            subCategoryRepository.save(subCategory);
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Successfully Creat new SubCategory");
            commonResponse.setPayload(Collections.singletonList(subCategory));


        }catch (Exception e){
            commonResponse.setStatus(false);
            LOGGER.error("/**************** Exception in CategoryService -> save()" + e);
        }


        return commonResponse;
    }

    @Override
    public CommonResponse update(SubCategoryDto subCategoryDto) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            SubCategory subCategory= subCategoryRepository.findById(Long.valueOf(subCategoryDto.getSubCategoryId())).get();
            if(subCategory != null){
                subCategory.setSubCategoryName(subCategoryDto.getSubCategoryName());
                subCategory.setSubCategoryDiscription(subCategoryDto.getSubCategoryDiscription());
                subCategory.setCommonStatus(subCategoryDto.getCommonStatus());
                subCategory.setCategory(categoryService.findByCategoryId(subCategoryDto.getCategoryDto().getCategoryId()));
                subCategoryRepository.save(subCategory);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("SubCategory Updated");
                commonResponse.setPayload(Collections.singletonList(subCategory));
         }else {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("SubCategory not found");
            }

        } catch (Exception e) {
            LOGGER.error("/**************** Exception in SubCategoryService -> update()" + e);
            commonResponse.setErrorMessages(Collections.singletonList("An error occurred while updating SubCategory"));
        }
      return commonResponse;
    }

    @Override
    public CommonResponse updateStatus(String subCategoryId, String commonStatus) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            SubCategory subCategory = subCategoryRepository.findById(Long.valueOf(subCategoryId)).get();
            if (commonStatus.equals("ACTIVE")) {
                subCategory.setCommonStatus(CommonStatus.ACTIVE);
                subCategoryRepository.save(subCategory);
                commonResponse.setStatus(true);
                return commonResponse;

            } else if (commonStatus.equals("INACTIVE")) {
                subCategory.setCommonStatus(CommonStatus.INACTIVE);
                subCategoryRepository.save(subCategory);
                commonResponse.setStatus(true);
                return commonResponse;
            } else if (commonStatus.equals("DELETED")) {
                subCategoryRepository.delete(subCategory);
                commonResponse.setStatus(true);
                return commonResponse;
            }
        }catch (Exception e){
            commonResponse.setStatus(false);
            LOGGER.error("/***************** Exception in CategoryService -> updateStatus()" + e);

        }
return commonResponse;
    }

    @Override
    public SubCategory findBySubCategoryId(String subCategoryId) {
        return subCategoryRepository.getBySubCategoryId(Long.valueOf(subCategoryId));
    }

    @Override
    public SubCategoryDto findById(Long subCategoryId) {
        SubCategory subCategory;
        subCategory = subCategoryRepository.findById(subCategoryId).get();
        SubCategoryDto subCategoryDto;
        subCategoryDto = subCategoryIntoSubCategoryDto(subCategory);
        return subCategoryDto;
    }

    @Override
    public CommonResponse getAllSub(String commonStatus) {
        CommonResponse commonResponse = new CommonResponse();
        List<SubCategoryDto> subCategoryDtoList = new ArrayList<>();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);

        try {
            Predicate<SubCategory> filterOnStatus = subCategory  -> subCategory.getCommonStatus() == commonStatus1;
            subCategoryDtoList= subCategoryRepository.findAll().stream().filter(filterOnStatus).map(this::subCategoryIntoSubCategoryDto).collect(Collectors.toList());

            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(subCategoryDtoList));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in SubCategoryService -> getAll(ActiveCategories)" + e);
        }
        return commonResponse;
    }


    private SubCategoryDto subCategoryIntoSubCategoryDto(SubCategory subCategory) {
        SubCategoryDto subCategoryDto = new SubCategoryDto();
        subCategoryDto.setSubCategoryId(String.valueOf(subCategory.getSubCategoryId()));
        subCategoryDto.setSubCategoryName(subCategory.getSubCategoryName());
        subCategoryDto.setSubCategoryDiscription(subCategory.getSubCategoryDiscription());
        subCategoryDto.setCommonStatus(subCategory.getCommonStatus());
        subCategoryDto.setCategoryDto(categoryService.findById(subCategory.getCategory().getCategoryId()));
        return subCategoryDto;
    }

    private SubCategory subCategoryDtoIntoCategory(SubCategoryDto subCategoryDto) {
       SubCategory subCategory = new SubCategory();
       subCategory.setSubCategoryName(subCategoryDto.getSubCategoryName());
       subCategory.setSubCategoryDiscription(subCategoryDto.getSubCategoryDiscription());
       subCategory.setCommonStatus(subCategoryDto.getCommonStatus());
        subCategory.setCategory(categoryService.findByCategoryId(subCategoryDto.getCategoryDto().getCategoryId()));
        return subCategory;
    }
    private List<String> subCategoryValidation(SubCategoryDto subCategoryDto) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(subCategoryDto.getSubCategoryName()))
            validationList.add("SubCategory name field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(subCategoryDto.getSubCategoryName())))
            validationList.add("SubCategory Discription field is empty");

        return validationList;
    }

}
