package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.dto.SystemIssuesAnswerDto;
import Software.Portal.web.entity.SystemIssuesAnswer;
import Software.Portal.web.repository.SystemIssuesAnswerRepository;
import Software.Portal.web.service.SystemIssueMessagesService;
import Software.Portal.web.service.SystemIssuesAnswerService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SystemIssuesAnswerServiceImpl implements SystemIssuesAnswerService {


    private final SystemIssuesAnswerRepository systemIssuesAnswerRepository;

    private final SystemIssueMessagesService systemIssueMessagesService;

    @Autowired
    public SystemIssuesAnswerServiceImpl(SystemIssuesAnswerRepository systemIssuesAnswerRepository, SystemIssueMessagesService systemIssueMessagesService) {
        this.systemIssuesAnswerRepository = systemIssuesAnswerRepository;
        this.systemIssueMessagesService = systemIssueMessagesService;
    }

    @Override
    public CommonResponse getInformation(String issueId) {
        CommonResponse commonResponse = new CommonResponse();
        SystemIssuesAnswerDto systemIssuesAnswerDto = new SystemIssuesAnswerDto();
        SystemIssuesAnswer systemIssuesAnswer = new SystemIssuesAnswer();

        systemIssuesAnswer = systemIssuesAnswerRepository.findBySystemIssueMessagesIssueId(Long.valueOf(issueId));

        systemIssuesAnswerDto.setAnswerId(String.valueOf(systemIssuesAnswer.getAnswerId()));
        systemIssuesAnswerDto.setAnswerSubject(systemIssuesAnswer.getAnswerSubject());
        systemIssuesAnswerDto.setAnswerBody(systemIssuesAnswer.getAnswerBody());
        systemIssuesAnswerDto.setSendDate(systemIssuesAnswer.getSendDate());
        systemIssuesAnswerDto.setSendTime(systemIssuesAnswer.getSendTime());
        systemIssuesAnswerDto.setSystemIssueMessagesDto(systemIssueMessagesService.findll(systemIssuesAnswer.getSystemIssueMessages().getIssueId()));
        commonResponse.setPayload(Collections.singletonList(systemIssuesAnswerDto));
        commonResponse.setStatus(true);
        return commonResponse;
    }
}
