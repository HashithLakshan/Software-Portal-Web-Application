package Software.Portal.web.service;

import Software.Portal.web.dto.EmployeeDto;
import Software.Portal.web.entity.Employee;
import Software.Portal.web.utill.CommonResponse;



public interface EmployeeService {


    CommonResponse saveUser(String rollName,EmployeeDto employeeDto);


    CommonResponse getAllFilter(String commonStatus, String requestStatus);

    CommonResponse getFilteredEmployees(String commonStatus, String requestStatus, int page, int size);

    CommonResponse getDetailsInId(String commonStatus, String requestStatus, String employeeId);

    CommonResponse getDetailsInUserName(String commonStatus, String requestStatus, String companyName);

    CommonResponse updateStatus(String employeeId,String requestStatus,String commonStatus);

    Employee findByemployeeId(String employeeId);

    EmployeeDto findById(String employeeId);
}
