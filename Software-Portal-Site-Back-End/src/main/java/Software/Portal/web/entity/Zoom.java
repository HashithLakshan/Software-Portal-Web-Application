package Software.Portal.web.entity;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Zoom_Shedules")
public class Zoom {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long perchaseId;

    @Column(name = "ZoomId")
    private String zMeetingId;

    @Column(name = "Shedule_Topic")
    private String SheduleTopic;

    @Column(name = "start_Date")
    private LocalDate startDate;


    @Column(name = "start_Time")
    private LocalTime startTime;

    @Column(name = "zoom_Link")
    private String zoomLink;

    @Column(name = "meeting_Duration")
    private String meeting_Duration;

    @Column(name = "meeting_Password")
    private String meetingPassword;

    @Column(name = "customer_Address")
    private String customerAddress;

    @Column(name = "customer_number")
    private String customerNumber;

    @Column(name = "customer_Email")
    private String customerEmail;

    @Column(name = "customer_Name")
    private String customerName;

    @Column(name = "company_RegNo")
    private String companyRegNo;


    @Column(name = "customer_Type")
    private String customerType;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "systemProfile_Id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private SystemProfile systemProfile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zoomTimeSlot_Id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private ZoomTimeSlots zoomTimeSlots;



    @Enumerated
    private RequestStatus requestStatus;

    @Enumerated
    private CommonStatus commonStatus;

}
