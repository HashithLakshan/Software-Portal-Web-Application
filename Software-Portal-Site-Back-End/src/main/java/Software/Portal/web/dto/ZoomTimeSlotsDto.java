package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ZoomTimeSlotsDto {
    private String zoomTimeSlotId;
    private String SlotOpenTime;
    private String SlotCloseTime;
    private CommonStatus commonStatus;
}
