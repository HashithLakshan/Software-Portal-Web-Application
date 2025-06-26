package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDto {
    private String employeeId;
    private String employeeNumber;
//    private String gender;
    private String companyRgNo;
    private String companyName;
    private String employeeNIC;
    private CommonStatus commonStatus;
    private String companyEmail;
    private RequestStatus requestStatus;;

}
