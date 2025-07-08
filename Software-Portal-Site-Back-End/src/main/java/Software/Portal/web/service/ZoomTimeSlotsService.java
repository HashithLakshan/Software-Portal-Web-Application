package Software.Portal.web.service;

import Software.Portal.web.dto.ZoomTimeSlotsDto;
import Software.Portal.web.entity.ZoomTimeSlots;
import Software.Portal.web.utill.CommonResponse;

public interface ZoomTimeSlotsService {
    CommonResponse save(ZoomTimeSlotsDto zoomTimeSlotsDto);

    ZoomTimeSlots findById(String zoomTimeSlotId);

    CommonResponse getAll(String commonStatus);

    ZoomTimeSlotsDto findDetails(Long zoomTimeSlotId);

    CommonResponse getFilteredTimeSlots(String commonStatus, int page, int size);

    CommonResponse updateStatus(String zoomTimeSlotId, String commonStatus);
}
