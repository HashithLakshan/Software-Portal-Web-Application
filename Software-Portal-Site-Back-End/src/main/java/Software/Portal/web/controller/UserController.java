package Software.Portal.web.controller;

import Software.Portal.web.dto.UserDto;
import Software.Portal.web.service.UserService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {
    private final UserService userService;



@Autowired
    public UserController(UserService userService) {
        this.userService = userService;

}

    @PostMapping("/save/user")
    CommonResponse saveUser (@RequestBody UserDto userDto){
        return userService.saveUser(userDto);
    }
    @PostMapping("/findByEmailAndUserPassword")
    public CommonResponse getUserDetails(@RequestBody UserDto UserDto) {
        return userService.getDetalisUser(UserDto.getEmail(), UserDto.getPassword());
    }
    @PutMapping("/approvedRequest/{userId}")
    CommonResponse approvedRequestLogging (@PathVariable String userId) {
        return userService.requestApproved(userId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/forgetPassword")
    CommonResponse forgetPassword(@RequestParam ("email") String email){
        return userService.sendCode(email);
        }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/recover")
    CommonResponse recover(@RequestParam ("sendCode") String sendCode,@RequestParam ("email")
    String email,@RequestParam ("password") String password){
    return userService.recover(sendCode,email,password);
    }
}