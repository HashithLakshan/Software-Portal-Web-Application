package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResitDto {
    private String id;
    private String fileName;
    private String fileType;
    private String saveDate;
    private SystemProfileDto systemProfileDto;
    private ZoomDto zoomDto;
    private CommonStatus commonStatus;
    private RequestStatus requestStatus;
    private byte[] data;

}
