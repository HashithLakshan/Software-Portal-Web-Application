package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageDto {
    private EmployeeDto employeeDto;
    private CommonStatus commonStatus;
}
