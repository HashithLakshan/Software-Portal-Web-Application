package Software.Portal.web.repository;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.entity.Zoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ZoomRepository extends JpaRepository<Zoom,Long> {
    Page<Zoom> findByCommonStatus(CommonStatus commonStatusEnum, Pageable pageable);

    Page<Zoom> findByCommonStatusAndRequestStatus(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum,
                                                  Pageable pageable);

    Optional<Zoom> findByZoomTimeSlotsZoomTimeSlotIdAndStartDateAndRequestStatusInAndCommonStatus(
            Long zoomTimeSlotId,
            LocalDate startDate,
            List<String> requestStatus,
            CommonStatus commonStatus
    );

    Zoom findByzMeetingIdAndCommonStatusAndRequestStatus(String zMeetingId, CommonStatus commonStatus1, RequestStatus requestStatus1);

    Zoom findByzMeetingIdAndCommonStatus(String zMeetingId, CommonStatus commonStatus1);

    Zoom findByCommonStatusAndPerchaseId(CommonStatus commonStatus1,Long perchaseId);

    Zoom findByCommonStatusAndRequestStatusAndPerchaseId(CommonStatus commonStatus1, RequestStatus requestStatus1, Long perchaseId);

    Page<Zoom> findByStartDateBetweenAndCommonStatus(LocalDate fromDateD, LocalDate toDateD, CommonStatus commonStatus1, Pageable pageable);

    Page<Zoom> findByStartDateBetweenAndCommonStatusAndRequestStatus(LocalDate fromDateD, LocalDate toDateD, CommonStatus commonStatus1, RequestStatus requestStatus1, Pageable pageable);

    Zoom findByStartDateAndStartTimeAndCommonStatusAndRequestStatus(LocalDate today, LocalTime now ,CommonStatus commonStatus, RequestStatus requestStatus);


    List<Zoom> findByCommonStatusAndRequestStatusAndStartDate(CommonStatus commonStatus, RequestStatus requestStatus, LocalDate currentDate);

    List<Zoom> findByCommonStatusAndRequestStatus(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum);

    Page<Zoom> findByCommonStatusAndRequestStatusAndStartDate(CommonStatus commonStatus, RequestStatus requestStatus, LocalDate currentDate, Pageable pageable);

}
