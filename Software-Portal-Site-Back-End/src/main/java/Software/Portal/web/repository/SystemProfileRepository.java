package Software.Portal.web.repository;


import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.entity.SystemProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface SystemProfileRepository extends JpaRepository<SystemProfile,Long> {

//    List<SystemProfile> findByCategoryCategoryIdAndRequestStatusAndCommonStatus(Long categoryId, RequestStatus requestStatus1, CommonStatus commonStatus1);
//
//    @Query(value = "SELECT * FROM System_Profiles WHERE category_Id = :categoryId AND System_Profile_Price BETWEEN  0 AND :value", nativeQuery = true)
//    List<SystemProfile> findByCategoryCategoryIdAndValueBetween(
//            @Param("categoryId") Long categoryId,
//            @Param("value") int value);

    Page<SystemProfile> findByCommonStatus(CommonStatus commonStatusEnum, Pageable pageable);


    Page<SystemProfile> findByCommonStatusAndRequestStatus(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum, Pageable pageable);

    List<SystemProfile> findByCommonStatusAndRequestStatus(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum);

    SystemProfile findByCommonStatusAndSystemProfilesId( CommonStatus commonStatus1, Long systemProfileId);

    SystemProfile findByCommonStatusAndRequestStatusAndSystemProfilesId(CommonStatus commonStatus1, RequestStatus requestStatus1, Long systemProfileId);

    List<SystemProfile> findByCommonStatusAndEmployeeEmployeeId(CommonStatus commonStatus1, String employeeId);

    List<SystemProfile> findByCommonStatusAndRequestStatusAndEmployeeEmployeeId(CommonStatus commonStatus1, RequestStatus requestStatus1, String employeeId);


    Page<SystemProfile> findByCommonStatusAndEmployeeEmployeeId(CommonStatus commonStatusEnum, String employeeId, Pageable pageable);

    Page<SystemProfile> findByCommonStatusAndRequestStatusAndEmployeeEmployeeId(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum, String employeeId, Pageable pageable);

    Page<SystemProfile> findByCategoryCategoryIdAndSystemProfilesPriceBetweenAndCommonStatus(
            Long categoryId,
            int minPrice,  // ✅ First value (lower range)
            int value,  // ✅ Second value (upper range)
            CommonStatus commonStatusEnum,
            Pageable pageable
    );

    Page<SystemProfile> findByCategoryCategoryIdAndSystemProfilesPriceBetweenAndCommonStatusAndRequestStatus(
            Long categoryId,
            int minPrice,
            int value,
            CommonStatus commonStatusEnum,
            RequestStatus requestStatusEnum,
            Pageable pageable
    );

    Page<SystemProfile> findByCommonStatusAndCategoryCategoryId(CommonStatus commonStatusEnum, Long categoryId, Pageable pageable);

    Page<SystemProfile> findByRequestStatusAndCommonStatusAndCategoryCategoryId(RequestStatus requestStatusEnum, CommonStatus commonStatusEnum, Long categoryId, Pageable pageable);

    Page<SystemProfile> findByCommonStatusAndRequestStatusAndCategoryCategoryName(CommonStatus commonStatus, RequestStatus requestStatus, String inPut, Pageable pageable);

//    Page<SystemProfile> findByCommonStatusAndRequestStatusAndSystemProfilesPrice(CommonStatus commonStatus, RequestStatus requestStatus, int inPut, Pageable pageable);

    Page<SystemProfile> findByCommonStatusAndRequestStatusAndSystemProfilesName(CommonStatus commonStatus, RequestStatus requestStatus, String inPut, Pageable pageable);

    Page<SystemProfile> findByCommonStatusAndRequestStatusAndSystemProfilesPriceBetween(CommonStatus commonStatus, RequestStatus requestStatus, int minPrice, int inPut, Pageable pageable);
}
