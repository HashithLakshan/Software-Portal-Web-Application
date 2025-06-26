package Software.Portal.web.entity;

import Software.Portal.web.constant.CommonStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "System_Issues_Answer_Messages")
public class SystemIssuesAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "answer_Id")
    private  Long answerId;

    @Column(name = "answer_Subject",length = 200)
    private String answerSubject;

    @Column(name = "answer_Body",length = 2000)
    private String answerBody;

    @Column(name = "send_Date")
    private String sendDate;

    @Column(name = "send_Time")
    private String sendTime;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_Id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private SystemIssueMessages systemIssueMessages;

    @Enumerated
    private CommonStatus commonStatus;
}
