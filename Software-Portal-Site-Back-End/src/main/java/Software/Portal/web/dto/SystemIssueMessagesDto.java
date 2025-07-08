package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.MessageStatus;
import Software.Portal.web.constant.ReplyMessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SystemIssueMessagesDto {
    private  String issueId;
    private String subject;
    private String body;
    private ZoomDto zoomDto;
    private String receivedDate;
    private String receivedTime;
    private CommonStatus commonStatus;
    private ReplyMessageStatus replyMessageStatus;

}
