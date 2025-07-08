package Software.Portal.web.controller;

import Software.Portal.web.dto.ZoomTimeSlotsDto;
import Software.Portal.web.service.ZoomTimeSlotsService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timeSlots")
@CrossOrigin
public class ZoomTimeSlotsController {

    private  final ZoomTimeSlotsService zoomTimeSlotsService;

    @Autowired
    public ZoomTimeSlotsController(ZoomTimeSlotsService zoomTimeSlotsService) {
        this.zoomTimeSlotsService = zoomTimeSlotsService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public CommonResponse saveTimeSlots(@RequestBody ZoomTimeSlotsDto zoomTimeSlotsDto) {
        return zoomTimeSlotsService.save(zoomTimeSlotsDto);
    }

    @GetMapping("/getAll/{commonStatus}")
    CommonResponse getAllTimeSlots(@PathVariable String commonStatus) {
        return zoomTimeSlotsService.getAll(commonStatus);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/filtered")
    public CommonResponse getFilteredTimeSlots(
            @RequestParam(required = false) String commonStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return zoomTimeSlotsService.getFilteredTimeSlots(commonStatus, page, size);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/delete")
    CommonResponse  deleteTimeSlot (@RequestParam String zoomTimeSlotId,@RequestParam String commonStatus) {
        return zoomTimeSlotsService.updateStatus(zoomTimeSlotId,commonStatus);
    }

}
