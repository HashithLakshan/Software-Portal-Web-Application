package Software.Portal.web.controller;

import Software.Portal.web.service.SystemIssuesAnswerService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/answerMessages")
@CrossOrigin
public class SystemIssuesAnswerController {

    private final SystemIssuesAnswerService systemIssuesAnswerService;

    @Autowired
    public SystemIssuesAnswerController(SystemIssuesAnswerService systemIssuesAnswerService) {
        this.systemIssuesAnswerService = systemIssuesAnswerService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getInformation/{issueId}")
    CommonResponse getInformation(@PathVariable("issueId") String issueId) {
        return systemIssuesAnswerService.getInformation(issueId);
    }
}
