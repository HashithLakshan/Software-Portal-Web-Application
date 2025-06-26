package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SystemProfileDto {
    private  String systemProfilesId;
    private String systemProfilesName;
    private String systemProfilesPrice;
    private String systemProfilesDiscription;
    private EmployeeDto employeeDto;
    private CategoryDto categoryDto;
    private SubCategoryDto subCategoryDto;
    private CommonStatus commonStatus;
    private RequestStatus requestStatus;
}
