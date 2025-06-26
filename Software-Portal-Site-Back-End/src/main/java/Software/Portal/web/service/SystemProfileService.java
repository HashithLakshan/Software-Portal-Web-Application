package Software.Portal.web.service;

import Software.Portal.web.dto.SystemProfileDto;
import Software.Portal.web.entity.SystemProfile;
import Software.Portal.web.utill.CommonResponse;

public interface SystemProfileService {
    CommonResponse save(SystemProfileDto systemProfileDto);

    CommonResponse aproveStatus(String systemProfilesId);

    CommonResponse getAllActiveProfilesPendingAndApproved(String requestStatus,String commonStatus);

    SystemProfile findByProfileId(String systemProfilesId);

    CommonResponse getByOneProfileDetails(String systemProfileId);

    SystemProfile findById(String systemProfilesId);

    CommonResponse getAllActiveProfilesPendingAndApprovedCategory(String requestStatus, String commonStatus, String categoryId,int page,int size);

    CommonResponse getAllProfilesWithSliderBar(String categoryId, int value,String requestStatus,String commonStatus,int page, int size);

    CommonResponse getAllActiveProfilesPendingAndApprovedSuper(String commonStatus, String requestStatus, int page, int size);

    CommonResponse InactiveStatus(String systemProfilesId);

    CommonResponse ActiveStatus(String systemProfilesId);

    CommonResponse DeletePermanent(String systemProfilesId);

    CommonResponse update(SystemProfileDto systemProfileDto);

    SystemProfileDto findDetails(Long systemProfilesId);

    CommonResponse getById(String systemProfileId, String commonStatus, String requestStatus);

    CommonResponse getByEmployeeId(String employeeId, String commonStatus, String requestStatus);

    CommonResponse getAllActiveProfilesApprovedAndInactiveSuper(String commonStatus, String requestStatus,String employeeId, int page, int size);


    CommonResponse searchAll(String inPut, int page, int size);
}
