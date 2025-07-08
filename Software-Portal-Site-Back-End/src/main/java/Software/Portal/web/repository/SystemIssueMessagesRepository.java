package Software.Portal.web.repository;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.ReplyMessageStatus;
import Software.Portal.web.entity.SystemIssueMessages;
import Software.Portal.web.entity.SystemIssuesAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@EnableJpaRepositories
public interface SystemIssueMessagesRepository  extends JpaRepository<SystemIssueMessages,Long> {

    Page<SystemIssueMessages> findByCommonStatus(CommonStatus commonStatusEnum, Pageable pageable);

    Page<SystemIssueMessages> findByCommonStatusAndReplyMessageStatus(CommonStatus commonStatusEnum, ReplyMessageStatus messageStatus, Pageable pageable);

    Page<SystemIssueMessages> findByReceivedDateBetweenAndCommonStatus(LocalDate fromDateD, LocalDate toDateD, CommonStatus commonStatus1, Pageable pageable);

    Page<SystemIssueMessages> findByReceivedDateBetweenAndCommonStatusAndReplyMessageStatus(LocalDate fromDateD, LocalDate toDateD, CommonStatus commonStatus1, ReplyMessageStatus replyMessageStatus1, Pageable pageable);


    SystemIssueMessages findByZoomPerchaseIdAndReplyMessageStatusAndCommonStatus(Long aLong, ReplyMessageStatus replyMessageStatus, CommonStatus commonStatus);

    SystemIssueMessages findByZoomPerchaseIdAndCommonStatus(Long perchaseId, CommonStatus commonStatus);

    SystemIssueMessages findByReplyMessageStatusAndCommonStatusAndIssueId( ReplyMessageStatus replyMessageStatus, CommonStatus commonStatus,Long issueId);

    SystemIssueMessages findByCommonStatusAndIssueId(CommonStatus commonStatus, Long issueId);
}
