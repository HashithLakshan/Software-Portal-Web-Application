package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.ReplyMessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SystemIssuesAnswerDto {
    private  String answerId;
    private String answerSubject;
    private String answerBody;
    private String sendDate;
    private String sendTime;
    private CommonStatus commonStatus;
    private SystemIssueMessagesDto systemIssueMessagesDto;
}
