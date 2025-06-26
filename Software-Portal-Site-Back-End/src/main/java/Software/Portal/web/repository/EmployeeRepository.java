package Software.Portal.web.repository;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,String> {

    List<Employee> findByCommonStatusAndRequestStatus(CommonStatus commonStatus, RequestStatus requestStatus);

    Employee findByCommonStatusAndCompanyName(CommonStatus commonStatusEnum, String companyName);

    Employee findByCommonStatusAndRequestStatusAndCompanyName(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum, String companyName);

    Employee findByCommonStatusAndEmployeeId(CommonStatus commonStatusEnum, String employeeId);

    Employee findByCommonStatusAndRequestStatusAndEmployeeId(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum, String employeeId);

    Page<Employee> findByCommonStatus(CommonStatus commonStatusEnum, Pageable pageable);

    Page<Employee> findByCommonStatusAndRequestStatus(CommonStatus commonStatusEnum, RequestStatus requestStatusEnum, Pageable pageable);

    Optional<Employee> findByCompanyEmailAndCommonStatusAndRequestStatus(String companyEmail, CommonStatus active, RequestStatus approved);
}