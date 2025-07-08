package Software.Portal.web.repository;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.entity.SystemIssueMessages;
import Software.Portal.web.entity.SystemIssuesAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface SystemIssuesAnswerRepository extends JpaRepository<SystemIssuesAnswer,Long> {

    SystemIssuesAnswer findBySystemIssueMessagesIssueId(Long issueId);

    SystemIssuesAnswer findBySystemIssueMessagesIssueIdAndCommonStatus(Long issueId, CommonStatus commonStatus);
}
