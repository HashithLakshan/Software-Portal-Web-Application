package Software.Portal.web.repository;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.entity.PaymentResit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface PaymentResitRepository extends JpaRepository<PaymentResit, Long> {
    Page<PaymentResit> findByCommonStatus(CommonStatus commonStatusEnum, Pageable pageable);

    Page<PaymentResit> findByCommonStatusAndRequestStatus(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum, Pageable pageable);

    PaymentResit findByCommonStatusAndRequestStatusAndId(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum, Long id);

    PaymentResit findByZoomPerchaseId(Long perchaseId);

    PaymentResit findByCommonStatusAndRequestStatusAndZoomPerchaseId(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum, Long perchaseId);

    Page<PaymentResit> findBySaveDateBetweenAndCommonStatus(LocalDate fromDateD, LocalDate toDateD, CommonStatus commonStatus1, Pageable pageable);

    Page<PaymentResit> findBySaveDateBetweenAndCommonStatusAndRequestStatus(LocalDate fromDateD, LocalDate toDateD, CommonStatus commonStatus1, RequestStatus requestStatus1, Pageable pageable);

    List<PaymentResit> findByCommonStatusAndRequestStatus(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum);

}
