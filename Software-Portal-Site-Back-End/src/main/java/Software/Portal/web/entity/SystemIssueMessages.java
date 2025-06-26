package Software.Portal.web.entity;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.MessageStatus;
import Software.Portal.web.constant.ReplyMessageStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "System_Issues_Messages")
public class SystemIssueMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "issue_Id")
    private  Long issueId;

    @Column(name = "subject",length = 200)
    private String subject;

    @Column(name = "body",length = 2000)
    private String body;

    @Column(name = "received_Date")
    private LocalDate receivedDate;

    @Column(name = "received_Time")
    private LocalTime receivedTime;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perchase_Id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Zoom zoom;

    @Enumerated
    private CommonStatus commonStatus;

    @Enumerated
    private ReplyMessageStatus replyMessageStatus;
}
