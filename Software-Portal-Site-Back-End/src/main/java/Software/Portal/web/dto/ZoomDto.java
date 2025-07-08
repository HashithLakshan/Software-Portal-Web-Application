package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.entity.ZoomTimeSlots;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Data
public class ZoomDto {
    private String perchaseId;
    private String SheduleTopic;
    private String startDate;
    private String startTime;
    private String zoomLink;
    private String ZMeetingId;
    private String meetingPassword;
    private String meeting_Duration;
    private RequestStatus requestStatus;
    private CommonStatus commonStatus;
    private String customerAddress;
    private String customerNumber;
    private String customerEmail;
    private String customerName;
    private String customerType;
    private String companyRegNo;
    private ZoomTimeSlotsDto zoomTimeSlotsDto;
    private SystemProfileDto systemProfileDto;
}
