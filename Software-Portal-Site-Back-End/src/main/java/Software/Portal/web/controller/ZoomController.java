package Software.Portal.web.controller;

import Software.Portal.web.service.serviceImpl.ZoomService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/zoom")
@CrossOrigin
public class ZoomController {

    private final ZoomService zoomService;

    @Autowired
    public ZoomController(ZoomService zoomService) {
        this.zoomService = zoomService;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/zoom/callback")
    public CommonResponse getAccessToken(@RequestParam String code) {
        return zoomService.getAccessToken(code);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllActiveProfilesPendingAndApprovedSuper")
    public CommonResponse getAllSystemProfiles(
            @RequestParam(required = false) String requestStatus,
            @RequestParam(required = false) String commonStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return zoomService.getAll(commonStatus, requestStatus, page, size);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllTodayPage")
    public CommonResponse getAllSystemProfilesPagesToday(
            @RequestParam(required = false) String requestStatus,
            @RequestParam(required = false) String commonStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return zoomService.getAllTodayPage(commonStatus,requestStatus,page, size);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateStatus/{changeStatus}/{perchaseId}")
    public CommonResponse updateStatus(@PathVariable String changeStatus,@PathVariable String perchaseId) {
        return zoomService.updateStatus(changeStatus,perchaseId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/schedule")
    public CommonResponse scheduleMeeting(  @RequestParam String perchaseId,
                                            @RequestParam String zoomTimeSlotId
                                            ,@RequestParam String topic,
                                          @RequestParam String startTime,
                                          @RequestParam String duration,
                                          @RequestParam String systemProfilesId,
                                          @RequestParam String rollName,
                                          @RequestParam String customerNumber,
                                          @RequestParam String customerAddress,
                                          @RequestParam String customerEmail,
                                          @RequestParam String customerName,
                                          @RequestParam String customerType,
                                          @RequestParam String companyRegNo
                                         )
                                           {
        return zoomService.createMeeting(perchaseId,zoomTimeSlotId,topic,startTime,duration,systemProfilesId,rollName,customerNumber,customerAddress,customerEmail,customerName,customerType,companyRegNo );
    }
    @PostMapping("/savePending")
    CommonResponse shedulePendingMeeting(@RequestParam String zoomTimeSlotId,
                                         @RequestParam String systemProfilesId,
                                         @RequestParam String StartDate,
                                         @RequestParam String topic,
                                         @RequestParam String customerNumber,
                                         @RequestParam String customerAddress,
                                         @RequestParam String customerEmail,
                                         @RequestParam String customerName,
                                         @RequestParam String customerType,
                                         @RequestParam String companyRegNo){
        return zoomService.savePending(zoomTimeSlotId ,systemProfilesId,StartDate,topic,  customerNumber, customerAddress, customerEmail, customerName, customerType, companyRegNo);

    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/scheduleApproving")
    public CommonResponse scheduleMeetingApproving(  @RequestParam String perchaseId,
                                            @RequestParam String zoomTimeSlotId
                                            ,@RequestParam String topic,
                                            @RequestParam String startTime,
                                            @RequestParam String duration,
                                            @RequestParam String systemProfilesId,
                                            @RequestParam String rollName,
                                            @RequestParam String customerNumber,
                                            @RequestParam String customerAddress,
                                            @RequestParam String customerEmail,
                                            @RequestParam String customerName,
                                            @RequestParam String customerType,
                                            @RequestParam String companyRegNo
    )
    {
        return zoomService.createMeetingApproving(perchaseId,zoomTimeSlotId,topic,startTime,duration,systemProfilesId,rollName,customerNumber,customerAddress,customerEmail,customerName,customerType,companyRegNo );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllToday")
    public CommonResponse getAllToday(@RequestParam String action,
                                      @RequestParam(required = false) String requestStatus,
                                      @RequestParam(required = false) String commonStatus){
            return zoomService.getAllToday(action,requestStatus,commonStatus);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-meeting/{meetingId}")
    public CommonResponse deleteMeeting(@PathVariable String meetingId) {
        return zoomService.deleteMeeting(meetingId);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getDetailsPerchaseId/{perchaseId}/{commonStatus}/{requestStatus}")
    public CommonResponse getDetailsPerchaseId(@PathVariable String perchaseId,@PathVariable String commonStatus
            ,@PathVariable String requestStatus) {
        return zoomService.getPerchaseDetails(perchaseId,commonStatus,requestStatus);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getDetailsZoomId/{zMeetingId}/{commonStatus}/{requestStatus}")
    public CommonResponse getZoomIdDetails(@PathVariable String zMeetingId,@PathVariable String commonStatus
            ,@PathVariable String requestStatus) {
        return zoomService.getZoomIdDetails(zMeetingId,commonStatus,requestStatus);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getDetailsAllDateFilter")
    public CommonResponse getDetailsAllDate(@RequestParam (required = false) String fromDate,
                                            @RequestParam (required = false) String toDate,
                                            @RequestParam (required = false) String commonStatus,
                                            @RequestParam (required = false) String requestStatus,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size) {
        return zoomService.getDetailsAllDateFilter(fromDate,toDate,commonStatus,requestStatus,page,size);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/reminderMeting")
    CommonResponse reminderMeting(){
            return zoomService.reminderZoom();
    }

}
