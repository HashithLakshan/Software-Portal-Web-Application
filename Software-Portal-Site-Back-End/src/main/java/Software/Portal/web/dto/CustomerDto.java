package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDto {
    private String customerId;
    private String customerAddress;
    private String customerNumber;
    private String customerEmail;
    private String customerName;
    private String customerType;
    private String companyRegNo;
    private CommonStatus customerStatus;
    private SystemProfileDto systemProfileDto;


}
