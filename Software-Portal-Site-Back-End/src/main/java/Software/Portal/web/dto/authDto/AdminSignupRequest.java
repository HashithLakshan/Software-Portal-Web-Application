package Software.Portal.web.dto.authDto;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.dto.RollDto;
import lombok.Data;

@Data
public class AdminSignupRequest {
    private String userId;
    private String userName;
    private String password;
    private String email;
    private RollDto rollDto;
    private CommonStatus commonStatus;
    private RequestStatus requestStatus;
}
