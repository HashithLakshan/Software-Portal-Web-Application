package Software.Portal.web.controller;

import Software.Portal.web.dto.SystemIssueMessagesDto;
import Software.Portal.web.entity.SystemIssueMessages;
import Software.Portal.web.service.SystemIssueMessagesService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/issuesMessages")
@CrossOrigin
public class SystemIssueMessagesController {

    private final SystemIssueMessagesService systemIssueMessagesService;

    @Autowired
    public SystemIssueMessagesController(SystemIssueMessagesService systemIssueMessagesService) {
        this.systemIssueMessagesService = systemIssueMessagesService;
    }


    @PostMapping("/save")
    CommonResponse save (@RequestBody SystemIssueMessagesDto systemIssueMessagesDto) {
        return systemIssueMessagesService.save(systemIssueMessagesDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/filtered")
    public CommonResponse getFilteredEmployees(
            @RequestParam(required = false) String commonStatus,
            @RequestParam(required = false) String replyMessageStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return systemIssueMessagesService.getFilteredMessages(commonStatus, replyMessageStatus, page, size);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateStatus")
    CommonResponse updateStatus (@RequestParam String status,
                                 @RequestParam String issueId,
                                 @RequestParam String subject,
                                 @RequestParam String body) {
        return systemIssueMessagesService.updateStatus(status,issueId,subject,body);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getPerchaseIdWithDetails")
CommonResponse getPerchaseIdWithDetails (@RequestParam String perchaseId,
                                         @RequestParam String status) {
        return systemIssueMessagesService.detailsPerchaseId(perchaseId,status);
}

    @PreAuthorize("hasRole('ROLE_ADMIN')")
@GetMapping("/issueIdDetails")
CommonResponse issueIdDetails (@RequestParam String issueId,
                                @RequestParam String status) {
    return systemIssueMessagesService.DetailsIssueId(issueId,status);

}


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getDetailsAllDateFilter")
    public CommonResponse getDetailsAllDate(@RequestParam (required = false) String fromDate,
                                            @RequestParam (required = false) String toDate,
                                            @RequestParam (required = false) String commonStatus,
                                            @RequestParam (required = false) String replyMessageStatus,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size) {
        return systemIssueMessagesService.getDetailsAllDateFilter(fromDate,toDate,commonStatus,replyMessageStatus,page,size);
    }
}
