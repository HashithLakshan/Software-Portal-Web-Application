package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.dto.EmployeeDto;
import Software.Portal.web.dto.SubCategoryDto;
import Software.Portal.web.dto.SystemProfileDto;
import Software.Portal.web.entity.SystemProfile;
import Software.Portal.web.repository.SystemProfileRepository;
import Software.Portal.web.service.*;
import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.CommonValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemProfileServiceImpl implements SystemProfileService {

    private final SystemProfileRepository systemProfileRepository;

    private final EmployeeService employeeService;

    private final CategoryService categoryService;

    private final SubCategoryService subCategoryService;


    private static final Logger LOGGER = LoggerFactory.getLogger(SystemProfileServiceImpl.class);


    @Autowired
    public SystemProfileServiceImpl(SystemProfileRepository systemProfileRepository, EmployeeService employeeService, CategoryService categoryService, SubCategoryService subCategoryService) {
        this.systemProfileRepository = systemProfileRepository;
        this.employeeService = employeeService;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
    }

    @Override
    public CommonResponse save(SystemProfileDto systemProfileDto) {
    CommonResponse commonResponse = new CommonResponse();
        SystemProfile systemProfile;
        try{
            List<String> validationList = this.SystemProfileValidation(systemProfileDto);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                commonResponse.setStatus(false);
                return commonResponse;
            }

            if(systemProfileDto.getEmployeeDto().getEmployeeId().isEmpty()){
                SystemProfile systemProfile1 = new SystemProfile();
                systemProfile1.setSystemProfilesName(systemProfileDto.getSystemProfilesName());
                systemProfile1.setSystemProfilesId(Long.valueOf(systemProfileDto.getSystemProfilesId()));
                systemProfile1.setSystemProfilesDiscription(systemProfileDto.getSystemProfilesDiscription());
                systemProfile1.setRequestStatus(systemProfileDto.getRequestStatus());
                systemProfile1.setSystemProfilesPrice(Integer.parseInt(systemProfileDto.getSystemProfilesPrice()));
                systemProfile1.setCommonStatus(CommonStatus.ACTIVE);
                systemProfile1.setCategory(categoryService.findByCategoryId(systemProfileDto.getCategoryDto().getCategoryId()));
                systemProfile1.setSubCategory(subCategoryService.findBySubCategoryId(systemProfileDto.getSubCategoryDto().getSubCategoryId()));
                systemProfile = systemProfileRepository.save(systemProfile1);
                commonResponse.setStatus(true);
                commonResponse.setPayload(Collections.singletonList(systemProfile));

            } else if (systemProfileDto.getSubCategoryDto().getSubCategoryId().isEmpty()) {
                SystemProfile systemProfile1 = new SystemProfile();
                systemProfile1.setSystemProfilesName(systemProfileDto.getSystemProfilesName());
                systemProfile1.setSystemProfilesId(Long.valueOf(systemProfileDto.getSystemProfilesId()));
                systemProfile1.setSystemProfilesDiscription(systemProfileDto.getSystemProfilesDiscription());
                systemProfile1.setRequestStatus(systemProfileDto.getRequestStatus());
                systemProfile1.setSystemProfilesPrice(Integer.parseInt(systemProfileDto.getSystemProfilesPrice()));
                systemProfile1.setCommonStatus(CommonStatus.ACTIVE);
                systemProfile1.setCategory(categoryService.findByCategoryId(systemProfileDto.getCategoryDto().getCategoryId()));
                systemProfile = systemProfileRepository.save(systemProfile1);
                commonResponse.setStatus(true);
                commonResponse.setPayload(Collections.singletonList(systemProfile));
            } else {


                systemProfile = SystemProfileDtoIntoSystemProfile(systemProfileDto);
                systemProfile = systemProfileRepository.save(systemProfile);
                commonResponse.setStatus(true);
                commonResponse.setPayload(Collections.singletonList(systemProfile));
            }

        }catch (Exception e){
            commonResponse.setStatus(false);
            LOGGER.error("/**************** Exception in SystemProfileService -> save()" + e);
        }
        return commonResponse;
    }

    @Override
    public CommonResponse aproveStatus(String systemProfilesId) {
        CommonResponse commonResponse = new CommonResponse();
      try {
          SystemProfile systemProfile = systemProfileRepository.findById(Long.valueOf(systemProfilesId)).get();
          systemProfile.setRequestStatus(RequestStatus.APPROVED);
          systemProfileRepository.save(systemProfile);
          commonResponse.setStatus(true);
          commonResponse.setCommonMessage("Approved the system profile");
          return commonResponse;
      }catch (Exception e){
          commonResponse.setStatus(false);
          LOGGER.error("/***************** Exception in SystemProfileService -> aproveStatus()" + e);
      }
      return commonResponse;
    }

    @Override
    public CommonResponse getAllActiveProfilesPendingAndApproved(String requestStatus, String commonStatus) {
        CommonResponse commonResponse = new CommonResponse();
        List<SystemProfileDto> systemProfileDtoList = new ArrayList<>();
        CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
        RequestStatus requestStatusEnum = RequestStatus.valueOf(requestStatus);
        List<SystemProfile> SystemProfile = systemProfileRepository.findByCommonStatusAndRequestStatus(commonStatusEnum,requestStatusEnum);

        if(SystemProfile.isEmpty()){
             commonResponse.setStatus(false);
             commonResponse.setCommonMessage("No system profile found");

         }
        else {
            systemProfileDtoList = SystemProfile.stream()
                    .map(this::castSystemProfileyIntoSystemProfileDto)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("SystemProfile fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(systemProfileDtoList)); // Employee list
        }
        return commonResponse;
    }

    @Override
    public CommonResponse getAllActiveProfilesPendingAndApprovedSuper(String commonStatus,String requestStatus, int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        List<SystemProfileDto> systemProfileDtoList = new ArrayList<>();
        Page<SystemProfile> SystemProfilePage;

        try {

            // Convert String to Enum
            CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
            RequestStatus requestStatusEnum = null;

            if (requestStatus != null && !requestStatus.isEmpty()) {
                requestStatusEnum = RequestStatus.valueOf(requestStatus);
            }

            if (commonStatus.equals("INACTIVE")) {
                SystemProfilePage = systemProfileRepository.findByCommonStatus(commonStatusEnum, pageable);
            } else {
                SystemProfilePage = systemProfileRepository.findByCommonStatusAndRequestStatus(commonStatusEnum, requestStatusEnum, pageable);
            }


            // Convert Employee entities to DTOs
            systemProfileDtoList = SystemProfilePage.getContent().stream()
                    .map(this::castSystemProfileyIntoSystemProfileDto)
                    .collect(Collectors.toList());

            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", SystemProfilePage.getNumber());
            paginationDetails.put("totalItems", SystemProfilePage.getTotalElements());
            paginationDetails.put("totalPages", SystemProfilePage.getTotalPages());

            // Set response payload properly
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("SystemProfile fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(systemProfileDtoList));
            commonResponse.setPages(Collections.singletonList(paginationDetails));

        } catch (IllegalArgumentException e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Invalid request status or common status.");
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching SystemProfile.");
        }

        return commonResponse;
    }

    @Override
    public CommonResponse InactiveStatus(String systemProfilesId) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            SystemProfile systemProfile = systemProfileRepository.findById(Long.valueOf(systemProfilesId)).get();
            systemProfile.setCommonStatus(CommonStatus.INACTIVE);
            systemProfileRepository.save(systemProfile);
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Delete the system profile");
            return commonResponse;
        } catch (Exception e) {
            commonResponse.setStatus(false);
            LOGGER.error("/***************** Exception in SystemProfileService -> InactiveStatus()" + e);
        }
        return commonResponse;
    }

    @Override
    public CommonResponse ActiveStatus(String systemProfilesId) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            SystemProfile systemProfile = systemProfileRepository.findById(Long.valueOf(systemProfilesId)).get();
            systemProfile.setCommonStatus(CommonStatus.ACTIVE);
            systemProfileRepository.save(systemProfile);
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Delete the system profile");
            return commonResponse;
        } catch (Exception e) {
            commonResponse.setStatus(false);
            LOGGER.error("/***************** Exception in SystemProfileService -> ActiveStatus()" + e);
        }
        return commonResponse;
    }

    @Override
    public CommonResponse DeletePermanent(String systemProfilesId) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            SystemProfile systemProfile = systemProfileRepository.findById(Long.valueOf(systemProfilesId)).get();
            systemProfileRepository.delete(systemProfile);
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Delete the system profile for Permanent");
            return commonResponse;
        } catch (Exception e) {
            commonResponse.setStatus(false);
            LOGGER.error("/***************** Exception in SystemProfileService -> PermanentDelete()" + e);
        }
        return commonResponse;    }

    @Override
    public CommonResponse update(SystemProfileDto systemProfileDto) {
        CommonResponse commonResponse = new CommonResponse();
        SystemProfile systemProfile = systemProfileRepository.findById(Long.valueOf(systemProfileDto.getSystemProfilesId())).get();

        if(systemProfileDto.getSystemProfilesPrice().equals(systemProfile.getSystemProfilesPrice())
                && systemProfileDto.getSystemProfilesDiscription().equals(systemProfile.getSystemProfilesDiscription())
                && systemProfileDto.getSystemProfilesName().equals(systemProfile.getSystemProfilesName()) &&
                systemProfileDto.getCategoryDto() ==
                systemProfileDto.getCategoryDto() && systemProfile.getSubCategory() == systemProfile.getSubCategory()) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Not changes In profile information's");
        }else{
Optional <SystemProfile> systemProfile1 = systemProfileRepository.findById(Long.valueOf(systemProfileDto.getSystemProfilesId()));
            if(systemProfile1.isPresent()) {
                SystemProfile systemProfile2 = systemProfileRepository.findById(Long.valueOf(systemProfileDto.getSystemProfilesId())).get();
                if(systemProfileDto.getEmployeeDto().getEmployeeId().isEmpty()){
                    systemProfile2.setSystemProfilesName(systemProfileDto.getSystemProfilesName());
                    systemProfile2.setSystemProfilesDiscription(systemProfileDto.getSystemProfilesDiscription());
                    systemProfile2.setRequestStatus(systemProfileDto.getRequestStatus());
                    systemProfile2.setSystemProfilesPrice(Integer.parseInt(systemProfileDto.getSystemProfilesPrice()));
                    systemProfile2.setCategory(categoryService.findByCategoryId(systemProfileDto.getCategoryDto().getCategoryId()));
//                    systemProfile2.setSubCategory(subCategoryService.findBySubCategoryId(systemProfileDto.getSubCategoryDto().getSubCategoryId()));
                     systemProfileRepository.save(systemProfile2);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Update the system profile Information's");
                    commonResponse.setPayload(Collections.singletonList(systemProfile2));

                } else {


                    systemProfile2 = SystemProfileDtoIntoSystemProfile(systemProfileDto);
                    systemProfileRepository.save(systemProfile2);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Update the system profile Information's");
                    commonResponse.setPayload(Collections.singletonList(systemProfile2));
                }





            }



        }

        return commonResponse;
    }

    @Override
    public SystemProfileDto findDetails(Long systemProfilesId) {
        SystemProfileDto systemProfileDto;
        SystemProfile  systemProfile = systemProfileRepository.findById(systemProfilesId).get();
        systemProfileDto = castSystemProfileyIntoSystemProfileDto(systemProfile);
        return systemProfileDto;
    }

    @Override
    public CommonResponse getById(String systemProfileId, String commonStatus, String requestStatus) {
        CommonResponse commonResponse = new CommonResponse();
        SystemProfileDto systemProfileDto;
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);
        if( !systemProfileId.isEmpty()) {
            if (commonStatus.equals("INACTIVE")) {
                SystemProfile systemProfile = systemProfileRepository.findByCommonStatusAndSystemProfilesId(commonStatus1, Long.valueOf(systemProfileId));
                if (systemProfile !=  null) {
                    systemProfileDto = castSystemProfileyIntoSystemProfileDto(systemProfile);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("System Profile Information's");
                    commonResponse.setPayload(Collections.singletonList(systemProfileDto));
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No record For that ProfileId" + systemProfileId);
                    return commonResponse;
                }
            } else {
                RequestStatus requestStatus1 = RequestStatus.valueOf(requestStatus);
                SystemProfile systemProfile = systemProfileRepository.findByCommonStatusAndRequestStatusAndSystemProfilesId(commonStatus1, requestStatus1, Long.valueOf(systemProfileId));
                if (systemProfile != null) {
                    systemProfileDto = castSystemProfileyIntoSystemProfileDto(systemProfile);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("System Profile Information's");
                    commonResponse.setPayload(Collections.singletonList(systemProfileDto));
                    return commonResponse;
                } else {

                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No record For that ProfileId" + systemProfileId);
                    return commonResponse;
                }

            }
        }else {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Searchbar Is empty");
            return commonResponse;
        }
    }

    @Override
    public CommonResponse getByEmployeeId(String employeeId, String commonStatus, String requestStatus) {
        CommonResponse commonResponse = new CommonResponse();
        List <SystemProfileDto> systemProfileDtoList = new  ArrayList<>();
        SystemProfileDto systemProfileDto;
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);
        if(!employeeId.isEmpty()) {
            if (commonStatus.equals("INACTIVE")) {
                List<SystemProfile> systemProfile = systemProfileRepository.findByCommonStatusAndEmployeeEmployeeId(commonStatus1, employeeId);

                if (systemProfile != null) {
                    systemProfileDtoList = systemProfile.stream()
                            .map(this::castSystemProfileyIntoSystemProfileDto)
                            .collect(Collectors.toList());
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("System Profile Information's");
                    commonResponse.setPayload(Collections.singletonList(systemProfileDtoList));
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No record For that employeeId" + employeeId);
                    return commonResponse;
                }
            } else {
                RequestStatus requestStatus1 = RequestStatus.valueOf(requestStatus);
                List<SystemProfile> systemProfile = systemProfileRepository.findByCommonStatusAndRequestStatusAndEmployeeEmployeeId(commonStatus1, requestStatus1, employeeId);
                if (!systemProfile.isEmpty()) {
                    systemProfileDtoList = systemProfile.stream()
                            .map(this::castSystemProfileyIntoSystemProfileDto)
                            .collect(Collectors.toList());
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("System Profile Information's");
                    commonResponse.setPayload(Collections.singletonList(systemProfileDtoList));
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No record For that employeeId" + employeeId);
                    return commonResponse;
                }
            }
        }else{
           commonResponse.setStatus(false);
           commonResponse.setCommonMessage("Searchbar Is empty");
           return commonResponse;
        }

    }

    @Override
    public CommonResponse getAllActiveProfilesApprovedAndInactiveSuper(String commonStatus, String requestStatus,String employeeId, int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        List<SystemProfileDto> systemProfileDtoList = new ArrayList<>();
        Page<SystemProfile> SystemProfilePage;

        try {

            // Convert String to Enum
            CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
            RequestStatus requestStatusEnum = null;

            if (requestStatus != null && !requestStatus.isEmpty()) {
                requestStatusEnum = RequestStatus.valueOf(requestStatus);
            }

            if (commonStatus.equals("INACTIVE")) {
                SystemProfilePage = systemProfileRepository.findByCommonStatusAndEmployeeEmployeeId(commonStatusEnum,employeeId, pageable);
            } else {
                SystemProfilePage = systemProfileRepository.findByCommonStatusAndRequestStatusAndEmployeeEmployeeId(commonStatusEnum, requestStatusEnum,employeeId, pageable);
            }


            // Convert Employee entities to DTOs
            systemProfileDtoList = SystemProfilePage.getContent().stream()
                    .map(this::castSystemProfileyIntoSystemProfileDto)
                    .collect(Collectors.toList());

            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", SystemProfilePage.getNumber());
            paginationDetails.put("totalItems", SystemProfilePage.getTotalElements());
            paginationDetails.put("totalPages", SystemProfilePage.getTotalPages());

            // Set response payload properly
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("SystemProfile fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(systemProfileDtoList)); // Employee list
            commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details

        } catch (IllegalArgumentException e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Invalid request status or common status.");
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching SystemProfile.");
        }

        return commonResponse;
    }

    @Override
    public CommonResponse searchAll(String inPut, int page, int size) {
        CommonResponse commonResponse =  new CommonResponse();
        boolean x = CommonValidation.stringNullValidation(inPut);
        Pageable pageable = PageRequest.of(page, size);
        List<SystemProfileDto> systemProfileDtoList = new ArrayList<>();
        Page<SystemProfile> SystemProfilePage;

        if(CommonValidation.stringNullValidation(inPut)){
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Searchbar Is Empty");
            return commonResponse;
        }


        SystemProfilePage = systemProfileRepository.findByCommonStatusAndRequestStatusAndCategoryCategoryName(CommonStatus.ACTIVE,RequestStatus.APPROVED,inPut,pageable);

if(x){
    commonResponse.setStatus(false);
    commonResponse.setCommonMessage("search bar is empty");
    return commonResponse;
}

if(SystemProfilePage.getContent().size() > 0){
    systemProfileDtoList = SystemProfilePage.getContent().stream()
            .map(this::castSystemProfileyIntoSystemProfileDto)
            .collect(Collectors.toList());

    // Prepare pagination details
    Map<String, Object> paginationDetails = new HashMap<>();
    paginationDetails.put("currentPage", SystemProfilePage.getNumber());
    paginationDetails.put("totalItems", SystemProfilePage.getTotalElements());
    paginationDetails.put("totalPages", SystemProfilePage.getTotalPages());

    // Set response payload properly
    commonResponse.setStatus(true);
    commonResponse.setCommonMessage("SystemProfile fetched successfully.");
    commonResponse.setPayload(Collections.singletonList(systemProfileDtoList)); // Employee list
    commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details
}
else{
    SystemProfilePage = systemProfileRepository.findByCommonStatusAndRequestStatusAndSystemProfilesName(CommonStatus.ACTIVE,RequestStatus.APPROVED, inPut,pageable);

    if(SystemProfilePage.getContent().size() > 0){
        systemProfileDtoList = SystemProfilePage.getContent().stream()
                .map(this::castSystemProfileyIntoSystemProfileDto)
                .collect(Collectors.toList());

        // Prepare pagination details
        Map<String, Object> paginationDetails = new HashMap<>();
        paginationDetails.put("currentPage", SystemProfilePage.getNumber());
        paginationDetails.put("totalItems", SystemProfilePage.getTotalElements());
        paginationDetails.put("totalPages", SystemProfilePage.getTotalPages());

        // Set response payload properly
        commonResponse.setStatus(true);
        commonResponse.setCommonMessage("SystemProfile fetched successfully.");
        commonResponse.setPayload(Collections.singletonList(systemProfileDtoList)); // Employee list
        commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details
    }else{
         boolean z = CommonValidation.isNumeric(inPut);
         if (z){
             SystemProfilePage = systemProfileRepository.findByCommonStatusAndRequestStatusAndSystemProfilesPriceBetween(CommonStatus.ACTIVE,RequestStatus.APPROVED,0, Integer.parseInt(inPut),pageable);
             if(SystemProfilePage.getContent().size() > 0){
                 systemProfileDtoList = SystemProfilePage.getContent().stream()
                         .map(this::castSystemProfileyIntoSystemProfileDto)
                         .collect(Collectors.toList());

                 // Prepare pagination details
                 Map<String, Object> paginationDetails = new HashMap<>();
                 paginationDetails.put("currentPage", SystemProfilePage.getNumber());
                 paginationDetails.put("totalItems", SystemProfilePage.getTotalElements());
                 paginationDetails.put("totalPages", SystemProfilePage.getTotalPages());

                 // Set response payload properly
                 commonResponse.setStatus(true);
                 commonResponse.setCommonMessage("SystemProfile fetched successfully.");
                 commonResponse.setPayload(Collections.singletonList(systemProfileDtoList)); // Employee list
                 commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details
             }else{
                 commonResponse.setStatus(false);
                 commonResponse.setCommonMessage("Not Found");
                 return commonResponse;
             }
         }else{
             commonResponse.setStatus(false);
             commonResponse.setCommonMessage("Not Found");
             return commonResponse;

         }

    }


}

return commonResponse;
    }


    @Override
    public SystemProfile findByProfileId(String systemProfilesId) {
        return systemProfileRepository.findById(Long.valueOf(systemProfilesId)).get();
    }

    @Override
    public CommonResponse getByOneProfileDetails(String systemProfileId) {
        CommonResponse commonResponse = new CommonResponse();
try {
    SystemProfile systemProfile = systemProfileRepository.findById(Long.valueOf(systemProfileId)).get();
    SystemProfileDto systemProfileDto = new SystemProfileDto();
    systemProfileDto = castSystemProfileyIntoSystemProfileDto(systemProfile);
    commonResponse.setStatus(true);
    commonResponse.setPayload(Collections.singletonList(systemProfileDto));
}catch (Exception e){
    commonResponse.setStatus(false);
    LOGGER.error("/**************** Exception in SystemProfileService -> getByOneProfileDetails" + e);

}
        return commonResponse;
    }

    @Override
    public SystemProfile findById(String systemProfilesId) {
        return systemProfileRepository.findById(Long.valueOf(systemProfilesId)).get();
    }

    @Override
    public CommonResponse getAllActiveProfilesPendingAndApprovedCategory(String requestStatus, String commonStatus, String categoryId,int page,int size) {
        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        List<SystemProfileDto> systemProfileDtoList = new ArrayList<>();
        Page<SystemProfile> SystemProfilePage;

        try {

            // Convert String to Enum
            CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
            RequestStatus requestStatusEnum = null;

            if (requestStatus != null && !requestStatus.isEmpty()) {
                requestStatusEnum = RequestStatus.valueOf(requestStatus);
            }

            if (commonStatus.equals("INACTIVE")) {
                SystemProfilePage = systemProfileRepository.findByCommonStatusAndCategoryCategoryId(commonStatusEnum, Long.valueOf(categoryId), pageable);
            } else {
                SystemProfilePage = systemProfileRepository.findByRequestStatusAndCommonStatusAndCategoryCategoryId(requestStatusEnum, commonStatusEnum, Long.valueOf(categoryId), pageable);
            }

            // Convert Employee entities to DTOs
            systemProfileDtoList = SystemProfilePage.getContent().stream()
                    .map(this::castSystemProfileyIntoSystemProfileDto)
                    .collect(Collectors.toList());

            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", SystemProfilePage.getNumber());
            paginationDetails.put("totalItems", SystemProfilePage.getTotalElements());
            paginationDetails.put("totalPages", SystemProfilePage.getTotalPages());

            // Set response payload properly
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("SystemProfile fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(systemProfileDtoList)); // Employee list
            commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details

        } catch (IllegalArgumentException e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Invalid request status or common status.");
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching SystemProfile.");
        }

        return commonResponse;
    }
    @Override
    public CommonResponse getAllProfilesWithSliderBar(String categoryId, int value,String requestStatus,String commonStatus,int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        List<SystemProfileDto> systemProfileDtoList = new ArrayList<>();
        Page<SystemProfile> SystemProfilePage;

        try {

            // Convert String to Enum
            CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
            RequestStatus requestStatusEnum = null;

            if (requestStatus != null && !requestStatus.isEmpty()) {
                requestStatusEnum = RequestStatus.valueOf(requestStatus);
            }

            if (commonStatus.equals("INACTIVE")) {
                SystemProfilePage = systemProfileRepository.findByCategoryCategoryIdAndSystemProfilesPriceBetweenAndCommonStatus(Long.valueOf(categoryId),0, value,commonStatusEnum, pageable);
            } else {
                SystemProfilePage = systemProfileRepository.findByCategoryCategoryIdAndSystemProfilesPriceBetweenAndCommonStatusAndRequestStatus(Long.valueOf(categoryId),0, value,commonStatusEnum,requestStatusEnum, pageable);
            }


            // Convert Employee entities to DTOs
            systemProfileDtoList = SystemProfilePage.getContent().stream()
                    .map(this::castSystemProfileyIntoSystemProfileDto)
                    .collect(Collectors.toList());

            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", SystemProfilePage.getNumber());
            paginationDetails.put("totalItems", SystemProfilePage.getTotalElements());
            paginationDetails.put("totalPages", SystemProfilePage.getTotalPages());

            // Set response payload properly
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("SystemProfile fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(systemProfileDtoList)); // Employee list
            commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details

        } catch (IllegalArgumentException e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Invalid request status or common status.");
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching SystemProfile." + e);
        }

        return commonResponse;
    }

    private SystemProfile SystemProfileDtoIntoSystemProfile( SystemProfileDto systemProfileDto) {
      SystemProfile systemProfile = new SystemProfile();
      systemProfile.setSystemProfilesName(systemProfileDto.getSystemProfilesName());
      systemProfile.setSystemProfilesId(Long.valueOf(systemProfileDto.getSystemProfilesId()));
      systemProfile.setSystemProfilesDiscription(systemProfileDto.getSystemProfilesDiscription());
      systemProfile.setRequestStatus(systemProfileDto.getRequestStatus());
      systemProfile.setSystemProfilesPrice(Integer.parseInt(systemProfileDto.getSystemProfilesPrice()));
      systemProfile.setCommonStatus(CommonStatus.ACTIVE);
      systemProfile.setEmployee(employeeService.findByemployeeId(systemProfileDto.getEmployeeDto().getEmployeeId()));
      systemProfile.setCategory(categoryService.findByCategoryId(systemProfileDto.getCategoryDto().getCategoryId()));
      systemProfile.setSubCategory(subCategoryService.findBySubCategoryId(systemProfileDto.getSubCategoryDto().getSubCategoryId()));
        return systemProfile;
    }
private SystemProfileDto castSystemProfileyIntoSystemProfileDto(SystemProfile systemProfile) {
    SystemProfileDto systemProfileDto = new SystemProfileDto();
    systemProfileDto.setSystemProfilesName(systemProfile.getSystemProfilesName());
    systemProfileDto.setSystemProfilesPrice(String.valueOf(systemProfile.getSystemProfilesPrice()));
    systemProfileDto.setSystemProfilesDiscription(systemProfile.getSystemProfilesDiscription());
    systemProfileDto.setSystemProfilesId(String.valueOf(systemProfile.getSystemProfilesId()));
    systemProfileDto.setRequestStatus(systemProfile.getRequestStatus());
    systemProfileDto.setCommonStatus(systemProfile.getCommonStatus());
    if(systemProfile.getEmployee() == null ) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId("Super Admin");
        systemProfileDto.setEmployeeDto(employeeDto);
    }else{
      systemProfileDto.setEmployeeDto(employeeService.findById(systemProfile.getEmployee().getEmployeeId()));
    }
    systemProfileDto.setCategoryDto(categoryService.findById(systemProfile.getCategory().getCategoryId()));
    if(systemProfile.getSubCategory() != null ) {
        systemProfileDto.setSubCategoryDto(subCategoryService.findById(systemProfile.getSubCategory().getSubCategoryId()));
    }else{
        SubCategoryDto subCategoryDto = new SubCategoryDto();
        subCategoryDto.setSubCategoryId("No Selected");
        systemProfileDto.setSubCategoryDto(subCategoryDto);    }
    return systemProfileDto;
}

    private List<String> SystemProfileValidation(SystemProfileDto systemProfileDto) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(systemProfileDto.getSystemProfilesName()))
            validationList.add("Profile name field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(systemProfileDto.getSystemProfilesDiscription())))
            validationList.add("Profile Discription field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(systemProfileDto.getSystemProfilesPrice())))
            validationList.add("Profile Prise field is empty");
        return validationList;
    }
}
