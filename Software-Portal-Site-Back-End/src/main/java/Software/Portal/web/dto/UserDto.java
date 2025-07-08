package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private String userId;
    private String userName;
    private String password;
    private String email;
    private RollDto rollDto;
    private CommonStatus commonStatus;
    private RequestStatus requestStatus;


}
