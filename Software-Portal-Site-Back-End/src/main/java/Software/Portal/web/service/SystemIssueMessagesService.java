package Software.Portal.web.service;

import Software.Portal.web.dto.SystemIssueMessagesDto;
import Software.Portal.web.utill.CommonResponse;

public interface SystemIssueMessagesService {
    CommonResponse getFilteredMessages(String commonStatus, String replyMessageStatus, int page, int size);

    CommonResponse save(SystemIssueMessagesDto systemIssueMessagesDto);

    CommonResponse updateStatus(String status,String issueId,String subject,String body);

    CommonResponse getDetailsAllDateFilter(String fromDate, String toDate, String commonStatus, String replyMessageStatus, int page, int size);

    SystemIssueMessagesDto findll(Long issueId);

    CommonResponse detailsPerchaseId(String perchaseId,String status);

    CommonResponse DetailsIssueId(String issueId, String status);
}
