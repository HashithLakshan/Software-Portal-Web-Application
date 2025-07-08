package Software.Portal.web.controller;

import Software.Portal.web.dto.SystemProfileDto;
import Software.Portal.web.service.SystemProfileService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/SystemProfile")
@CrossOrigin
public class SystemProfileController {

    private final SystemProfileService systemProfileService;

    @Autowired
    public SystemProfileController(SystemProfileService systemProfileService) {
        this.systemProfileService = systemProfileService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save/Profile")
    CommonResponse saveSystemProfile(@RequestBody SystemProfileDto systemProfileDto) {
        return systemProfileService.save(systemProfileDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/Update/Profile")
    CommonResponse UpdateSystemProfile(@RequestBody SystemProfileDto systemProfileDto) {
        return systemProfileService.update(systemProfileDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getById")
    CommonResponse getBySystemProfileId(
            @RequestParam String commonStatus,
            @RequestParam String requestStatus,
            @RequestParam String systemProfileId) {
        return  systemProfileService.getById(systemProfileId,commonStatus,requestStatus);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getByEmployeeId")
    CommonResponse getBySystemEmployeeId(
            @RequestParam String commonStatus,
            @RequestParam String requestStatus,
            @RequestParam String employeeId) {
        return  systemProfileService.getByEmployeeId(employeeId,commonStatus,requestStatus);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/approve")
    CommonResponse updateSystemProfile(@RequestParam String systemProfilesId) {
        return systemProfileService.aproveStatus(systemProfilesId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/Inactive")
    CommonResponse updateSystemProfileInactive(@RequestParam  String systemProfilesId) {
        return systemProfileService.InactiveStatus(systemProfilesId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/Active")
    CommonResponse updateSystemProfileActive(@RequestParam String systemProfilesId) {
        return systemProfileService.ActiveStatus(systemProfilesId);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/DeletePermanent")
    CommonResponse updateSystemProfileDeletePermanent(@RequestParam String systemProfilesId) {
        return systemProfileService.DeletePermanent(systemProfilesId);
    }

    @GetMapping("/getAllActiveProfilesPendingAndApproved/{requestStatus}/{commonStatus}")
    CommonResponse getAllSystemProfiles(@PathVariable String requestStatus,@PathVariable String commonStatus) {
        return systemProfileService.getAllActiveProfilesPendingAndApproved(requestStatus,commonStatus);
    }
    @GetMapping("/getAllActiveProfilesPendingAndApprovedSuper")
    public CommonResponse getAllSystemProfiles(
            @RequestParam(required = false) String requestStatus,
            @RequestParam(required = false) String commonStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return systemProfileService.getAllActiveProfilesPendingAndApprovedSuper(commonStatus, requestStatus, page, size);
    }
    @GetMapping("/getAllActiveProfilesApprovedAndInactiveSuper")
    public CommonResponse getAllSystemProfilesSuper(
            @RequestParam(required = false) String requestStatus,
            @RequestParam(required = false) String commonStatus,
            @RequestParam(required = false) String employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return systemProfileService.getAllActiveProfilesApprovedAndInactiveSuper(commonStatus, requestStatus,employeeId, page, size);
    }
    @GetMapping("/getByIdSystem/{systemProfileId}")
    CommonResponse getBySystemProfileId(@PathVariable String systemProfileId) {
        return systemProfileService.getByOneProfileDetails(systemProfileId);
    }


    @GetMapping("/getAllActiveProfilesPendingAndApprovedCategory")
    CommonResponse getAllSystemProfiles(
            @RequestParam (required = false) String requestStatus,
            @RequestParam (required = false) String commonStatus,
            @RequestParam String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return systemProfileService.getAllActiveProfilesPendingAndApprovedCategory(requestStatus,commonStatus,categoryId,page,size);
    }

    @GetMapping("/getAllProfilesWithSliderBar")
    CommonResponse getAllProfilesWithSliderBar(
            @RequestParam String categoryId,
            @RequestParam int value,
            @RequestParam(required = false) String requestStatus,
            @RequestParam(required = false) String commonStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size ) {
        return systemProfileService.getAllProfilesWithSliderBar(categoryId,value,requestStatus,commonStatus,page,size);
    }

    @GetMapping("/getAllSearching")
    public CommonResponse SearchAll(
            @RequestParam(required = false) String inPut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return systemProfileService.searchAll(inPut, page, size);
    }
}
