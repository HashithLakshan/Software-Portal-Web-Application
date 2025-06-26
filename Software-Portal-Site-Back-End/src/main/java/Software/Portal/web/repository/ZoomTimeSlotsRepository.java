package Software.Portal.web.repository;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.entity.ZoomTimeSlots;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@Repository
@EnableJpaRepositories
public interface ZoomTimeSlotsRepository extends JpaRepository<ZoomTimeSlots,Long> {

    ZoomTimeSlots findBySlotOpenTimeAndSlotCloseTime(String slotOpenTime, String slotCloseTime);


    Page<ZoomTimeSlots> findByCommonStatus(CommonStatus commonStatusEnum, Pageable pageable);
}
