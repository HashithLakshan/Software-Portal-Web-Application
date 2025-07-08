package Software.Portal.web.service;

import Software.Portal.web.dto.UserDto;
import Software.Portal.web.entity.User;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserService {
    CommonResponse saveUser(UserDto userDto);

    CommonResponse getDetalisUser(String email, String password);




    User findBySiteUserId(String userId);

    CommonResponse requestApproved(String userId);

    UserDto findById(String userId);


    CommonResponse sendCode(String email);

    CommonResponse recover(String sendCode,String email,String password);

    void clearCodeIfExpired();
}
