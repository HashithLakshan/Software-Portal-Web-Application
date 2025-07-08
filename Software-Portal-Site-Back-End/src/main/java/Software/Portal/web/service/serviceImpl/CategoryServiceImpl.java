package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.dto.CategoryDto;
import Software.Portal.web.entity.Category;
import Software.Portal.web.repository.CategoryRepository;
import Software.Portal.web.service.CategoryService;
import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.CommonValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }

    @Override
    public CommonResponse save(CategoryDto categoryDto) {
        CommonResponse commonResponse = new CommonResponse();
        Category category = new Category();
        try {
            List<String> validationList = this.CategoryValidation(categoryDto);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(categoryDto.getCategoryId())) {
                LocalDate today = LocalDate.now();

                category.setSaveDate(today);
                category.setCategoryName(categoryDto.getCategoryName());
                category.setCategoryDiscription(categoryDto.getCategoryDiscription());
                category.setCommonStatus(categoryDto.getCommonStatus());
                categoryRepository.save(category);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Successfully Creat new Category");
                commonResponse.setPayload(Collections.singletonList(category));
                return commonResponse;
            } else {

            Optional<Category> category1 = categoryRepository.findById(Long.valueOf(categoryDto.getCategoryId()));
            if (category1.isPresent()) {
                category = categoryRepository.findById(Long.valueOf(categoryDto.getCategoryId())).get();

                if (category.getCategoryName().equals(categoryDto.getCategoryName()) && category.getCategoryDiscription().equals(categoryDto.getCategoryDiscription())) {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("You not any changes update");
                    return commonResponse;
                } else {
                    category.setCategoryName(categoryDto.getCategoryName());
                    category.setCategoryDiscription(categoryDto.getCategoryDiscription());
                    category.setCommonStatus(categoryDto.getCommonStatus());
                    categoryRepository.save(category);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Successfully Update Category");
                    commonResponse.setPayload(Collections.singletonList(category));
                    return commonResponse;
                }
            } else {

                category = CategoryDtoIntoCategory(categoryDto);
                categoryRepository.save(category);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Successfully Creat new Category");
                commonResponse.setPayload(Collections.singletonList(category));
                return commonResponse;
            }
        }
        }catch (Exception e){
            commonResponse.setStatus(false);
            LOGGER.error("/**************** Exception in CategoryService -> save()" + e);
        }


        return commonResponse;
    }

    @Override
    public CommonResponse updateStatus(String categoryId,String commonStatus) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Category category = categoryRepository.findById(Long.valueOf(categoryId)).get();
            if (commonStatus.equals("ACTIVE")) {
                category.setCommonStatus(CommonStatus.ACTIVE);
                categoryRepository.save(category);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Successfully Recovered");
                return commonResponse;

            } else if (commonStatus.equals("INACTIVE")) {
                category.setCommonStatus(CommonStatus.INACTIVE);
                categoryRepository.save(category);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Successfully Deleted");
                return commonResponse;
            } else if (commonStatus.equals("DELETED")) {
                categoryRepository.delete(category);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Successfully Permanent Deleted");
                return commonResponse;
            }
        }catch (Exception e){
            commonResponse.setStatus(false);
            LOGGER.error("/***************** Exception in SubCategoryService -> updateStatus()" + e);

        }

        return commonResponse;
    }

    @Override
    public CommonResponse getAll(String commonStatus) {
        CommonResponse commonResponse = new CommonResponse();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);

        try {
            Predicate<Category> filterOnStatus = category -> category.getCommonStatus() == commonStatus1;
            categoryDtoList= categoryRepository.findAll().stream().filter(filterOnStatus).map(this::castCategoryIntoCategoryDto).collect(Collectors.toList());

            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(categoryDtoList));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CategoryService -> getAll(ActiveCategories)" + e);
        }
        return commonResponse;
    }

    @Override
    public Category findByCategoryId(String categoryId) {
        return categoryRepository.getByCategoryId(Long.valueOf(categoryId));
    }

    @Override
    public CategoryDto findById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        CategoryDto categoryDto ;
        categoryDto   =  castCategoryIntoCategoryDto(category);
        return categoryDto;
    }

    @Override
    public CommonResponse getById(String categoryId) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Category category = categoryRepository.getByCategoryId(Long.valueOf(categoryId));
            CategoryDto categoryDto = castCategoryIntoCategoryDto(category);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(categoryDto));
            commonResponse.setCommonMessage("Successfully Retrieved Category");
        } catch (Exception e) {
            commonResponse.setStatus(false);
            LOGGER.error("/**************** Exception in CategoryService -> getById(Categories)" + e);

        }
        return commonResponse;
    }

    @Override
    public CommonResponse getFilteredCategory(String commonStatus, int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        Page<Category> categoryPage;

        try {

            // Convert String to Enum
            CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);


                categoryPage = categoryRepository.findByCommonStatus(commonStatusEnum, pageable);


            // Convert Employee entities to DTOs
            categoryDtoList = categoryPage.getContent().stream()
                    .map(this::castCategoryIntoCategoryDto)
                    .collect(Collectors.toList());

            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", categoryPage.getNumber());
            paginationDetails.put("totalItems", categoryPage.getTotalElements());
            paginationDetails.put("totalPages", categoryPage.getTotalPages());

            // Set response payload properly
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Category fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(categoryDtoList)); // Employee list
            commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details

        } catch (IllegalArgumentException e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Invalid request status or common status.");
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching employees.");
        }

        return commonResponse;
    }

    @Override
    public CommonResponse getByName(String categoryName) {
        CommonResponse commonResponse = new CommonResponse();
        if(CommonValidation.stringNullValidation(categoryName)){
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Search filed are  empty.");
            return commonResponse;
        }else{
            Optional<Category> category = categoryRepository.findByCategoryNameAndCommonStatus(categoryName,CommonStatus.ACTIVE);
            if(category.isPresent()){
                CategoryDto categoryDto = castCategoryIntoCategoryDto(category.get());
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Successfully Retrieved Category");
                commonResponse.setPayload(Collections.singletonList(categoryDto));
                return commonResponse;
            }else{
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Category not found.");
                return commonResponse;
            }
        }
    }

    private Category CategoryDtoIntoCategory(CategoryDto categoryDto) {
        Category category = new Category();
        LocalDate today = LocalDate.now();
        category.setSaveDate(today);
        category.setCategoryName(categoryDto.getCategoryName());
        category.setCategoryDiscription(categoryDto.getCategoryDiscription());
        category.setCommonStatus(categoryDto.getCommonStatus());
       return category;
    }
    private CategoryDto castCategoryIntoCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setSaveDate(String.valueOf(category.getSaveDate()));
        categoryDto.setCategoryId(String.valueOf(category.getCategoryId()));
        categoryDto.setCategoryName(category.getCategoryName());
        categoryDto.setCategoryDiscription(category.getCategoryDiscription());
        categoryDto.setCommonStatus(category.getCommonStatus());
        return categoryDto;
    }

    private List<String> CategoryValidation(CategoryDto categoryDto) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(categoryDto.getCategoryName()))
            validationList.add("Category name field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(categoryDto.getCategoryDiscription())))
            validationList.add("Category Discription field is empty");

        return validationList;
    }

}
